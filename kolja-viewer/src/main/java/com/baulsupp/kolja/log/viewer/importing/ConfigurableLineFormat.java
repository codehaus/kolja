package com.baulsupp.kolja.log.viewer.importing;

import java.io.Serializable;
import java.util.Set;

import com.baulsupp.kolja.log.entry.BufferedLogEntryIndex;
import com.baulsupp.kolja.log.entry.LogEntryIndex;
import com.baulsupp.kolja.log.entry.MemoryLogEntryIndex;
import com.baulsupp.kolja.log.line.LineIndex;
import com.baulsupp.kolja.log.line.LineParser;
import com.baulsupp.kolja.log.line.ParsingLineIndex;
import com.baulsupp.kolja.log.line.matcher.EntryPattern;

public class ConfigurableLineFormat implements Serializable {
  private static final long serialVersionUID = 3762034774959051436L;

  private EntryPattern entryPattern;

  private LineParser lineParser;

  public EntryPattern getEntryPattern() {
    return entryPattern;
  }

  public void setEntryPattern(EntryPattern entryPattern) {
    this.entryPattern = entryPattern;
  }

  public LineParser getLineParser() {
    return lineParser;
  }

  public void setLineParser(LineParser lineParser) {
    this.lineParser = lineParser;
  }

  public LineIndex buildLineIndex(CharSequence buffer, LogEntryIndex entryIndex) {
    return new ParsingLineIndex(lineParser, entryIndex, buffer);
  }

  public Set<String> getTypeNames() {
    return lineParser.getNames();
  }

  public LineIndex buildLineIndex(CharSequence buffer) {
    LogEntryIndex entryIndex = new MemoryLogEntryIndex(buffer, entryPattern);

    entryIndex = new BufferedLogEntryIndex(entryIndex);

    return buildLineIndex(buffer, entryIndex);
  }
}
