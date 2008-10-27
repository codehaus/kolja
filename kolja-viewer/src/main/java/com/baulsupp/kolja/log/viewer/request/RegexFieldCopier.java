package com.baulsupp.kolja.log.viewer.request;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baulsupp.kolja.log.LogConstants;
import com.baulsupp.kolja.log.line.Line;

public class RegexFieldCopier implements FieldCopier, Serializable {
  private static final long serialVersionUID = -295776206270084404L;

  private String sourceField = LogConstants.CONTENT;
  private String[] fields;
  private Pattern pattern;
  
  private transient Matcher matcher;
  
  public RegexFieldCopier() {
  }

  public RegexFieldCopier(String sourceField, String patternString, String... fields) {
    this.sourceField = sourceField;
    this.pattern = Pattern.compile(patternString);
    this.fields = fields;
  }

  public String[] getFields() {
    return fields;
  }

  public void setFields(String[] fields) {
    this.fields = fields;
  }

  public Pattern getPattern() {
    return pattern;
  }

  public void setPattern(Pattern pattern) {
    this.pattern = pattern;
  }

  public String getSourceField() {
    return sourceField;
  }

  public void setSourceField(String sourceField) {
    this.sourceField = sourceField;
  }

  public void copy(Line line, RequestLine requestLine) {
    String content = (String) line.getValue(sourceField);

    if (content != null) {
      reset(content);
  
      if (matcher.find()) {
        for (int i = 0; i < fields.length; i++) {
          String value = matcher.group(i + 1);
          
          requestLine.setValue(fields[i], value);
        }
      }
    }
  }

  private void reset(String content) {
    if (matcher == null) {
      matcher = pattern.matcher("");
    }

    matcher.reset(content);
  }
}
