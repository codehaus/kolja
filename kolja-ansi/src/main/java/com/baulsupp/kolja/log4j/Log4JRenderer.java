package com.baulsupp.kolja.log4j;

import jline.Terminal;

import com.baulsupp.kolja.log.LogConstants;
import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.viewer.format.CompressedPackageFormat;
import com.baulsupp.kolja.log.viewer.format.FormatFormat;
import com.baulsupp.kolja.log.viewer.highlight.PriorityHighlight;
import com.baulsupp.kolja.log.viewer.highlight.RotatingHighlight;
import com.baulsupp.kolja.log.viewer.renderer.FieldRenderer;
import com.baulsupp.kolja.log.viewer.renderer.Renderer;

public class Log4JRenderer {
  public static Renderer<Line> create() {
    FieldRenderer fr = new FieldRenderer();
    
    fr.appendColumn(LogConstants.DATE, 8, FormatFormat.shortTimeFormat());
    fr.appendColumn(LogConstants.LOGGER, 20, new CompressedPackageFormat());
    fr.appendColumn(LogConstants.THREAD, 12);
    fr.appendColumn(LogConstants.PRIORITY, 5);
    fr.appendColumn(LogConstants.CONTENT, 20);
    
    fr.showAdditional(LogConstants.EXCEPTION);
    
    fr.addHighlight(new PriorityHighlight());
    fr.addHighlight(new RotatingHighlight(LogConstants.THREAD));
    
    fr.setWidth(Terminal.getTerminal().getTerminalWidth());
    
    System.out.println(fr.getWidths());
    
    return fr;
  }
}
