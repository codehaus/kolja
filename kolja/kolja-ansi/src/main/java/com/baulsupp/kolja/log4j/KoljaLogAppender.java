package com.baulsupp.kolja.log4j;

import java.io.IOException;
import java.util.Date;

import org.apache.log4j.AppenderSkeleton;
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

public class KoljaLogAppender extends AppenderSkeleton {
  private ConsoleRenderer<Line> renderer = null;
  private LogFormat logFormat;
  
  @Override
  public void activateOptions() {
    super.activateOptions();
    
    try {
      logFormat = loadFormat();
      
      Renderer<Line> gridRenderer = logFormat.getRenderer();
      renderer = new TailRenderer(gridRenderer, true);
    } catch (Exception e) {
      e.printStackTrace();
      renderer = new ConsoleRenderer<Line>() {
        public void show(Line item) {
          System.out.println(item);
        }
      };
    }
  }
  
  private LogFormat loadFormat() throws IOException {
    return SavedLogFormatLoader.load(name);
  }

  @Override
  protected void append(LoggingEvent arg0) {
    Line l = buildLine(arg0);
    renderer.show(l);
  }

  private Line buildLine(LoggingEvent arg0) {
    Line line = new BasicLine();
    
    line.setValue(LogConstants.DATE, new Date(arg0.timeStamp));
    line.setValue(LogConstants.CONTENT, arg0.getMessage());

    line.setValue(LogConstants.EXCEPTION, join(arg0.getThrowableStrRep()));
    line.setValue(LogConstants.PRIORITY, Priority.getByName(arg0.getLevel().toString()));
    line.setValue(LogConstants.LOGGER, arg0.getLoggerName());
    line.setValue(LogConstants.THREAD, arg0.getThreadName());
    
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
