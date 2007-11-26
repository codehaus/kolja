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
package com.baulsupp.kolja.util;

import java.beans.PropertyEditorSupport;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternPropertyEditor extends PropertyEditorSupport {
  private static final Pattern withMode = Pattern.compile("(\\d+):(.*)");

  public void setAsText(String text) throws IllegalArgumentException {
    Pattern p;

    p = parsePattern(text);

    setValue(p);
  }

  public static Pattern parsePattern(String text) {
    Pattern p;
    Matcher m = withMode.matcher(text);
    if (m.matches()) {
      int mode = Integer.parseInt(m.group(1));
      String pattern = m.group(2);

      p = Pattern.compile(pattern, mode);
    } else {
      p = Pattern.compile(text);
    }
    return p;
  }
}