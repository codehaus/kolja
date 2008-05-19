package com.baulsupp.kolja.log.viewer.renderer;


public interface Renderer<T> {
  TextDisplayRow getRow(T viewRow);

  void setWidth(int terminalWidth);
}
