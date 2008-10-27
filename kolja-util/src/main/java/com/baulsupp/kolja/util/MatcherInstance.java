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
package com.baulsupp.kolja.util;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Yuri Schimke
 */
public class MatcherInstance implements Serializable {
  private static final long serialVersionUID = -6763359079166545233L;

  private Pattern pattern;

  private transient Matcher matcher;

  public MatcherInstance(Pattern pattern) {
    this.pattern = pattern;
  }

  public static MatcherInstance compile(String string) {
    return new MatcherInstance(Pattern.compile(string));
  }

  public synchronized boolean matches(String url) {
    Matcher matcher = getMatcher();

    matcher.reset(url);

    return matcher.matches();
  }

  private synchronized Matcher getMatcher() {
    if (matcher == null) {
      matcher = pattern.matcher("");
    }

    return matcher;
  }

  public synchronized String match(String url) {
    Matcher matcher = getMatcher();

    matcher.reset(url);

    if (!matcher.matches()) {
      return null;
    }

    return matcher.group(1);
  }

}
