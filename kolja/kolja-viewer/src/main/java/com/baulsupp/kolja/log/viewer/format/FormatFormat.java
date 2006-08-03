package com.baulsupp.kolja.log.viewer.format;

import java.text.Format;
import java.text.SimpleDateFormat;

public class FormatFormat implements OutputFormat {
  private static final long serialVersionUID = 6750671123052767172L;
  
  private Format format;
  
  public FormatFormat() {
  }
  
  public FormatFormat(Format format) {
    this.format = format;
  }

  public Format getFormat() {
    return format;
  }

  public void setFormat(Format format) {
    this.format = format;
  }

  public String format(Object value) {
    if (value == null) {
      return "";
    }
      
    try {
      return format.format(value);
    } catch (Exception e) {
      return "XXX";
    }
  }

  public static OutputFormat dateFormat(String string) {
    return new FormatFormat(new SimpleDateFormat(string));
  }
  
  public static OutputFormat shortTimeFormat() {
    return new FormatFormat(new SimpleDateFormat("HH:mm.ss"));
  }
}
