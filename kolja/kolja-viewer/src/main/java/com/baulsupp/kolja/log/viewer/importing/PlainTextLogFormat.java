package com.baulsupp.kolja.log.viewer.importing;

import com.baulsupp.kolja.log.entry.BufferedLogEntryIndex;
import com.baulsupp.kolja.log.entry.LogEntryIndex;
import com.baulsupp.kolja.log.entry.MemoryLogEntryIndex;
import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.line.LineIndex;
import com.baulsupp.kolja.log.line.LineParser;
import com.baulsupp.kolja.log.line.LogEntryLineIndex;
import com.baulsupp.kolja.log.line.matcher.EntryPattern;
import com.baulsupp.kolja.log.line.matcher.NewLineEntryPattern;
import com.baulsupp.kolja.log.viewer.event.EventMatcher;
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
    EntryPattern p = getEntryPattern();
    LogEntryIndex entryIndex = new MemoryLogEntryIndex(buffer, p);

    entryIndex = new BufferedLogEntryIndex(entryIndex);

    return new LogEntryLineIndex(entryIndex, buffer);
  }

  public HighlightList<Line> getHighlights() {
    return new HighlightList<Line>();
  }

  public Renderer<Line> getLineRenderer() {
    return new PlainTextRenderer();
  }

  public Renderer<Line> getRequestRenderer() {
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

  public EntryPattern getEntryPattern() {
    return new NewLineEntryPattern();
  }

  public LineParser getLineParser() {
    return new PlainTextLineParser();
  }

  public EventMatcher getEventDetector() {
    return null;
  }
}
