package com.baulsupp.kolja.log.viewer.renderer;

import java.util.List;

import com.baulsupp.kolja.util.colours.MultiColourString;

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