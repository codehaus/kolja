package com.baulsupp.curses.list;

import static org.junit.Assert.assertEquals;
import jcurses.system.CharColor;

import org.junit.Test;

import com.baulsupp.kolja.util.colours.ColourPair;

public class ColorListTest {
  @Test
  public void lookupReturnsCorrectColours() {
    assertCharColorEquals(ColorList.blackOnWhite, ColorList.lookup(ColourPair.BLACK_ON_WHITE));
    assertCharColorEquals(ColorList.blueOnBlack, ColorList.lookup(ColourPair.BLUE_ON_BLACK));
    assertCharColorEquals(ColorList.blueOnWhite, ColorList.lookup(ColourPair.BLUE_ON_WHITE));
    assertCharColorEquals(ColorList.cyanOnBlack, ColorList.lookup(ColourPair.CYAN_ON_BLACK));
    assertCharColorEquals(ColorList.cyanOnWhite, ColorList.lookup(ColourPair.CYAN_ON_WHITE));
    assertCharColorEquals(ColorList.greenOnBlack, ColorList.lookup(ColourPair.GREEN_ON_BLACK));
    assertCharColorEquals(ColorList.greenOnWhite, ColorList.lookup(ColourPair.GREEN_ON_WHITE));
  }

  private void assertCharColorEquals(CharColor expected, CharColor actual) {
    assertEquals(expected.getForeground(), expected.getForeground());
    assertEquals(expected.getBackground(), expected.getBackground());
  }
}
