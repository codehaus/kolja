package com.baulsupp.kolja.log.line;

import java.util.List;

import com.baulsupp.kolja.log.util.IntRange;

public interface LineListener {
  void linesAvailable(IntRange region, List<Line> lines);
}
