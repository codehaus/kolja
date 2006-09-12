package com.baulsupp.kolja.log.viewer.highlight;

import junit.framework.TestCase;

import com.baulsupp.kolja.log.LogConstants;
import com.baulsupp.kolja.log.line.BasicLine;
import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.util.ColourPair;

public class FilenameHighlightTest extends TestCase {
  public void testGetHighlights() {
    FilenameHighlight h = new FilenameHighlight(ColourPair.BLUE_ON_BLACK, ColourPair.GREEN_ON_BLACK);
    
    assertEquals(ColourPair.BLUE_ON_BLACK, h.getHighlights(createRow("blue-black")).getColumnHighlight(LogConstants.FILE_NAME));
    assertEquals(ColourPair.GREEN_ON_BLACK, h.getHighlights(createRow("greeb-black")).getColumnHighlight(LogConstants.FILE_NAME));
    assertEquals(ColourPair.BLUE_ON_BLACK, h.getHighlights(createRow("blue-black-2")).getColumnHighlight(LogConstants.FILE_NAME));
    assertEquals(ColourPair.BLUE_ON_BLACK, h.getHighlights(createRow("blue-black")).getColumnHighlight(LogConstants.FILE_NAME));
    assertEquals(ColourPair.GREEN_ON_BLACK, h.getHighlights(createRow("greeb-black")).getColumnHighlight(LogConstants.FILE_NAME));
    assertEquals(ColourPair.BLUE_ON_BLACK, h.getHighlights(createRow("blue-black-2")).getColumnHighlight(LogConstants.FILE_NAME));
  }

  private Line createRow(String string) {
    BasicLine basicLine = new BasicLine("x");
    basicLine.setValue(LogConstants.FILE_NAME, string);
    return basicLine;
  }
}
