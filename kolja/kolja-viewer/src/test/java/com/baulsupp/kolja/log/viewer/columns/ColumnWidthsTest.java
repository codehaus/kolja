package com.baulsupp.kolja.log.viewer.columns;

import junit.framework.TestCase;


public class ColumnWidthsTest extends TestCase {
  public void testFixed() {
    ColumnWidths w = ColumnWidths.fixed(30, 8, 40);

    assertEquals(3, w.getColumnCount());
    assertEquals(80, w.getWidth());
    assertEquals(30, w.get(0)); 
    assertEquals(8, w.get(1));
    assertEquals(40, w.get(2));
    
    assertTrue(w.isVisible(0));
    
    w.setScreenWidth(60);

    assertEquals(60, w.getWidth());
    assertEquals(30, w.get(0)); 
    assertEquals(8, w.get(1));
    assertEquals(20, w.get(2));
  }
  
  public void testZeroWidth() {
    ColumnWidths w = ColumnWidths.fixed(9, 0, 40);

    assertEquals(3, w.getColumnCount());
    assertEquals(50, w.getWidth());    

    assertFalse(w.isVisible(1));
    assertEquals(0, w.get(1));
    
    int[] steps = w.getSteps();
    assertEquals(2, steps.length);
    assertEquals(0, steps[0]);
    assertEquals(10, steps[1]);    
  }
}
