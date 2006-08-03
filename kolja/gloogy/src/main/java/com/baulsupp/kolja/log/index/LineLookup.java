package com.baulsupp.kolja.log.index;

import com.baulsupp.kolja.log.util.IntRange;

public interface LineLookup {
  void index(IntRange region);

  void addIndex(String key);

  void addDateIndex(String key);

  void addSortedIndex(String key);
}
