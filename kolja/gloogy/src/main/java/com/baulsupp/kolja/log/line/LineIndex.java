package com.baulsupp.kolja.log.line;

import java.util.List;

import com.baulsupp.kolja.log.util.IntRange;

public interface LineIndex {
  List<Line> get(IntRange region);

  // TODO where should this be?
  int length();

  void addLineListener(LineListener requestIndex);
}
