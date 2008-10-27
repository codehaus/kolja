/**
 * Copyright (c) 2002-2007 Yuri Schimke. All Rights Reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package com.baulsupp.kolja.log.line;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baulsupp.kolja.log.line.type.Type;
import com.baulsupp.kolja.log.line.type.TypeList;

public class RegexLineParser implements LineParser {
  private static final long serialVersionUID = 2578922978567306442L;

  private TypeList columns;

  private Pattern pattern;

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
  }

  public Line parse(CharSequence c) {
    BasicLine row = new BasicLine();

    row.setContent(c);

    Matcher matcher = pattern.matcher(c);

    if (!matcher.matches()) {
      row.setFailed();
      return row;
    }

    Map<String, Object> values = new HashMap<String, Object>(columns.size());

    int pos = 0;
    for (Type type : columns) {
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

  public Set<String> getNames() {
    return new HashSet<String>(columns.getNames());
  }

}
