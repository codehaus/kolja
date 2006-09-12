package com.baulsupp.kolja.log.entry;

import org.apache.commons.collections.primitives.IntList;
import org.apache.log4j.Logger;

import com.baulsupp.kolja.log.field.MemoryIntField;
import com.baulsupp.kolja.log.field.MemorySparseIntField;
import com.baulsupp.kolja.log.field.SparseIntField;
import com.baulsupp.kolja.log.util.IntRange;

public class BufferedLogEntryIndex implements LogEntryIndex {
  private static final Logger log = Logger.getLogger(BufferedLogEntryIndex.class);

  private LogEntryIndex index;

  SparseIntField field = new MemorySparseIntField();

  public BufferedLogEntryIndex(LogEntryIndex index) {
    this.index = index;
  }

  public IntList get(IntRange region) {
    IntList result = field.get(region);

    log.debug("buf " + region);

    if (result == null) {
      IntRange[] missing = field.listUnknownRanges(region);
      for (int i = 0; i < missing.length; i++) {
        // TODO problem here!
        log.debug("missing " + missing[i]);
        IntList l = index.get(missing[i]);

        field.add(new MemoryIntField(missing[i], l));
      }

      result = field.get(region);
    }

    return result;
  }
}
