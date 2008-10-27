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
package com.baulsupp.kolja.log.line.matcher;

/**
 * @author Yuri Schimke
 * 
 */
public class NewLineEntryMatcher implements EntryMatcher {

  private CharSequence text;
  private int lastMatch = -1;
  private int from = 0;

  public NewLineEntryMatcher(CharSequence text) {
    this.text = text;
  }

  public boolean find(int from) {
    this.from = from;
    this.lastMatch = -1;

    return find();
  }

  public boolean find() {
    if (from == 0) {
      from++;
      lastMatch = 0;
      return true;
    }

    int length = text.length();
    for (int i = from; i < length; i++) {
      if (text.charAt(i - 1) == '\n') {
        lastMatch = i;
        from = i + 1;
        return true;
      }
    }

    return false;
  }

  public void reset(CharSequence text) {
    this.text = text;

    this.from = 0;
    this.lastMatch = -1;
  }

  public int start() {
    if (lastMatch == -1) {
      throw new IllegalStateException();
    }

    return lastMatch;
  }
}
