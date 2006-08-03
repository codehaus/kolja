package com.baulsupp.kolja.log.line;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.primitives.ArrayIntList;

import com.baulsupp.kolja.log.field.MemoryIntField;
import com.baulsupp.kolja.log.field.MemorySparseIntField;
import com.baulsupp.kolja.log.field.SparseIntField;
import com.baulsupp.kolja.log.util.IntRange;

public class ValueIndexer implements LineListener, CompletionStatus {
  protected SparseIntField indexed = new MemorySparseIntField();

  protected LineIndex li;
  private boolean inRead = false;

  public ValueIndexer(LineIndex li) {
    this.li = li;
  }

  public void linesAvailable(IntRange region, List<Line> lines) {
    if (!inRead) {
      IntRange[] unknown = indexed.listUnknownRanges(region);
      for (IntRange range : unknown) {
        List<Line> regionLines = new ArrayList<Line>();
        for (Line line : lines) {
          if (range.contains(line.getOffset())) {
            regionLines.add(line);
          }
        }
        processLines(range, regionLines);
      }
    }
  }

  public void ensureAllKnown() {
    ensureKnown(new IntRange(0, li.length()));
  }

  public void ensureKnown(IntRange region) {
    IntRange[] unknown = indexed.listUnknownRanges(region);

    for (IntRange unknownRegion : unknown) {
      List<Line> list;
      try {
        inRead = true;
        list = li.get(unknownRegion);
      } finally {
        inRead = false;
      }

      // TODO check this is making sense
      processLines(unknownRegion, list);
    }
  }
  
  public IntRange[] listUnknown() {
    return indexed.listUnknownRanges(new IntRange(0, li.length()));
  }

  protected void processLines(IntRange range, List<Line> regionLines) {
    indexed.add(new MemoryIntField(range, new ArrayIntList(0)));
  }

  public int getLength() {
    return li.length();
  }

  public double getCompletionPercentage(IntRange range) {
    IntRange[] unknown = indexed.listUnknownRanges(range);
    
    int totalUnknown = 0;
    for (IntRange r : unknown) {
      totalUnknown += r.getLength();
    }
    
    return totalUnknown / range.getLength();
  }
}
