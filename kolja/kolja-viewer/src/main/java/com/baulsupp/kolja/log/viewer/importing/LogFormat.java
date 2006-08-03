package com.baulsupp.kolja.log.viewer.importing;

import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.line.LineIndex;
import com.baulsupp.kolja.log.viewer.event.EventList;
import com.baulsupp.kolja.log.viewer.renderer.Renderer;
import com.baulsupp.kolja.log.viewer.request.StandardRequestIndex;

public interface LogFormat {
  EventList getEventList(LineIndex li);

  boolean supportsEvents();

  enum Direction {
    FORWARD_ONLY, ANY_DIRECTION;
  }

  LineIndex getLineIndex(CharSequence buffer, Direction direction);

  Renderer<Line> getRenderer();

  StandardRequestIndex getRequestIndex(LineIndex lineIndex);

  boolean supportsRequests();

  String getRequestField();
}
