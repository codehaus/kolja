package com.baulsupp.kolja.log.viewer.importing;

import java.util.regex.Pattern;

import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.line.LineIndex;
import com.baulsupp.kolja.log.line.LineParser;
import com.baulsupp.kolja.log.viewer.event.EventList;
import com.baulsupp.kolja.log.viewer.renderer.Renderer;
import com.baulsupp.kolja.log.viewer.request.StandardRequestIndex;

public interface LogFormat {
  EventList getEventList(LineIndex li);

  boolean supportsEvents();

  LineIndex getLineIndex(CharSequence buffer);

  Renderer<Line> getRenderer();

  StandardRequestIndex getRequestIndex(LineIndex lineIndex);

  boolean supportsRequests();

  String getRequestField();

  Pattern getEntryPattern();

  LineParser getLineParser();
}
