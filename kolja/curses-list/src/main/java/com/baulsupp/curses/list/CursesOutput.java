package com.baulsupp.curses.list;

import jcurses.system.CharColor;
import jcurses.system.Toolkit;

public class CursesOutput {
  private int xOffset;
  private int yOffset;
  
  public void printString(String text, int x, int y, CharColor colors) {
    Toolkit.printString(text, x + xOffset, y + yOffset, colors);
  }
}
