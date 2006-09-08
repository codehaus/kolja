package com.baulsupp.kolja.log.viewer.format;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LastPackageFormat implements OutputFormat {
  private static final long serialVersionUID = 6750671123052767172L;
  
  private Pattern pattern;
  
  private transient Matcher matcher;
  
  public LastPackageFormat(String seperator) {
    this.pattern = Pattern.compile(seperator);
  }

  public String format(Object value) {
    if (value == null) {
      return "";
    }
    
    if (matcher == null) {
      this.matcher = pattern.matcher("");
    }
      
    int from = 0;
    
    String string = value.toString();
    
    matcher.reset(string);
    
    while (matcher.find()) {
      // loop until end
      from = matcher.regionEnd() + 1; 
    }

    return string.substring(from);
  }
}
