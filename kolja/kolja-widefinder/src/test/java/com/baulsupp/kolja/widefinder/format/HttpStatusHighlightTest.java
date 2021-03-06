package com.baulsupp.kolja.widefinder.format;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.baulsupp.kolja.log.line.BasicLine;
import com.baulsupp.kolja.util.colours.ColourPair;

public class HttpStatusHighlightTest {
  private HttpStatusHighlight h;
  private BasicLine line;

  @Before
  public void setup() {
    h = new HttpStatusHighlight();

    line = new BasicLine();
  }

  @Test
  public void testNullStatus() {
    assertNull(h.getHighlights(line));
  }

  @Test
  public void test200() {
    line.setValue("status", new HttpStatus("200"));
    assertNull(h.getHighlights(line));
  }

  @Test
  public void test500() {
    line.setValue("status", new HttpStatus("500"));
    assertEquals(ColourPair.RED_ON_BLACK, h.getHighlights(line).getRow());
  }

  @Test
  public void test300() {
    line.setValue("status", new HttpStatus("300"));
    assertEquals(ColourPair.CYAN_ON_BLACK, h.getHighlights(line).getColumnHighlight("status"));
  }
}
