package com.baulsupp.kolja.log.viewer.renderer;

import java.util.List;

import com.baulsupp.kolja.util.MultiColourString;

public enum Wrap {
  LAST_COLUMN {
    List<MultiColourString> wrap(MultiColourString gridRow, int screenWidth) {
      return gridRow.hardWrap(screenWidth);
    }
  },
  NONE {
    List<MultiColourString> wrap(MultiColourString gridRow, int screenWidth) {
      return gridRow.softWrap();
    }
  };

  abstract List<MultiColourString> wrap(MultiColourString gridRow, int screenWidth);
}