package com.baulsupp.kolja.log.filter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baulsupp.kolja.log.line.Line;

public class GrepFilter implements Filter {
  private Matcher matcher;

  public GrepFilter(Pattern pattern) {
    this.matcher = pattern.matcher("");
  }

  public boolean lineMatches(Line line) {
    matcher.reset(line);
    boolean result = matcher.find();
    return result;
  }
}
