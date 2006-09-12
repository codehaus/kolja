package com.baulsupp.kolja.log.viewer.renderer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.util.TextUtil;

public class DebugRenderer implements Renderer<Line> {
  private int width;
  
  public DebugRenderer() {
  }

  public TextDisplayRow getRow(Line viewRow) {
    OutputRow row = new OutputRow(viewRow);
    for (Entry<String, Object> e : viewRow.getValues().entrySet()) {
      String text = format(viewRow.getOffset(), e);
      String[] lines = TextUtil.hardWrap(text, width);
      row.appendLines(Arrays.asList(lines));
    }
    return row;
  }

  private String format(int i, Entry<String, Object> e) {
    return String.format("%10d %s: '%s'", i, e.getKey(), e.getValue());
  }

  public void setWidth(int terminalWidth) {
    this.width = terminalWidth;
  }

  public List<String> getFieldNames() {
    return Collections.emptyList();
  }
}
