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

import com.baulsupp.kolja.log.LogConstants;
import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.util.colours.ColourPair;

// TODO select highlight scope, line, field etc
public class RegexHighlight implements Highlight<Line> {
  private static final long serialVersionUID = 8950666564334952873L;

  private String contentField = LogConstants.CONTENT;
  private Pattern pattern;
  private ColourPair colours;

  private transient Matcher matcher;

  public RegexHighlight() {
  }

  public RegexHighlight(String contentField, Pattern pattern, ColourPair colours) {
    this.contentField = contentField;
    this.pattern = pattern;
    this.colours = colours;
  }

  public String getContentField() {
    return contentField;
  }

  public void setContentField(String contentField) {
    this.contentField = contentField;
  }

  public Pattern getPattern() {
    return pattern;
  }

  public void setPattern(Pattern pattern) {
    this.pattern = pattern;
  }

  public ColourPair getColours() {
    return colours;
  }

  public void setColours(ColourPair colours) {
    this.colours = colours;
  }

  private void reset(String l) {
    if (matcher == null) {
      matcher = pattern.matcher(l);
    } else {
      matcher.reset(l);
    }
  }

  public HighlightResult getHighlights(Line l) {
    String m = (String) l.getValue(contentField);

    if (m != null) {
      reset(m);

      if (matcher.find()) {
        return HighlightResult.row(colours);
      }
    }

    return null;
  }
}
