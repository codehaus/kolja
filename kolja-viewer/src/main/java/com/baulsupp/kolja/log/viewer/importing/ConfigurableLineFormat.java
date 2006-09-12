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
    RegexLineParser parser = getLineParser();

    return new ParsingLineIndex(parser, entryIndex, buffer);
  }

  public RegexLineParser getLineParser() {
    if (fieldPattern == null) {
      throw new NullPointerException("fieldPattern == null");
    }
    
    if (types == null) {
      throw new NullPointerException("types == null");
    }
    
    RegexLineParser parser = new RegexLineParser();
    parser.setPattern(fieldPattern);
    parser.setColumns(types);
    return parser;
  }
  
  public List<String> getTypeNames() {
    return types.getNames();
  }
  
  public LineIndex buildLineIndex(CharSequence buffer) {
    LogEntryIndex entryIndex = new MemoryLogEntryIndex(buffer, entryPattern);

    entryIndex = new BufferedLogEntryIndex(entryIndex);

    return buildLineIndex(buffer, entryIndex);
  } 
}
