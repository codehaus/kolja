package com.baulsupp.kolja.log.viewer.request;

import java.io.Serializable;

import com.baulsupp.kolja.log.line.Line;

public class BasicFieldCopier implements FieldCopier, Serializable {
  private static final long serialVersionUID = -2225432168142267174L;

  private String field;

  public BasicFieldCopier() {
  }
  
  public BasicFieldCopier(String field) {
    this.field = field;
  }

  public String getField() {
    return field;
  }

  public void setField(String field) {
    this.field = field;
  }

  public void copy(Line line, RequestLine requestLine) {
        Object value = line.getValue(field);
        if (isInteresting(value)) {
          requestLine.setValue(field, value);
        }
  }

  private boolean isInteresting(Object value) {
    if (value instanceof String) {
      return ((String) value).length() > 0;
    } else {
      return value != null;
    }
  }
}
