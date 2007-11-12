package com.baulsupp.less;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baulsupp.curses.list.DisplayRow;
import com.baulsupp.curses.list.TextPanel;
import com.baulsupp.kolja.log.viewer.renderer.TextDisplayRow;
import com.baulsupp.kolja.util.colours.ColouredString;
import com.baulsupp.kolja.util.colours.MultiColourString;

public class LessRow implements DisplayRow {
  public static final Logger logger = LoggerFactory.getLogger(LessRow.class);

  private TextDisplayRow row;

  public LessRow(TextDisplayRow row) {
    this.row = row;
  }

  public int getHeight() {
    return row.getHeight();
  }

  public void paint(TextPanel panel, int fromRow, int height, int xPos) {
    List<MultiColourString> lines = row.getLines();
    for (int i = 0; i < height; i++) {
      paintLine(panel, i, xPos, lines.get(i + fromRow));
    }
  }

  private void paintLine(TextPanel panel, int y, int xPos, MultiColourString string) {
    int offset = 0;
    
    // TODO stop when already enough
    for (ColouredString o : string.getColouredStrings()) {
      print(panel.row(y, 1), offset - xPos, o);

      offset += o.length();
    }
  }

  public void print(TextPanel panel, int xPos, ColouredString string) {
    String content = string.toString();

    if (xPos < 0) {
      int skip = Math.abs(xPos);
      if (skip < string.length()) {
        content = content.substring(skip);
        xPos += skip;
      } else {
        return;
      }
    }

    panel.printString(xPos, 0, new ColouredString(string.getColorPair(), content));
  }
}