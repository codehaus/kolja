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
package com.baulsupp.kolja.log.entry;

import org.apache.commons.collections.primitives.IntList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baulsupp.kolja.log.field.MemoryIntField;
import com.baulsupp.kolja.log.field.MemorySparseIntField;
import com.baulsupp.kolja.log.field.SparseIntField;
import com.baulsupp.kolja.log.util.IntRange;

public class BufferedLogEntryIndex implements LogEntryIndex {
  private static final Logger log = LoggerFactory.getLogger(BufferedLogEntryIndex.class);

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
