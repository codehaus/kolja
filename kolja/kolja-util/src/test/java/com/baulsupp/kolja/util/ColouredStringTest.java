package com.baulsupp.kolja.util;

import junit.framework.TestCase;

import com.baulsupp.kolja.util.colours.Colour;
import com.baulsupp.kolja.util.colours.ColourPair;
import com.baulsupp.kolja.util.colours.ColouredString;

public class ColouredStringTest extends TestCase {
  public void testUncoloured() {
    ColouredString s = new ColouredString(null, "abc");
    
    assertNull(s.getBackgroundColor());
    assertNull(s.getForegroundColor());
    assertEquals("abc", s.toString());
  }
  
  public void testColoured() {
    ColouredString s = new ColouredString(ColourPair.RED_ON_WHITE, "abc");
    
    assertEquals(Colour.WHITE, s.getBackgroundColor());
    assertEquals(Colour.RED, s.getForegroundColor());
    assertEquals("abc", s.toString());
  }
  
  public void testEquals() {
    ColouredString s1 = new ColouredString(null, "abc");
    ColouredString s2 = new ColouredString(ColourPair.RED_ON_WHITE, "abc");
    ColouredString s1a = new ColouredString(null, "abc");
    ColouredString s2a = new ColouredString(ColourPair.RED_ON_WHITE, "abc");
    
    assertEquals(s1, s1);
    assertEquals(s2, s2);
    assertFalse(s1.equals(s2));
    assertEquals(s1, s1a);
    assertEquals(s2, s2a);
  }
  
  public void testCharSequence() {
    ColouredString s = new ColouredString(ColourPair.RED_ON_WHITE, "abc");
    
    assertEquals(3, s.length());
    assertEquals('a', s.charAt(0));
  }
}
