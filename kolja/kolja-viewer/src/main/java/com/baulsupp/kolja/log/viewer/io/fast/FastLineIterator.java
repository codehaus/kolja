/**
 * Copyright (c) 2002-2007 Yuri Schimke. All Rights Reserved.
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package com.baulsupp.kolja.log.viewer.io.fast;

import java.util.NoSuchElementException;

import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.line.LineIterator;
import com.baulsupp.kolja.log.line.LineParser;
import com.baulsupp.kolja.log.line.matcher.EntryMatcher;
import com.baulsupp.kolja.log.line.matcher.EntryPattern;

/**
 * @author Yuri Schimke
 * 
 */
public class FastLineIterator implements LineIterator {
  private EntryMatcher matcher;
  private Line nextLine;
  private LineParser lineParser;

  int lastEnd = -1;
  private CharSequence content;
  private boolean finished;
  private int maximum = Integer.MAX_VALUE;
  private long offset;

  public FastLineIterator(EntryPattern pattern, CharSequence content, LineParser lineParser) {
    this.matcher = pattern.matcher(content);
    this.content = content;
    this.lineParser = lineParser;
  }

  public FastLineIterator(EntryPattern pattern, CharSequence content, LineParser lineParser, long offset, int maximum) {
    this.matcher = pattern.matcher(content);
    this.content = content;
    this.lineParser = lineParser;
    this.maximum = maximum;
    this.offset = offset;
  }

  public boolean hasNext() {
    if (nextLine != null) {
      return true;
    } else if (finished) {
      return false;
    }

    if (lastEnd == -1) {
      boolean foundNext = matcher.find(0);

      if (!foundNext) {
        finished = true;
        return false;
      } else {
        lastEnd = matcher.start();
      }
    }

    if (lastEnd >= maximum) {
      finished = true;
      return false;
    }

    boolean foundNext = matcher.find();

    if (foundNext) {
      int nextEnd = matcher.start();
      nextLine = parse(lastEnd, nextEnd);
      lastEnd = nextEnd;
    } else {
      nextLine = parse(lastEnd, content.length());
      finished = true;
    }

    return true;
  }

  private Line parse(int from, int to) {
    Line line = lineParser.parse(content.subSequence(from, to));
    line.setOffset(from + offset);
    return line;
  }

  public Line next() {
    if (nextLine == null && !hasNext()) {
      throw new NoSuchElementException();
    }

    Line l = nextLine;

    nextLine = null;

    return l;
  }

  public boolean hasPrevious() {
    throw new UnsupportedOperationException();
  }

  public void moveTo(int position) {
    throw new UnsupportedOperationException();
  }

  public void moveToEnd() {
    throw new UnsupportedOperationException();
  }

  public void moveToStart() {
    throw new UnsupportedOperationException();
  }

  public Line previous() {
    throw new UnsupportedOperationException();
  }

  public void remove() {
    throw new UnsupportedOperationException();
  }
}
