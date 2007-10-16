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

package com.baulsupp.kolja.util.colours;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Foreground and Background colour pair.
 * 
 * @author nbk7xsp
 */
public class ColourPair implements Serializable {
  private static final long serialVersionUID = -6492938675447744798L;

  public static final ColourPair WHITE_ON_BLACK = new ColourPair(Colour.WHITE, Colour.BLACK);
  public static final ColourPair RED_ON_WHITE = new ColourPair(Colour.RED, Colour.WHITE);
  public static final ColourPair GREEN_ON_BLACK = new ColourPair(Colour.GREEN, Colour.BLACK);
  public static final ColourPair RED_ON_BLACK = new ColourPair(Colour.RED, Colour.BLACK);
  public static final ColourPair BLUE_ON_WHITE = new ColourPair(Colour.BLUE, Colour.WHITE);
  public static final ColourPair GREEN_ON_WHITE = new ColourPair(Colour.GREEN, Colour.WHITE);
  public static final ColourPair BLACK_ON_WHITE = new ColourPair(Colour.BLACK, Colour.WHITE);
  public static final ColourPair MAGENTA_ON_BLACK = new ColourPair(Colour.MAGENTA, Colour.WHITE);
  public static final ColourPair BLUE_ON_BLACK = new ColourPair(Colour.BLUE, Colour.BLACK);
  public static final ColourPair CYAN_ON_BLACK = new ColourPair(Colour.CYAN, Colour.BLACK);
  public static final ColourPair CYAN_ON_WHITE = new ColourPair(Colour.CYAN, Colour.WHITE);

  public static final List<ColourPair> HIGHLIGHT_PAIRS;
  static {
    List<ColourPair> list = Arrays.asList(WHITE_ON_BLACK, RED_ON_WHITE, GREEN_ON_BLACK, RED_ON_BLACK, BLUE_ON_WHITE,
        GREEN_ON_WHITE, BLACK_ON_WHITE, MAGENTA_ON_BLACK, BLUE_ON_BLACK);
    HIGHLIGHT_PAIRS = Collections.unmodifiableList(list);
  }

  private final Colour foreground;
  private final Colour background;

  public ColourPair(Colour foreground, Colour background) {
    this.foreground = foreground;
    this.background = background;
  }

  public Colour getForeground() {
    return foreground;
  }

  public Colour getBackground() {
    return background;
  }
}
