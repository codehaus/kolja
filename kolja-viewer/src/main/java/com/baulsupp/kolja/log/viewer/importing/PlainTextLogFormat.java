package com.baulsupp.kolja.log.viewer.importing;

import java.util.regex.Pattern;

import com.baulsupp.kolja.log.entry.BufferedLogEntryIndex;
import com.baulsupp.kolja.log.entry.LogEntryIndex;
import com.baulsupp.kolja.log.entry.MemoryLogEntryIndex;
import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.line.LineIndex;
import com.baulsupp.kolja.log.line.LineParser;
import com.baulsupp.kolja.log.line.LogEntryLineIndex;
import com.baulsupp.kolja.log.viewer.event.EventList;
import com.baulsupp.kolja.log.viewer.highlight.HighlightList;
import com.baulsupp.kolja.log.viewer.renderer.PlainTextRenderer;
import com.baulsupp.kolja.log.viewer.renderer.Renderer;
import com.baulsupp.kolja.log.viewer.request.StandardRequestIndex;

public class PlainTextLogFormat implements LogFormat {
  public EventList getEventList(LineIndex li) {
    return null;
  }

  public boolean supportsEvents() {
    return false;
  }

  public LineIndex getLineIndex(CharSequence buffer) {
    Pattern p = getEntryPattern();
    LogEntryIndex entryIndex = new MemoryLogEntryIndex(buffer, p);

    entryIndex = new BufferedLogEntryIndex(entryIndex);

    LogEntryLineIndex parsingLineIndex = new LogEntryLineIndex(entryIndex, buffer);

    return parsingLineIndex;
  }

  public HighlightList<Line> getHighlights() {
    HighlightList<Line> highlight = new HighlightList<Line>();
    return highlight;
  }

  public Renderer<Line> getRenderer() {
    return new PlainTextRenderer();
  }

  public StandardRequestIndex getRequestIndex(LineIndex lineIndex) {
    throw new UnsupportedOperationException();
  }

  public boolean supportsRequests() {
    return false;
  }

  public String getRequestField() {
    return null;
  }

  public Pattern getEntryPattern() {
    return Pattern.compile("^", Pattern.MULTILINE);
  }

  public LineParser getLineParser() {
    return new PlainTextLineParser();
  }
}
