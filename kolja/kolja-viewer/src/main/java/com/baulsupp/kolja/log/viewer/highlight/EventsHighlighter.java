package com.baulsupp.kolja.log.viewer.highlight;

import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.viewer.event.EventMatcher;
import com.baulsupp.kolja.util.colours.ColourPair;

/**
 * Highlighter based on events
 */
public class EventsHighlighter implements Highlight<Line> {
  private EventMatcher events;
  private ColourPair colours;

  public EventsHighlighter(EventMatcher events, ColourPair colours) {
    this.events = events;
    this.colours = colours;
  }

  public HighlightResult getHighlights(Line line) {
    if (hasEvent(line)) {
      return HighlightResult.row(colours);
    }

    return null;
  }

  private boolean hasEvent(Line line) {
    return events.match(line) != null;
  }
}
