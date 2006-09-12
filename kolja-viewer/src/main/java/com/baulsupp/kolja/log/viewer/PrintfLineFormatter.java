package com.baulsupp.kolja.log.viewer;

import java.io.Serializable;

import com.baulsupp.kolja.log.line.Line;

public class PrintfLineFormatter implements LineFormatter, Serializable {
  private static final long serialVersionUID = -1386222170365186121L;

  private String pattern;
  private String[] fields;

  public PrintfLineFormatter() {
  }

  public PrintfLineFormatter(String pattern, String... fields) {
    this.pattern = pattern;
    this.fields = fields;
  }

  public String format(Line l) {
    return String.format(pattern, l.getValues(fields));
  }

  public String getPattern() {
    return pattern;
  }

  public void setPattern(String pattern) {
    this.pattern = pattern;
  }

  public String[] getFields() {
    return fields;
  }

  public void setFields(String[] fields) {
    this.fields = fields;
  }
}
