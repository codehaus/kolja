package com.baulsupp.kolja.log.viewer.importing;

import java.io.Serializable;
import java.util.List;
import java.util.regex.Pattern;

import com.baulsupp.kolja.log.entry.BufferedLogEntryIndex;
import com.baulsupp.kolja.log.entry.LogEntryIndex;
import com.baulsupp.kolja.log.entry.MemoryLogEntryIndex;
import com.baulsupp.kolja.log.line.LineIndex;
import com.baulsupp.kolja.log.line.ParsingLineIndex;
import com.baulsupp.kolja.log.line.RegexLineParser;
import com.baulsupp.kolja.log.line.type.TypeList;
import com.baulsupp.kolja.log.viewer.importing.LogFormat.Direction;

public class ConfigurableLineFormat implements Serializable {
  private static final long serialVersionUID = 3762034774959051436L;

  private Pattern entryPattern;
  private Pattern fieldPattern;
  private TypeList types = new TypeList();
  
  public Pattern getEntryPattern() {
    return entryPattern;
  }
  public void setEntryPattern(Pattern entryPattern) {
    this.entryPattern = entryPattern;
  }
  public Pattern getFieldPattern() {
    return fieldPattern;
  }
  public void setFieldPattern(Pattern fieldPattern) {
    this.fieldPattern = fieldPattern;
  }
  public TypeList getTypes() {
    return types;
  }
  public void setTypes(TypeList types) {
    this.types = types;
  }
 
  public LineIndex buildLineIndex(CharSequence buffer, LogEntryIndex entryIndex) {
    RegexLineParser parser = new RegexLineParser();
    parser.setPattern(fieldPattern);
    parser.setColumns(types);

    return new ParsingLineIndex(parser, entryIndex, buffer);
  }
  
  public List<String> getTypeNames() {
    return types.getNames();
  }
  
  public LineIndex buildLineIndex(CharSequence buffer, Direction direction) {
    LogEntryIndex entryIndex = new MemoryLogEntryIndex(buffer, entryPattern);

    if (direction == LogFormat.Direction.ANY_DIRECTION) {
      entryIndex = new BufferedLogEntryIndex(entryIndex);
    }

    return buildLineIndex(buffer, entryIndex);
  } 
}
