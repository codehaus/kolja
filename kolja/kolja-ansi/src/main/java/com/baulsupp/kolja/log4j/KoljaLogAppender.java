package com.baulsupp.kolja.log4j;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.apache.log4j.spi.LoggingEvent;

import com.baulsupp.kolja.ansi.ConsoleRenderer;
import com.baulsupp.kolja.ansi.TailRenderer;
import com.baulsupp.kolja.log.LogConstants;
import com.baulsupp.kolja.log.line.BasicLine;
import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.line.type.Priority;
import com.baulsupp.kolja.log.viewer.importing.LogFormat;
import com.baulsupp.kolja.log.viewer.importing.SavedLogFormatLoader;
import com.baulsupp.kolja.log.viewer.renderer.Renderer;
import com.baulsupp.kolja.util.PlatformUtil;

public class KoljaLogAppender extends AppenderSkeleton {
  private ConsoleRenderer<Line> renderer = null;

  @Override
  public void activateOptions() {
    // TODO am I just being paranoid
    Logger.getLogger("com.baulsupp.kolja").setLevel(Level.OFF);

    super.activateOptions();

    Renderer<Line> gridRenderer;
    try {
      LogFormat logFormat = SavedLogFormatLoader.load(name);
      gridRenderer = logFormat.getRenderer();
    } catch (Exception e) {
      System.err.println(e);
      gridRenderer = Log4JRenderer.create();
    }
    
    renderer = new TailRenderer(gridRenderer, !PlatformUtil.isWindows());
  }

  @Override
  protected void append(LoggingEvent arg0) {
    try {
      Line l = buildLine(arg0);
      renderer.show(l);
    } catch (Exception e) {
      System.err.println(e);
    }
  }

  private Line buildLine(LoggingEvent arg0) {
    Line line = new BasicLine(arg0.toString());

    line.setValue(LogConstants.DATE, new Date(arg0.timeStamp));
    line.setValue(LogConstants.CONTENT, arg0.getMessage());

    line.setValue(LogConstants.EXCEPTION, join(arg0.getThrowableStrRep()));
    line.setValue(LogConstants.PRIORITY, Priority.getByName(arg0.getLevel().toString()));
    line.setValue(LogConstants.LOGGER, arg0.getLoggerName());
    line.setValue(LogConstants.THREAD, arg0.getThreadName());
    
    for (Object e : MDC.getContext().entrySet()) {
      Entry entry = (Map.Entry) e;
      line.setValue((String) entry.getKey(), entry.getValue());
    }
    

    return line;
  }

  private String join(String[] throwableStrRep) {
    if (throwableStrRep == null) {
      return null;
    }

    StringBuilder buffer = new StringBuilder();

    for (String string : throwableStrRep) {
      if (buffer.length() > 0) {
        buffer.append('\n');
      }

      buffer.append(string);
    }

    return buffer.toString();
  }

  @Override
  public void close() {
  }

  @Override
  public boolean requiresLayout() {
    return false;
  }
}
