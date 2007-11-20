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
package com.baulsupp.kolja.log.viewer.highlight;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.util.colours.ColourPair;

public class BasicSearchHighlight implements Highlight<Line> {
  private static final long serialVersionUID = 9159167812928693265L;

  private Pattern pattern;

  private transient Matcher matcher;

  private ColourPair colour;

  public BasicSearchHighlight(ColourPair colour) {
    this.colour = colour;
  }

  public HighlightResult getHighlights(Line viewRow) {
    HighlightResult result = null;
    if (pattern != null) {
      if (matcher == null) {
        matcher = pattern.matcher(viewRow);
      } else {
        matcher.reset(viewRow);
      }

      while (matcher.find()) {
        String match = matcher.group();

        if (result == null) {
          result = new HighlightResult();
        }
        result.highlightWord(match, colour);
      }
    }
    return result;
  }

  public void setPattern(Pattern pattern) {
    this.pattern = pattern;
    this.matcher = null;
  }

  public void clear() {
    this.matcher = null;
    this.pattern = null;
  }
}
