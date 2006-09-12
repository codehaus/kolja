package com.baulsupp.curses.list;

import static com.baulsupp.kolja.util.TextUtil.hardWrap;
import static com.baulsupp.kolja.util.TextUtil.softWrap;

import com.baulsupp.kolja.util.ColourPair;
import com.baulsupp.kolja.util.ColouredString;

public class TextRenderer<T> implements CursesListRenderer<T> {
  public static final int NO_WRAP = -1;
  
  private int wrappingWidth = -1;

  public TextRenderer() {
  }

  public DisplayRow getRow(T item) {
    String text = String.valueOf(item);
    
    String[] lines;
    if (isWrapped()) {
      lines = hardWrap(text, wrappingWidth);
    } else {
      lines = softWrap(text);
    }
    
    return new Row(lines);
  }

  private boolean isWrapped() {
    return wrappingWidth != NO_WRAP;
  }

  public void setWrapping(int width) {
    this.wrappingWidth = width;
  }

  class Row implements DisplayRow {
    private String[] lines;

    Row(String[] lines) {
      this.lines = lines;
    }

    public int getHeight() {
      return lines.length;
    }

    public void paint(TextPanel panel, int fromRow, int toRow, int xPos) {
      for (int i = 0; i < lines.length; i++) {
        int row = (i - fromRow);
        
        String text = lines[i];
        
        if (xPos > 0) {
          if (xPos >= text.length()) {
            continue;
          }
          
          text = text.substring(xPos);
        }
        
        panel.printString(0, row, new ColouredString(ColourPair.WHITE_ON_BLACK, text));
      }
    }
  }
}
