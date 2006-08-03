package com.baulsupp.kolja.log.field;

import org.apache.commons.collections.primitives.IntList;

import com.baulsupp.kolja.log.util.IntRange;

public interface IntField {
  IntRange getRange();

  IntList get(IntRange region);
}