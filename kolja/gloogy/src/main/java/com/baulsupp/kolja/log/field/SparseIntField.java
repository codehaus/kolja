package com.baulsupp.kolja.log.field;

import org.apache.commons.collections.primitives.IntList;

import com.baulsupp.kolja.log.util.IntRange;

public interface SparseIntField {
  boolean isKnown(IntRange range);

  void add(MemoryIntField newField);

  IntRange[] listUnknownRanges(IntRange range);

  IntRange getTotalRange();

  IntList get(IntRange region);
}
