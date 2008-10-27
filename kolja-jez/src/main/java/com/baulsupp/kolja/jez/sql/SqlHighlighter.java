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
package com.baulsupp.kolja.jez.sql;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baulsupp.kolja.util.colours.ColourPair;
import com.baulsupp.kolja.util.colours.MultiColourString;

/**
 * @author Yuri Schimke
 * 
 */
public class SqlHighlighter {
  private Pattern keywords = Pattern.compile("(?i)(?:select|from|delete|update|where|having|count|group|by)");

  public MultiColourString highlight(String string) {
    MultiColourString result = new MultiColourString(string);

    Matcher m = keywords.matcher(string);
    while (m.find()) {
      result.setColour(ColourPair.BLUE_ON_WHITE, m.start(), m.end());
    }

    return result;
  }
}
