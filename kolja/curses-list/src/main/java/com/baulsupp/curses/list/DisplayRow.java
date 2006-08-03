package com.baulsupp.curses.list;

public interface DisplayRow {
  int getHeight();

  void paint(TextPanel panel, int fromRow, int toRow, int xPos);
}
