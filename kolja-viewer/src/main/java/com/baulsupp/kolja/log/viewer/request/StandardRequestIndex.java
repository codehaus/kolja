package com.baulsupp.kolja.log.viewer.request;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.primitives.IntList;

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
          Date d = (Date) line.getValue(dateField);
          requestLine.setValue(dateField, d);
        }
      }

      if (message.contains(endPattern)) {
        requestLine.setEndFound(true);

        requestLine.setOffset(line.getOffset());

        Date d = (Date) line.getValue(dateField);
        requestLine.setValue(dateField + "-end", d);

        if (offsetIsEnd) {
          requests.put(line.getOffset(), requestLine);
          values.add(line.getOffset());
        }
      }

      if (!complete && requestLine.isComplete()) {
        Date start = (Date) requestLine.getValue(dateField);
        Date end = (Date) requestLine.getValue(dateField + "-end");

        long duration = end.getTime() - start.getTime();

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
