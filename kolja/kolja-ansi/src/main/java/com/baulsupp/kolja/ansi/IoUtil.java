package com.baulsupp.kolja.ansi;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

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
    LineIndex lineIndex = format.getLineIndex(buffer, LogFormat.Direction.FORWARD_ONLY);

    StandardRequestIndex requestIndex = format.getRequestIndex(lineIndex);

    return new BasicLineIterator(requestIndex);
  }

  public static BasicLineIterator load(CharSequence buffer, LogFormat format) {
    LineIndex lineIndex = format.getLineIndex(buffer, LogFormat.Direction.ANY_DIRECTION);

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

}
