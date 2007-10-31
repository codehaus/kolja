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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.primitives.IntList;
import org.apache.log4j.Logger;

import com.baulsupp.kolja.log.GloogyConstants;
import com.baulsupp.kolja.log.entry.LogEntryIndex;
import com.baulsupp.kolja.log.util.IntRange;
import com.baulsupp.kolja.log.util.ReloadableCharBuffer;

public class LogEntryLineIndex implements LineIndex, Reloadable {
  @SuppressWarnings("unused")
  private static final Logger logger = Logger.getLogger(LogEntryLineIndex.class);

  protected LogEntryIndex index;

  protected CharSequence text;

  private Set<LineListener> listeners = new HashSet<LineListener>();

  public LogEntryLineIndex(LogEntryIndex index, CharSequence text) {
    this.index = index;
    this.text = text;
  }

  public List<Line> get(IntRange region) {
    int to = region.getTo();

    IntRange newRange = new IntRange(region);
    newRange.setTo(Math.min(to + GloogyConstants.LINE_CUTOFF, text.length()));

    IntList list = index.get(newRange);

    if (list == null) {
      throw new NullPointerException("unable to index range " + newRange + " length of input " + text.length());
    }

    List<Line> l = new ArrayList<Line>(list.size());

    int[] values = list.toArray(new int[list.size()]);
    int lineStart = 0;
    int lineEnd = 0;

    for (int i = 0; i < values.length; i++) {
      lineStart = values[i];

      if (lineStart >= to)
        break;

      // TODO should we extend the search area?
      lineEnd = (i == values.length - 1) ? newRange.getTo() : values[i + 1];

      // TODO check if this is safe, strip newlines
      lineEnd--;

      Line line = buildLine(lineStart, lineEnd);
      l.add(line);
    }

    fireListeners(region, l);

    return l;
  }

  public void fireListeners(IntRange region, List<Line> lines) {
    if (listeners.size() > 0) {
      for (LineListener listener : listeners) {
        listener.linesAvailable(region, lines);
      }
    }
  }

  public void addLineListener(LineListener listener) {
    listeners.add(listener);
  }

  public void removeLineListener(LineListener listener) {
    listeners.remove(listener);
  }

  // TODO parse values
  protected Line buildLine(int lineStart, int lineEnd) {
    BasicLine l = new BasicLine();

    CharSequence section = text.subSequence(lineStart, lineEnd);

    l.setContent(section);

    Map<String, Object> emptyMap = new HashMap<String, Object>();
    l.setValues(emptyMap);

    l.setOffset(lineStart);

    return l;
  }

  public int length() {
    return text.length();
  }

  public boolean reload() throws IOException {
    if (text instanceof ReloadableCharBuffer) {
      ReloadableCharBuffer reloadableBuffer = (ReloadableCharBuffer) text;
      if (reloadableBuffer.hasFileGrown()) {
        reloadableBuffer.reload();
        return true;
      }
    }
    return false;
  }
}
