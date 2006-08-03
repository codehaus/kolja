package com.baulsupp.kolja.log.viewer.renderer;

import junit.framework.TestCase;

import com.baulsupp.kolja.util.ColourPair;
import com.baulsupp.kolja.util.ColouredString;

public class ColouredStringTest extends TestCase {
  public void testColourChange() {
    ColouredString cs = new ColouredString("a");

    assertEquals(null, cs.getColorPair());
    
    cs = cs.changeColour(ColourPair.BLUE_ON_BLACK);
    
    assertEquals(ColourPair.BLUE_ON_BLACK, cs.getColorPair());
  }
  
  public void testNoColour() {
    ColouredString cs = new ColouredString("a");

    assertEquals(null, cs.getColorPair());
    assertEquals(null, cs.getForegroundColor());
    assertEquals(null, cs.getBackgroundColor());
  }
  
  public void testColour() {
    ColouredString cs = new ColouredString(ColourPair.BLUE_ON_BLACK, "a");

    assertEquals(ColourPair.BLUE_ON_BLACK, cs.getColorPair());
    assertEquals(ColourPair.BLUE, cs.getForegroundColor());
    assertEquals(ColourPair.BLACK, cs.getBackgroundColor());
  }
  
  public void testStringOps() {
    ColouredString cs = new ColouredString("abc");

    assertEquals("bc", cs.substring(1));
    assertEquals("b", cs.substring(1, 2));
    assertEquals("abc", cs.toString());
    assertEquals(3, cs.length());
    assertEquals('a', cs.charAt(0));
    assertEquals("b", cs.subSequence(1, 2));
  }
}
