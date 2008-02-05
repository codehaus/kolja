package com.baulsupp.kolja.log.line;

import com.baulsupp.kolja.log.entry.LogEntryIndex;

public class ParsingLineIndex extends LogEntryLineIndex {
  private LineParser parser;

  private boolean strict = false;

  public ParsingLineIndex(LineParser parser, LogEntryIndex index, CharSequence text) {
    super(index, text);

    this.parser = parser;
  }

  protected Line buildLine(int lineStart, int lineEnd) {
    CharSequence section = text.subSequence(lineStart, lineEnd);

    Line l = parser.parse(section);

    if (strict && l.isFailed()) {
      throw new RuntimeException("" + lineStart + " " + l);
    }

    l.setOffset(lineStart);

    return l;
  }

  public boolean isStrict() {
    return strict;
  }

  public void setStrict(boolean strict) {
    this.strict = strict;
  }
}
