package com.baulsupp.kolja.log.viewer.renderer;

import java.util.Arrays;
import java.util.List;

import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.util.TextUtil;

public class PlainTextRenderer implements Renderer<Line> {
  private int width = -1;

  public TextDisplayRow getRow(Line viewRow) {
    OutputRow row = new OutputRow(viewRow);
    row.appendLines(wrap(viewRow));
    return row;
  }

  private List<String> wrap(Line viewRow) {
    if (width < 0) {
      return Arrays.asList(TextUtil.softWrap(viewRow.toString()));
    } else {
      return Arrays.asList(TextUtil.hardWrap(viewRow.toString(), width));
    }
  }

  public void setWidth(int terminalWidth) {
    this.width = terminalWidth;
  }
}
