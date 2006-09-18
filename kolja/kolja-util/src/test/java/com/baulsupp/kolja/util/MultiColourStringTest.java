package com.baulsupp.kolja.util;

import java.util.List;

import com.baulsupp.kolja.util.colours.ColourPair;
import com.baulsupp.kolja.util.colours.ColouredString;
import com.baulsupp.kolja.util.colours.MultiColourString;

import junit.framework.TestCase;

public class MultiColourStringTest extends TestCase {
  public void testNoColour() {
    MultiColourString string = new MultiColourString("abc");
    
    assertEquals(3, string.length());
    assertEquals("abc", string.toString());
    
    List<ColouredString> strings = string.getColouredStrings();
    
    assertEquals(1, strings.size());
    assertEquals(new ColouredString(null, "abc"), strings.get(0));
  }
  
  public void testColour() {
    MultiColourString string = new MultiColourString(ColourPair.RED_ON_BLACK, "abc");
    
    assertEquals(3, string.length());
    assertEquals("abc", string.toString());
    
    List<ColouredString> strings = string.getColouredStrings();
    
    assertEquals(1, strings.size());
    assertEquals(new ColouredString(ColourPair.RED_ON_BLACK, "abc"), strings.get(0));
  }
  
  public void testColouring() {
    MultiColourString string = new MultiColourString("abc");
    
    string.setColour(ColourPair.RED_ON_BLACK);
    
    assertEquals(3, string.length());
    assertEquals("abc", string.toString());
    
    List<ColouredString> strings = string.getColouredStrings();
    
    assertEquals(1, strings.size());
    assertEquals(new ColouredString(ColourPair.RED_ON_BLACK, "abc"), strings.get(0));
    
    string.setColour(ColourPair.WHITE_ON_BLACK, 1, 2);
    
    assertEquals(3, string.length());
    assertEquals("abc", string.toString());
    
    strings = string.getColouredStrings();
    
    assertEquals(3, strings.size());
    assertEquals(new ColouredString(ColourPair.RED_ON_BLACK, "a"), strings.get(0));
    assertEquals(new ColouredString(ColourPair.WHITE_ON_BLACK, "b"), strings.get(1));
    assertEquals(new ColouredString(ColourPair.RED_ON_BLACK, "c"), strings.get(2));
  }
  
  public void testColouringOverlap() {
    MultiColourString string = new MultiColourString("0123456789");

    string.setColour(ColourPair.BLUE_ON_BLACK, 2, 6);
    string.setColour(ColourPair.RED_ON_BLACK, 4, 8);
    
    List<ColouredString> strings = string.getColouredStrings();
    
    assertEquals(5, strings.size());
    assertEquals(new ColouredString(null, "01"), strings.get(0));
    assertEquals(new ColouredString(ColourPair.BLUE_ON_BLACK, "23"), strings.get(1));
    assertEquals(new ColouredString(ColourPair.RED_ON_BLACK, "45"), strings.get(2));
    assertEquals(new ColouredString(ColourPair.RED_ON_BLACK, "67"), strings.get(3));
    assertEquals(new ColouredString(null, "89"), strings.get(4));
  }
  
  public void testHardWrap() {
    MultiColourString mcs = new MultiColourString();
    
    mcs.append(new ColouredString(ColourPair.BLUE_ON_BLACK, "0123456789"));
    mcs.append(new ColouredString(ColourPair.RED_ON_BLACK, "01234567\n89"));
    
    List<MultiColourString> strings = mcs.hardWrap(5);
    assertEquals(5, strings.size());
    assertEquals(5, strings.get(0).length());
    assertEquals(5, strings.get(1).length());
    assertEquals(5, strings.get(2).length());
    assertEquals(3, strings.get(3).length());
    assertEquals(2, strings.get(4).length());
    assertEquals(new ColouredString(ColourPair.BLUE_ON_BLACK, "01234"), strings.get(0).getColouredStrings().get(0));
    assertEquals(new ColouredString(ColourPair.RED_ON_BLACK, "567"), strings.get(3).getColouredStrings().get(0));
  }
  
  public void testSoftWrap() {
    MultiColourString mcs = new MultiColourString();
    
    mcs.append(new ColouredString(ColourPair.BLUE_ON_BLACK, "0123456789"));
    mcs.append(new ColouredString(ColourPair.RED_ON_BLACK, "01234567\n89"));
    
    List<MultiColourString> strings = mcs.softWrap();
    assertEquals(2, strings.size());
    assertEquals(18, strings.get(0).length());
    assertEquals(2, strings.get(1).length());
    assertEquals(new ColouredString(ColourPair.RED_ON_BLACK, "89"), strings.get(1).getColouredStrings().get(0));
  }
}
