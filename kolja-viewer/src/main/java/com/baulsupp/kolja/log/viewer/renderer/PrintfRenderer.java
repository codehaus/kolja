package com.baulsupp.kolja.log.viewer.renderer;

import java.util.Arrays;
import java.util.Formatter;

import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.util.TextUtil;

public class PrintfRenderer implements Renderer<Line> {
  private int width;
  
  private Formatter formatter = new Formatter();
  
  private String format;
  
  private String[] fields;

  public PrintfRenderer(String format, String[] fields) {
    this.format = format;
    this.fields = fields;
  }

  public TextDisplayRow getRow(Line viewRow) {
    OutputRow row = new OutputRow(viewRow);
    row.appendLines(Arrays.asList(TextUtil.hardWrap(getContent(viewRow), width)));
    return row;
  }

  private String getContent(Line viewRow) {
    ((StringBuilder) formatter.out()).setLength(0);
    
    formatter.format(format, getValues(viewRow));
    
    return formatter.toString();
  }

  private Object[] getValues(Line viewRow) {
    Object[] values = new Object[fields.length];
    
    for (int i = 0; i < values.length; i++) {
      values[i] = viewRow.getValue(fields[i]);
    }
    
    return values;
  }

  public void setWidth(int terminalWidth) {
    this.width = terminalWidth;
  }

  public static PrintfRenderer parse(String optionValue) {
    String[] parts = optionValue.split(":", 2);
    
    return new PrintfRenderer(parts[0], parts[1].split(":"));
  }
}
