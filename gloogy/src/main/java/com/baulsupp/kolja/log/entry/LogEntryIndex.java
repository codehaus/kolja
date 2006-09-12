package com.baulsupp.kolja.log.entry;

import org.apache.commons.collections.primitives.IntList;

import com.baulsupp.kolja.log.util.IntRange;

public interface LogEntryIndex {
  IntList get(IntRange region);
}
