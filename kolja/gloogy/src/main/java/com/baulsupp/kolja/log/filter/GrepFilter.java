package com.baulsupp.kolja.log.filter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baulsupp.kolja.log.line.Line;

public class GrepFilter implements Filter {
  private Pattern pattern;
  
  private transient Matcher matcher;

  public GrepFilter(Pattern pattern) {
    this.pattern = pattern;
  }

  public boolean lineMatches(Line line) {
    if (matcher == null) {
      matcher = pattern.matcher(line);
    } else {
      matcher.reset(line);
    }
    
    boolean result = matcher.find();
    return result;
  }
}
