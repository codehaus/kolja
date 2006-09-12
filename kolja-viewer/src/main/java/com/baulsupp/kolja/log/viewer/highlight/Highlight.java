package com.baulsupp.kolja.log.viewer.highlight;


public interface Highlight<T> {
  HighlightResult getHighlights(T viewRow);
}
