package com.baulsupp.kolja.util;

import java.awt.Color;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class ColourPair implements Serializable {
  private static final long serialVersionUID = -4505666159280979611L;

  public static final Color WHITE = Color.WHITE;

  public static final Color RED = Color.RED;

  public static final Color BLACK = Color.BLACK;

  public static final Color GREEN = Color.GREEN;

  public static final Color MAGENTA = Color.MAGENTA;

  public static final Color BLUE = Color.BLUE;

  public static final ColourPair WHITE_ON_BLACK = new ColourPair(WHITE, BLACK);

  public static final ColourPair RED_ON_WHITE = new ColourPair(RED, WHITE);

  public static final ColourPair MAGENTA_ON_BLACK = new ColourPair(MAGENTA, BLACK);

  public static final ColourPair GREEN_ON_BLACK = new ColourPair(GREEN, BLACK);

  public static final ColourPair BLUE_ON_BLACK = new ColourPair(BLUE, BLACK);

  public static final ColourPair RED_ON_BLACK = new ColourPair(RED, BLACK);

  public static final ColourPair BLUE_ON_WHITE = new ColourPair(BLUE, WHITE);

  public static final ColourPair GREEN_ON_WHITE = new ColourPair(GREEN, WHITE);

  public static final ColourPair BLACK_ON_WHITE = new ColourPair(BLACK, WHITE);

  public static final ColourPair MAGENTA_ON_WHITE = new ColourPair(MAGENTA, WHITE);

  public static final List<ColourPair> ALL_PAIRS = Arrays.asList(
        ColourPair.BLUE_ON_BLACK, ColourPair.GREEN_ON_BLACK, ColourPair.MAGENTA_ON_BLACK, ColourPair.RED_ON_BLACK,
        ColourPair.RED_ON_WHITE, ColourPair.WHITE_ON_BLACK, ColourPair.BLUE_ON_WHITE, ColourPair.GREEN_ON_WHITE,
        ColourPair.MAGENTA_ON_WHITE, ColourPair.BLACK_ON_WHITE);

  private Color foreground;

  private Color background;

  public ColourPair(Color foreground, Color background) {
    this.foreground = foreground;
    this.background = background;
  }

  public Color getBackground() {
    return background;
  }

  public void setBackground(Color background) {
    this.background = background;
  }

  public Color getForeground() {
    return foreground;
  }

  public void setForeground(Color foreground) {
    this.foreground = foreground;
  }
  
  @Override
  public int hashCode() {
    return foreground.hashCode() * 7 ^ background.hashCode();
  }
  
  @Override
  public boolean equals(Object arg0) {
    if (this == arg0) {
      return true;
    }
    
    if (!(arg0 instanceof ColourPair)) {
      return false;
    }
    
    ColourPair other = (ColourPair) arg0;
    
    return colorEqual(foreground, other.foreground) && colorEqual(background, other.background);
  }

  private boolean colorEqual(Color foreground2, Color foreground3) {
    if (foreground2 == foreground3) {
      return true;
    } else if (foreground2 == null || foreground3 == null) {
      return false;
    } else {
      return foreground2.equals(foreground3);
    }
  }
  
  public static ColourPair parse(String string) {
    if (!string.matches("\\w+:\\w+")) {
      throw new IllegalArgumentException("unknown colour pair '" + string + "'");
    }
    
    String[] parts = string.split(":");
    return new ColourPair(parseColour(parts[0]), parseColour(parts[1]));
  }

  public static Color parseColour(String string) {
    if (string.equalsIgnoreCase("white")) {
      return WHITE;
    } else if (string.equalsIgnoreCase("red")) {
      return RED;
    } else if (string.equalsIgnoreCase("black")) {
      return BLACK;
    } else if (string.equalsIgnoreCase("green")) {
      return GREEN;
    } else if (string.equalsIgnoreCase("magenta")) {
      return MAGENTA;
    } else if (string.equalsIgnoreCase("blue")) {
      return BLUE;
    } else {
      return null;
    }
  }
}
