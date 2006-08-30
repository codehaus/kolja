package com.baulsupp.kolja.util;

import java.util.Arrays;
import java.util.List;

public class ColourRotator {
  public List<ColourPair> colours = null;

  private int pos = 0;
  
  public ColourRotator() {
    this.colours = ColourPair.ALL_PAIRS;
  }
  
  public ColourRotator(ColourPair... colours) {
    this.colours = Arrays.asList(colours);
  }

  public ColourRotator(List<ColourPair> colours) {
    this.colours = colours;
  }

  public ColourPair next() {
    ColourPair p = colours.get(pos);
    
    pos = (pos + 1) % colours.size();
    
    return p;
  }
}
