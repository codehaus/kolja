package com.baulsupp.kolja.log.viewer.format;


public class ToStringFormat implements OutputFormat {
  private static final long serialVersionUID = 6750671123052767172L;

  public String format(Object value) {
    if (value == null) {
      return "";
    }
      
    return value.toString();
  }
}
