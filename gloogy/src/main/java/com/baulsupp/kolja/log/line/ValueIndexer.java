/**
 * Copyright (c) 2002-2007 Yuri Schimke. All Rights Reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
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
          if (range.contains(line.getIntOffset())) {
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

  public void deregister() {
    li.removeLineListener(this);
  }
}
