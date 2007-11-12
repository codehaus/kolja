/**
 * Copyright (c) 2002-2007 Yuri Schimke. All Rights Reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package com.baulsupp.curses.list;

import jcurses.system.CharColor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baulsupp.kolja.util.colours.Colour;
import com.baulsupp.kolja.util.colours.ColourPair;

public class ColorList {
  public static final Logger logger = LoggerFactory.getLogger(ColorList.class);

  public static final CharColor blackOnWhite = new CharColor(CharColor.WHITE, CharColor.BLACK);

  public static final CharColor blueOnWhite = new CharColor(CharColor.WHITE, CharColor.BLUE);

  public static final CharColor redOnWhite = new CharColor(CharColor.WHITE, CharColor.RED, CharColor.NORMAL, CharColor.BOLD);

  public static final CharColor yellowOnWhite = new CharColor(CharColor.WHITE, CharColor.YELLOW);

  public static final CharColor greenOnWhite = new CharColor(CharColor.WHITE, CharColor.GREEN);

  public static final CharColor cyanOnWhite = new CharColor(CharColor.WHITE, CharColor.CYAN);

  public static final CharColor magentaOnWhite = new CharColor(CharColor.WHITE, CharColor.MAGENTA);

  public static final CharColor whiteOnBlack = new CharColor(CharColor.BLACK, CharColor.WHITE);

  public static final CharColor blueOnBlack = new CharColor(CharColor.BLACK, CharColor.BLUE);

  public static final CharColor redOnBlack = new CharColor(CharColor.BLACK, CharColor.RED, CharColor.NORMAL, CharColor.BOLD);

  public static final CharColor yellowOnBlack = new CharColor(CharColor.BLACK, CharColor.YELLOW);

  public static final CharColor greenOnBlack = new CharColor(CharColor.BLACK, CharColor.GREEN);

  public static final CharColor cyanOnBlack = new CharColor(CharColor.BLACK, CharColor.CYAN);

  public static final CharColor magentaOnBlack = new CharColor(CharColor.BLACK, CharColor.MAGENTA);

  public static final CharColor lookup(ColourPair colorPair) {
    if (colorPair == null) {
      return ColorList.whiteOnBlack;
    }

    short foreground = getColour(colorPair.getForeground(), CharColor.WHITE);
    short background = getColour(colorPair.getBackground(), CharColor.BLACK);

    CharColor c = new CharColor(background, foreground);
    return c;
  }

  private static short getColour(Colour foreground, short defaultColor) {
    if (foreground.equals(Colour.BLACK)) {
      return CharColor.BLACK;
    } else if (foreground.equals(Colour.BLUE)) {
      return CharColor.BLUE;
    } else if (foreground.equals(Colour.CYAN)) {
      return CharColor.CYAN;
    } else if (foreground.equals(Colour.GREEN)) {
      return CharColor.GREEN;
    } else if (foreground.equals(Colour.MAGENTA)) {
      return CharColor.MAGENTA;
    } else if (foreground.equals(Colour.RED)) {
      return CharColor.RED;
    } else if (foreground.equals(Colour.WHITE)) {
      return CharColor.WHITE;
    } else if (foreground.equals(Colour.YELLOW)) {
      return CharColor.YELLOW;
    } else {
      return defaultColor;
    }
  }
}
