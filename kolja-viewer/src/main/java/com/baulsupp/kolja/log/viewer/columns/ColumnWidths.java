package com.baulsupp.kolja.log.viewer.columns;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ColumnWidths implements Serializable {
  private static final long serialVersionUID = 5232145008041172094L;

  private List<Column> columns = new ArrayList<Column>();

  private int[] widths = new int[0];

  public ColumnWidths() {
  }

  public void addColumn(Column column) {
    columns.add(column);
  }

  public void addColumn(int i, Column column) {
    columns.add(i, column);
  }

  public int get(int i) {
    if (i >= widths.length)
      return 0;
    else
      return widths[i];
  }
  
  public void setDefaultWidths() {
    int pos = 0;
    widths = new int[columns.size()];
    for (Column column : columns) {
      widths[pos++] = column.getWidth();
    }
  }

  public void setScreenWidth(int width) {
    setDefaultWidths();
    
    int used = getWidth();
    
    if (used != width) {
      widths[widths.length - 1] += width - used;
    }
  }

  public int getColumnCount() {
    return columns.size();
  }

  public static ColumnWidths fixed(int... is) {
    ColumnWidths result = new ColumnWidths();

    result.widths = is.clone();
    
    for (int i : is) {
      result.addColumn(Column.fixed(i));
    }

    return result;
  }

  public boolean isVisible(int i) {
    return get(i) > 0;
  }

  public int getWidth() {
    int total = 0;
    int columns = 0;
    
    for (int i = 0; i < widths.length; i++) {
      if (widths[i] > 0) {
        columns++;
      }
      
      total += widths[i];
    }
    
    if (columns > 0) {
      total += columns - 1;
    }
    
    return total;
  }
  
  @Override
  public String toString() {
    return Arrays.toString(widths);
  }

  public int[] getSteps() {
    int nonZero = 0;
    for (int i : widths) {
      if (i > 0) {
        nonZero++;
      }
    }
    
    int[] result = new int[nonZero];
    int offset = 0;
    int pos = 0;
    for (int i = 0; i < widths.length; i++) {
      if (widths[i] != 0) { 
        int width = widths[i] + seperatorWidth();
        
        result[pos++] = offset;
        
        offset += width;
      }
    }
    
    return result;
  }

  private int seperatorWidth() {
    return 1;
  }
  

  public static ColumnWidths parse(String widthString) {
    String[] parts = widthString.split("\\s*,\\s*");
    int[] widths = new int[parts.length];
  
    for (int i = 0; i < widths.length; i++) {
      widths[i] = Integer.parseInt(parts[i]);
    }
  
    return ColumnWidths.fixed(widths);
  }
}
