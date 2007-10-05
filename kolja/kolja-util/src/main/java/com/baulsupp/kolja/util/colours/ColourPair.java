package com.baulsupp.kolja.util.colours;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Foreground and Background colour pair.
 * 
 * @author nbk7xsp
 */
public class ColourPair {
  public static final ColourPair WHITE_ON_BLACK = new ColourPair(Colour.WHITE, Colour.BLACK);
  public static final ColourPair RED_ON_WHITE = new ColourPair(Colour.RED, Colour.WHITE);
  public static final ColourPair GREEN_ON_BLACK = new ColourPair(Colour.GREEN, Colour.BLACK);
  public static final ColourPair RED_ON_BLACK = new ColourPair(Colour.RED, Colour.BLACK);
  public static final ColourPair BLUE_ON_WHITE = new ColourPair(Colour.BLUE, Colour.WHITE);
  public static final ColourPair GREEN_ON_WHITE = new ColourPair(Colour.GREEN, Colour.WHITE);
  public static final ColourPair BLACK_ON_WHITE = new ColourPair(Colour.BLACK, Colour.WHITE);
  public static final ColourPair MAGENTA_ON_BLACK = new ColourPair(Colour.MAGENTA, Colour.WHITE);
  public static final ColourPair BLUE_ON_BLACK = new ColourPair(Colour.BLUE, Colour.BLACK);

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
