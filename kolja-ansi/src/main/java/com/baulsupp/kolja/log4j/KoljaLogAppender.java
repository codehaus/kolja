package com.baulsupp.kolja.log4j;

import java.util.Map;

import org.joda.time.DateTime;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.AppenderBase;

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

public class KoljaLogAppender extends AppenderBase<LoggingEvent> {
  private ConsoleRenderer<Line> renderer = null;

  @Override
  public void start() {
    super.start();

    Renderer<Line> gridRenderer;
    try {
      LogFormat logFormat = SavedLogFormatLoader.load(name);
      gridRenderer = logFormat.getLineRenderer();
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

  private Line buildLine(LoggingEvent le) {
    Line line = new BasicLine(le.toString());

    line.setValue(LogConstants.DATE, new DateTime(le.getTimeStamp()));
    line.setValue(LogConstants.CONTENT, le.getMessage());

    line.setValue(LogConstants.EXCEPTION, join(le.getThrowableInformation().getThrowableStrRep()));
    line.setValue(LogConstants.PRIORITY, Priority.getByName(le.getLevel().toString()));
    line.setValue(LogConstants.LOGGER, le.getLoggerRemoteView().getName());
    line.setValue(LogConstants.THREAD, le.getThreadName());

    for (Map.Entry<String, String> e : le.getMDCPropertyMap().entrySet()) {
      line.setValue(e.getKey(), e.getValue());
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
}
