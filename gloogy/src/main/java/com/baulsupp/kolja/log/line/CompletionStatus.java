package com.baulsupp.kolja.log.line;

import com.baulsupp.kolja.log.util.IntRange;

public interface CompletionStatus {
  public int getLength();
  public double getCompletionPercentage(IntRange range);
}
