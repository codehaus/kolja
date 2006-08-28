package com.baulsupp.kolja.log.line;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baulsupp.kolja.log.line.type.Type;
import com.baulsupp.kolja.log.line.type.TypeList;

public class RegexLineParser implements LineParser {
  private TypeList columns;

  private Pattern pattern;

  private Matcher matcher;

  public RegexLineParser() {
  }

  public RegexLineParser(Pattern pattern, TypeList columns) {
    setPattern(pattern);
    setColumns(columns);
  }

  public Pattern getPattern() {
    return pattern;
  }

  public void setPattern(Pattern pattern) {
    this.pattern = pattern;
    this.matcher = pattern.matcher("");
  }

  public Line parse(CharSequence c) {
    BasicLine row = new BasicLine();

    row.setContent(c);

    matcher.reset(c);

    if (!matcher.matches()) {
      row.setFailed();
      return row;
    }

    Map<String, Object> values = new HashMap<String, Object>(columns.size());

    int pos = 0;
    for (Type type: columns) {
      Object value = type.parse(matcher.group(++pos));
      values.put(type.getName(), value);
    }

    row.setValues(values);

    return row;
  }

  public TypeList getColumns() {
    return columns;
  }

  public void setColumns(TypeList columns) {
    this.columns = columns;
  }
}
