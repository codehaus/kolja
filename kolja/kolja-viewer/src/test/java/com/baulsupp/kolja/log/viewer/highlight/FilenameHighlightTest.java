package com.baulsupp.kolja.log.viewer.highlight;

import com.baulsupp.kolja.log.line.BasicLine;
import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.viewer.io.MultipleLineIterator;
import com.baulsupp.kolja.util.ColourPair;

import junit.framework.TestCase;

public class FilenameHighlightTest extends TestCase {
  public void testGetHighlights() {
    FilenameHighlight h = new FilenameHighlight(ColourPair.BLUE_ON_BLACK, ColourPair.GREEN_ON_BLACK);
    
    assertEquals(ColourPair.BLUE_ON_BLACK, h.getHighlights(createRow("blue-black")).getColumnHighlight(MultipleLineIterator.FILE_NAME));
    assertEquals(ColourPair.GREEN_ON_BLACK, h.getHighlights(createRow("greeb-black")).getColumnHighlight(MultipleLineIterator.FILE_NAME));
    assertEquals(ColourPair.BLUE_ON_BLACK, h.getHighlights(createRow("blue-black-2")).getColumnHighlight(MultipleLineIterator.FILE_NAME));
    assertEquals(ColourPair.BLUE_ON_BLACK, h.getHighlights(createRow("blue-black")).getColumnHighlight(MultipleLineIterator.FILE_NAME));
    assertEquals(ColourPair.GREEN_ON_BLACK, h.getHighlights(createRow("greeb-black")).getColumnHighlight(MultipleLineIterator.FILE_NAME));
    assertEquals(ColourPair.BLUE_ON_BLACK, h.getHighlights(createRow("blue-black-2")).getColumnHighlight(MultipleLineIterator.FILE_NAME));
  }

  private Line createRow(String string) {
    BasicLine basicLine = new BasicLine("x");
    basicLine.setValue(MultipleLineIterator.FILE_NAME, string);
    return basicLine;
  }
}
