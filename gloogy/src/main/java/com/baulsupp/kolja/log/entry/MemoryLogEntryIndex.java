package com.baulsupp.kolja.log.entry;

import org.apache.commons.collections.primitives.IntList;

import com.baulsupp.kolja.log.GloogyConstants;
import com.baulsupp.kolja.log.line.matcher.EntryMatcher;
import com.baulsupp.kolja.log.line.matcher.EntryPattern;
import com.baulsupp.kolja.log.util.FastIntList;
import com.baulsupp.kolja.log.util.IntRange;

public class MemoryLogEntryIndex implements LogEntryIndex {
  private CharSequence text;

  private EntryMatcher matcher;

  public MemoryLogEntryIndex(CharSequence text, EntryPattern pattern) {
    this.text = text;
    this.matcher = pattern.matcher(text);
  }

  public IntList get(IntRange region) {
    assert (region.isValid());
    assert (region.getTo() <= text.length());

    int textLength = text.length();

    int to = region.getTo();
    int newTo = Math.min(textLength, region.getTo() + GloogyConstants.LINE_CUTOFF);

    int from = region.getFrom();

    int lookBack = Math.min(3, from);

    int effectiveFrom = from - lookBack;

    matcher.reset(text.subSequence(effectiveFrom, newTo));

    // TODO pool int lists
    IntList result = new FastIntList(100);

    boolean found = matcher.find(lookBack);

    while (found) {
      int pos = matcher.start() + effectiveFrom;

      if (pos >= to) {
        break;
      }

      result.add(pos);

      found = matcher.find();
    }

    return result;
  }
}