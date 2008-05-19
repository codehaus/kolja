package com.baulsupp.kolja.log.viewer.columns;

import java.io.Serializable;

public class Column implements Serializable {
  private static final long serialVersionUID = -2502023773192323111L;

  private int width = 0;

  public Column() {
  }

  public Column(int i) {
    this.width = i;
  }

  public int getWidth() {
    return width;
  }

  public static Column fixed(int i) {
    return new Column(i);
  }
}
