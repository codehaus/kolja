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
package com.baulsupp.kolja.log.viewer.event;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baulsupp.kolja.log.LogConstants;
import com.baulsupp.kolja.log.line.Line;

public class PatternEventMatcher implements EventMatcher {
  private static final long serialVersionUID = 8950666564334952873L;

  private String contentField = LogConstants.CONTENT;
  private Pattern pattern;
  private String message;

  public PatternEventMatcher() {
  }

  public PatternEventMatcher(String contentField, Pattern pattern, String message) {
    this.contentField = contentField;
    this.pattern = pattern;
    this.message = message;
  }

  public String getContentField() {
    return contentField;
  }

  public void setContentField(String contentField) {
    this.contentField = contentField;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Pattern getPattern() {
    return pattern;
  }

  public void setPattern(Pattern pattern) {
    this.pattern = pattern;
  }

  public Event match(Line l) {
    String m = (String) l.getValue(contentField);

    if (m != null) {
      Matcher matcher = pattern.matcher(m);

      if (matcher.find()) {
        String text = format(matcher);
        return new Event(l, text);
      }
    }

    return null;
  }

  private String format(Matcher matcher) {
    String[] values = extractGroups(matcher);

    return String.format(message, (Object[]) values);
  }

  private String[] extractGroups(Matcher matcher) {
    String[] values = new String[matcher.groupCount()];

    for (int i = 0; i < values.length; i++) {
      values[i] = matcher.group(i + 1);
    }

    return values;
  }
}
