package com.baulsupp.curses.list;

import java.awt.Color;

import jcurses.system.CharColor;

import org.apache.log4j.Logger;

import com.baulsupp.kolja.util.colours.Colour;
import com.baulsupp.kolja.util.colours.ColourPair;

public class ColorList {
  public static final Logger logger = Logger.getLogger(ColorList.class);
  
  public static final CharColor blackOnWhite = new CharColor(CharColor.WHITE, CharColor.BLACK);

  public static final CharColor blueOnWhite = new CharColor(CharColor.WHITE, CharColor.BLUE);

  public static final CharColor redOnWhite = new CharColor(CharColor.WHITE, CharColor.RED, CharColor.NORMAL,
      CharColor.BOLD);

  public static final CharColor yellowOnWhite = new CharColor(CharColor.WHITE, CharColor.YELLOW);

  public static final CharColor greenOnWhite = new CharColor(CharColor.WHITE, CharColor.GREEN);

  public static final CharColor cyanOnWhite = new CharColor(CharColor.WHITE, CharColor.CYAN);

  public static final CharColor magentaOnWhite = new CharColor(CharColor.WHITE, CharColor.MAGENTA);

  public static final CharColor whiteOnBlack = new CharColor(CharColor.BLACK, CharColor.WHITE);

  public static final CharColor blueOnBlack = new CharColor(CharColor.BLACK, CharColor.BLUE);

  public static final CharColor redOnBlack = new CharColor(CharColor.BLACK, CharColor.RED, CharColor.NORMAL,
      CharColor.BOLD);

  public static final CharColor yellowOnBlack = new CharColor(CharColor.BLACK, CharColor.YELLOW);

  public static final CharColor greenOnBlack = new CharColor(CharColor.BLACK, CharColor.GREEN);

  public static final CharColor cyanOnBlack = new CharColor(CharColor.BLACK, CharColor.CYAN);

  public static final CharColor magentaOnBlack = new CharColor(CharColor.BLACK, CharColor.MAGENTA);

  public static final CharColor lookup(ColourPair colorPair) {
    if (colorPair == null) {
      return ColorList.whiteOnBlack;
    }
    
    short foreground = getColor(colorPair.getForeground(), CharColor.WHITE);
    short background = getColor(colorPair.getBackground(), CharColor.BLACK);
    
    CharColor c = new CharColor(background, foreground);
    return c;
  }

  private static short getColor(Colour foreground, short defaultColor) {
    if (foreground.equals(Color.BLACK)) {
      return CharColor.BLACK;
    } else if (foreground.equals(Color.BLUE)) {
      return CharColor.BLUE;
    } else if (foreground.equals(Color.CYAN)) {
      return CharColor.CYAN;
    } else if (foreground.equals(Color.GREEN)) {
      return CharColor.GREEN;
    } else if (foreground.equals(Color.MAGENTA)) {
      return CharColor.MAGENTA;
    } else if (foreground.equals(Color.RED)) {
      return CharColor.RED;
    } else if (foreground.equals(Color.WHITE)) {
      return CharColor.WHITE;
    } else if (foreground.equals(Color.YELLOW)) {
      return CharColor.YELLOW;
    } else 
    
    return defaultColor;
  }
}
