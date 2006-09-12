package com.baulsupp.kolja.util;

import java.beans.PropertyEditorSupport;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternPropertyEditor extends PropertyEditorSupport {
  private static final Pattern withMode = Pattern.compile("(\\d+):(.*)");

  public void setAsText(String text) throws IllegalArgumentException {
    Pattern p;
    
    p = parsePattern(text);
    
    setValue(p);
  }

  public static Pattern parsePattern(String text) {
    Pattern p;
    Matcher m = withMode.matcher(text);
    if (m.matches()) {
      int mode = Integer.parseInt(m.group(1));
      String pattern = m.group(2);

      p = Pattern.compile(pattern, mode);
    } else {
      p = Pattern.compile(text);
    }
    return p;
  }
}