package com.baulsupp.kolja.log.viewer.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.baulsupp.kolja.log.line.BasicLineIterator;
import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.line.LineIndex;
import com.baulsupp.kolja.log.util.ReloadableCharBuffer;
import com.baulsupp.kolja.log.util.SystemInCharSequence;
import com.baulsupp.kolja.log.viewer.importing.LogFormat;
import com.baulsupp.kolja.log.viewer.request.StandardRequestIndex;

public class IoUtil {
  private static final Logger log = Logger.getLogger(IoUtil.class);

  @Deprecated
  public static CharSequence fromStdin() {
    final SystemInCharSequence charSequence = new SystemInCharSequence(System.in);

    Thread t = new Thread("InputProcessor") {
      public void run() {
        try {
          charSequence.readFully();
        } catch (IOException e) {
          log.error("error", e);
        }
      }
    };
    t.setDaemon(true);
    t.start();

    return charSequence;
  }

  public static CharSequence fromFile(File file) throws IOException {
    return ReloadableCharBuffer.fromFileReloadable(file);
  }

  public static Iterator<Line> loadByRequests(CharSequence buffer, LogFormat format) {
    LineIndex lineIndex = format.getLineIndex(buffer);

    StandardRequestIndex requestIndex = format.getRequestIndex(lineIndex);

    return new BasicLineIterator(requestIndex);
  }

  @Deprecated
  public static BasicLineIterator load(CharSequence buffer, LogFormat format) {
    LineIndex lineIndex = format.getLineIndex(buffer);

    return new BasicLineIterator(lineIndex);
  }

  public static Iterator<Line> loadLineIterator(LogFormat format, boolean byRequest, File f) throws IOException {
    Iterator<Line> bli;
    if (byRequest) {
      bli = IoUtil.loadByRequests(IoUtil.fromFile(f), format);
    } else {
      bli = IoUtil.load(IoUtil.fromFile(f), format);
    }
    return bli;
  }

  public static Iterator<Line> loadFiles(String[] args, boolean byRequest, LogFormat format) throws IOException {
    Iterator<Line> bli;
    if (args.length == 1) {
      File f = new File(args[0]);

      bli = IoUtil.loadLineIterator(format, byRequest, f);
    } else {
      MultipleLineIterator mli = new MultipleLineIterator();

      for (String s : args) {
        File file = new File(s);
        mli.add(getShortName(file), IoUtil.loadLineIterator(format, byRequest, file));
      }

      bli = mli;
    }
    return bli;
  }

  private static String getShortName(File file) {
    return file.getName();
  }

  public static Iterator<Line> loadFiles(LogFormat format, List<File> files) throws IOException {
    if (files.isEmpty()) {
      return new ArrayList<Line>().iterator();
    } else if (files.size() == 1) {
      return FileLineIterator.load(format, files.get(0));
    } else {
      MultipleLineIterator mli = new MultipleLineIterator();

      for (File file : files) {
        mli.add(getShortName(file), FileLineIterator.load(format, file));
      }

      return mli;
    }
  }

  public static Iterator<Line> tailFiles(LogFormat format, List<File> files) throws IOException {
    if (files.isEmpty()) {
      return new ArrayList<Line>().iterator();
    } else if (files.size() == 1) {
      FileLineIterator i = FileLineIterator.load(format, files.get(0));
      i.setTailing(true);
      return i;
    } else {
      MultipleLineIterator mli = new MultipleLineIterator();

      for (File file : files) {
        log.info("opening " + file);
        FileLineIterator i = FileLineIterator.load(format, file);
        i.setTailing(true);
        mli.add(getShortName(file), i);
      }

      return mli;
    }
  }

  public static Iterator<Line> loadFromStdin(LogFormat format) {
    // TODO Auto-generated method stub
    return null;
  }

  private static int count;

  public static void writeContent(File file, String... strings) throws IOException {
    FileWriter fw = new FileWriter(file, true);

    try {
      for (String string : strings) {
        fw.write(Integer.toString(count++));
        fw.write(" - ");
        fw.write(string);
        fw.write("\n");
        fw.flush();
      }
    } finally {
      fw.close();
    }
  }

  public static File createTestFile() throws IOException {
    return File.createTempFile("aaa", ".log");
  }
}
