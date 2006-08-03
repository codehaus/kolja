package com.baulsupp.kolja.log.viewer.event;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baulsupp.kolja.log.LogConstants;
import com.baulsupp.kolja.log.line.Line;

public class PatternEventMatcher implements EventMatcher, Serializable {
  private static final long serialVersionUID = 8950666564334952873L;

  private String contentField = LogConstants.CONTENT;
  private Pattern pattern;
  private String message;
  
  private transient Matcher matcher;

  public PatternEventMatcher() {
  }
  
  public PatternEventMatcher(String contentField, Pattern pattern, String message) {
    this.contentField = contentField;
    this.pattern = pattern;
    this.message = message;
  }

  public String getContentField() {
    return contentField;
  }

  public void setContentField(String contentField) {
    this.contentField = contentField;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Pattern getPattern() {
    return pattern;
  }

  public void setPattern(Pattern pattern) {
    this.pattern = pattern;
  }

  public Event match(Line l) {
    String m = (String) l.getValue(contentField);

    if (m != null) {
      reset(m);
      
      if (matcher.matches()) {
        String text = format();
        return new Event(l, text);
      }
    }
    
    return null;
  }

  private String format() {
    String[] values = extractGroups(matcher);

    return String.format(message, (Object[]) values);
  }

  private String[] extractGroups(Matcher matcher) {
    String[] values = new String[matcher.groupCount()];
    
    for (int i = 0; i < values.length; i++) {
      values[i] = matcher.group(i + 1);
    }
    
    return values;
  }

  private void reset(String l) {
    if (matcher == null) {
      matcher = pattern.matcher(l);
    } else {
      matcher.reset(l);
    }
  }
}
