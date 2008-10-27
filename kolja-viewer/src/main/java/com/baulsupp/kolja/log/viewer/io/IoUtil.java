/**
 * Copyright (c) 2002-2007 Yuri Schimke. All Rights Reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package com.baulsupp.kolja.log.viewer.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baulsupp.kolja.log.line.BasicLineIterator;
import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.line.LineIndex;
import com.baulsupp.kolja.log.util.ReloadableCharBuffer;
import com.baulsupp.kolja.log.util.SystemInCharSequence;
import com.baulsupp.kolja.log.viewer.importing.LogFormat;

public class IoUtil {
  private static final Logger log = LoggerFactory.getLogger(IoUtil.class);

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

  public static Iterator<Line> loadFiles(String[] args, LogFormat format) throws IOException {
    Iterator<Line> bli;
    if (args.length == 1) {
      File f = new File(args[0]);

      bli = IoUtil.loadLineIterator(format, fromFile(f));
    } else {
      MultipleLineIterator mli = new MultipleLineIterator();

      for (String s : args) {
        File file = new File(s);
        mli.add(getShortName(file), IoUtil.loadLineIterator(format, fromFile(file)));
      }

      bli = mli;
    }
    return bli;
  }

  public static Iterator<Line> loadLineIterator(LogFormat format, CharSequence sequence) {
    LineIndex lineIndex = format.getLineIndex(sequence);

    return new BasicLineIterator(lineIndex);
  }

  private static String getShortName(File file) {
    return file.getName();
  }

  public static Iterator<Line> loadFiles(LogFormat format, List<File> files, boolean end) throws IOException {
    if (files.isEmpty()) {
      return new ArrayList<Line>().iterator();
    } else if (files.size() == 1) {
      return FileLineIterator.load(format, files.get(0), end);
    } else {
      MultipleLineIterator mli = new MultipleLineIterator();

      for (File file : files) {
        mli.add(getShortName(file), FileLineIterator.load(format, file, end));
      }

      return mli;
    }
  }

  public static Iterator<Line> loadFromStdin(LogFormat format) {
    throw new UnsupportedOperationException();
  }

  public static void writeContent(File file, String... strings) throws IOException {
    FileWriter fw = new FileWriter(file, true);

    int count = 0;

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

  public static int getMaxFilenameWidth(List<File> files) {
    int result = 0;

    for (File file : files) {
      result = Math.max(result, file.getName().length());
    }

    return result;
  }
}
