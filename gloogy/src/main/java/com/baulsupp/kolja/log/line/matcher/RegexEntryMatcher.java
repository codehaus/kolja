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

import java.util.regex.Matcher;

/**
 * @author Yuri Schimke
 * 
 */
public class RegexEntryMatcher implements EntryMatcher {

  private Matcher matcher;

  public RegexEntryMatcher(Matcher matcher) {
    this.matcher = matcher;
  }

  public boolean find(int from) {
    return matcher.find(from);
  }

  public boolean find() {
    int from = matcher.end() + 1;

    boolean find = matcher.find();

    if (!find) {
      find = matcher.find(from);
    }

    return find;
  }

  public void reset(CharSequence subSequence) {
    matcher.reset(subSequence);
  }

  public int start() {
    return matcher.start();
  }

  public Matcher getMatcher() {
    return matcher;
  }

}
