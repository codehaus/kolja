package com.baulsupp.kolja.log.viewer.format;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CompressedPackageFormat implements OutputFormat {
  private static final long serialVersionUID = 6750671123052767172L;

  private Pattern pattern = Pattern.compile("(\\w)\\w*\\.");

  private Matcher matcher = pattern.matcher("");

  public String format(Object value) {
    if (value == null) {
      return "";
    }
      
    matcher.reset(value.toString());

    return matcher.replaceAll("$1.");
  }
}
