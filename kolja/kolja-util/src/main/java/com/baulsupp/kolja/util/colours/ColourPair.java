package com.baulsupp.kolja.util.colours;

public enum ColourPair {
  WHITE_ON_BLACK (Colour.WHITE, Colour.BLACK),
  RED_ON_WHITE (Colour.RED, Colour.WHITE),
  GREEN_ON_BLACK (Colour.GREEN, Colour.BLACK),
  RED_ON_BLACK (Colour.RED, Colour.BLACK),
  BLUE_ON_WHITE (Colour.BLUE, Colour.WHITE),
  GREEN_ON_WHITE (Colour.GREEN, Colour.WHITE),
  BLACK_ON_WHITE (Colour.BLACK, Colour.WHITE),
  MAGENTA_ON_BLACK (Colour.MAGENTA, Colour.WHITE), 
  BLUE_ON_BLACK (Colour.BLUE, Colour.BLACK);

  private final Colour foreground;
  private final Colour background;
  
  private ColourPair(Colour foreground, Colour background) {
    this.foreground = foreground;
    this.background = background;
  }
  
  public Colour getForeground() { return foreground; }
  public Colour getBackground() { return background; }
}
