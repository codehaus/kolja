package com.baulsupp.curses.list;

import jcurses.system.CharColor;
import jcurses.system.Toolkit;

import com.baulsupp.kolja.util.ColouredString;

public class TextPanel {
  private int xOffset = 0;
  private int yOffset = 0;
  private int width = 0;
  private int height = 0;
  
  public TextPanel(int xOffset, int yOffset, int width, int height) {
    if (height < 0) {
      throw new IllegalArgumentException("height < 0");
    }
    
    if (width < 0) {
      throw new IllegalArgumentException("width < 0");
    }
    
    if (xOffset < 0) {
      throw new IllegalArgumentException("xOffset < 0");
    }
    
    if (yOffset < 0) {
      throw new IllegalArgumentException("yOffset < 0");
    }
    
    this.xOffset = xOffset;
    this.yOffset = yOffset;
    this.width = width;
    this.height = height;
  }
  
  public int getHeight() {
    return height;
  }

  public int getWidth() {
    return width;
  }

  public int getXOffset() {
    return xOffset;
  }

  public int getYOffset() {
    return yOffset;
  }

  public TextPanel row(int yOffset, int height) {
    return new TextPanel(this.xOffset, this.yOffset + yOffset, width, this.height + height);
  }

  public void printString(int xOffset, int yOffset, ColouredString string) {
    String content = string.toString();    

    int left = width - xOffset;
    if (content.length() > left) {
      content = content.substring(0, left);
    }
    
    CharColor colours = ColorList.lookup(string.getColorPair());
    Toolkit.printString(content, this.xOffset + xOffset, this.yOffset + yOffset, colours);
  }
}
