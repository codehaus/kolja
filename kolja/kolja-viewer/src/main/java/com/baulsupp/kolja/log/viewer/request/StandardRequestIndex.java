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
package com.baulsupp.kolja.log.viewer.request;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.primitives.IntList;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import com.baulsupp.kolja.log.LogConstants;
import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.line.LineIndex;
import com.baulsupp.kolja.log.viewer.LineFormatter;

public class StandardRequestIndex extends RequestIndex {
  private String beginPattern;

  private String endPattern;

  private boolean offsetIsEnd = false;

  private String dateField = null;

  private LineFormatter statusFormatter;

  private Set<Object> unknownLines = new HashSet<Object>();

  public StandardRequestIndex(String requestField, String messageField, List<String> fields, LineIndex li) {
    super(requestField, messageField, fields, li);
  }

  @Override
  protected RequestLine createInitialLine(Line line, IntList values) {
    Object identifier = line.getValue(requestField);

    RequestLine requestLine = new RequestLine(identifier, messageField);

    for (String field : fields) {
      requestLine.setValue(field, line.getValue(field));
    }

    requestLine.setValue(messageField, "incomplete request");

    requestsById.put(line.getValue(requestField), requestLine);

    if (offsetIsEnd) {
      unknownLines.add(line.getValue(requestField));
    } else {
      requestLine.setOffset(line.getOffset());
      requests.put(line.getOffset(), requestLine);
      values.add(line.getOffset());
    }

    return requestLine;
  }

  @Override
  protected void updateLine(Line line, RequestLine requestLine, IntList values) {
    requestLine.addLine(line);

    updateMatchers(line, requestLine);

    String message = (String) line.getValue(messageField);

    if (message != null) {
      boolean complete = requestLine.isComplete();

      if (message.contains(beginPattern)) {
        requestLine.setStartFound(true);

        if (dateField != null) {
          DateTime d = (DateTime) line.getValue(dateField);
          requestLine.setValue(dateField, d);
        }
      }

      if (message.contains(endPattern)) {
        requestLine.setEndFound(true);

        requestLine.setOffset(line.getOffset());

        DateTime d = (DateTime) line.getValue(dateField);
        requestLine.setValue(dateField + "-end", d);

        if (offsetIsEnd) {
          requests.put(line.getOffset(), requestLine);
          values.add(line.getOffset());
        }
      }

      if (!complete && requestLine.isComplete()) {
        DateTime start = (DateTime) requestLine.getValue(dateField);
        DateTime end = (DateTime) requestLine.getValue(dateField + "-end");

        Duration duration = new Duration(end, start);

        requestLine.setValue(LogConstants.DURATION, duration);
        requestLine.setValue(messageField, statusFormatter.format(requestLine));
      }
    }
  }

  public void setOffsetIsEnd(boolean b) {
    this.offsetIsEnd = b;
  }

  public boolean getOffsetIsEnd() {
    return offsetIsEnd;
  }

  public String getBeginPattern() {
    return beginPattern;
  }

  public void setBeginPattern(String beginPattern) {
    this.beginPattern = beginPattern;
  }

  public String getEndPattern() {
    return endPattern;
  }

  public void setEndPattern(String endPattern) {
    this.endPattern = endPattern;
  }

  public String getDateField() {
    return dateField;
  }

  public void setDateField(String dateField) {
    this.dateField = dateField;
  }

  public LineFormatter getStatusFormatter() {
    return statusFormatter;
  }

  public void setStatusFormatter(LineFormatter statusFormatter) {
    this.statusFormatter = statusFormatter;
  }

  public void setMatchers(List<FieldCopier> matchers) {
    this.matchers = matchers;
  }
}
