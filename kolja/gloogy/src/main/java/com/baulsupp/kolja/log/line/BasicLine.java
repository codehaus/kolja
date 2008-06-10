package com.baulsupp.kolja.log.line;

import java.util.HashMap;
import java.util.Map;

public class BasicLine implements Line {
  private CharSequence content;

  private Map<String, Object> values;

  private boolean failed;

  private long offset;

  public BasicLine() {
  }

  public BasicLine(String string) {
    this.content = string;
  }

  public BasicLine(int offset, String string) {
    this.offset = offset;
    this.content = string;
  }

  public int length() {
    return content.length();
  }

  public char charAt(int index) {
    return content.charAt(index);
  }

  public CharSequence subSequence(int start, int end) {
    return content.subSequence(start, end);
  }

  public String toString() {
    return content.toString();
  }

  public Object getValue(String name) {
    return values == null ? null : values.get(name);
  }

  public void setValue(String name, Object value) {
    if (values == null) {
      values = new HashMap<String, Object>();
    }

    values.put(name, value);
  }

  public void setContent(CharSequence content) {
    this.content = content;
  }

  public void setValues(Map<String, Object> values) {
    this.values = values;
  }

  public boolean isFailed() {
    return failed;
  }

  public void setFailed() {
    this.values = new HashMap<String, Object>();
    this.failed = true;
  }

  public long getOffset() {
    return offset;
  }

  public int getIntOffset() {
    if (offset > Integer.MAX_VALUE) {
      throw new IllegalStateException("too big for int " + offset);
    }

    return (int) offset;
  }

  public void setOffset(long offset) {
    this.offset = offset;
  }

  public Map<String, Object> getValues() {
    return values;
  }
}