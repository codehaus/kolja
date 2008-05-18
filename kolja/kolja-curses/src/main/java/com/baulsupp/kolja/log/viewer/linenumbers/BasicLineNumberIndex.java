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
package com.baulsupp.kolja.log.viewer.linenumbers;

import org.apache.commons.collections.primitives.IntList;

import com.baulsupp.curses.list.LineNumberIndex;
import com.baulsupp.kolja.log.entry.BufferedLogEntryIndex;
import com.baulsupp.kolja.log.entry.LogEntryIndex;
import com.baulsupp.kolja.log.entry.MemoryLogEntryIndex;
import com.baulsupp.kolja.log.line.matcher.EntryPattern;
import com.baulsupp.kolja.log.line.matcher.NewLineEntryPattern;
import com.baulsupp.kolja.log.util.IntRange;

public class BasicLineNumberIndex implements LineNumberIndex {
  private LogEntryIndex lineIndex;

  private int linesRead = 0;

  private int positionRead = 0;

  private CharSequence buffer;

  private BasicLineNumberIndex(BufferedLogEntryIndex lineIndex, CharSequence buffer) {
    this.lineIndex = lineIndex;
    this.buffer = buffer;
  }

  public int lineNumber(int position) {
    IntList l = readUpTo(position + 1);

    return l.size();
  }

  private IntList readUpTo(int position) {
    position = Math.min(position, buffer.length());
    IntList result = lineIndex.get(new IntRange(0, position));

    positionRead = Math.max(positionRead, position);
    linesRead = result.size();

    return result;
  }

  public int numberOfLines() {
    return readUpTo(buffer.length()).size();
  }

  public int offset(int lineNumber) {
    if (lineNumber == 0) {
      return 0;
    }

    IntList l = null;
    while (l == null || linesRead <= lineNumber) {
      int estimate = estimateNeeded(lineNumber);
      l = readUpTo(estimate);

      if (linesRead <= lineNumber && positionRead >= buffer.length()) {
        return -1;
      }
    }

    return l.get(lineNumber);
  }

  private int estimateNeeded(int lineNumber) {
    if (lineNumber <= linesRead) {
      return positionRead;
    } else if (linesRead == 0) {
      return 100 * (lineNumber + 1);
    } else {
      return (lineNumber + 1) * (positionRead / linesRead + 1) + 100;
    }
  }

  public static LineNumberIndex create(CharSequence buffer) {
    EntryPattern p = new NewLineEntryPattern();
    MemoryLogEntryIndex rawIndex = new MemoryLogEntryIndex(buffer, p);
    BufferedLogEntryIndex entryIndex = new BufferedLogEntryIndex(rawIndex);

    return new BasicLineNumberIndex(entryIndex, buffer);
  }
}
