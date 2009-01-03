package com.baulsupp.kolja.log.viewer.event;

import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.collections.primitives.ArrayIntList;

import com.baulsupp.kolja.log.field.MemoryIntField;
import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.line.LineIndex;
import com.baulsupp.kolja.log.line.ValueIndexer;
import com.baulsupp.kolja.log.util.IntRange;

/**
 * A list of events in a log file.
 */
public class EventList extends ValueIndexer implements EventMatcher {

  private SortedSet<Event> events = new TreeSet<Event>(new Comparator<Event>() {
    public int compare(Event event, Event event1) {
      return event.getOffset() - event1.getOffset();
    }
  });
  private EventMatcher eventDetector;

  public EventList(LineIndex lineIndex) {
    super(lineIndex);
  }

  public EventList(LineIndex lineIndex, EventMatcher eventDetector) {
    super(lineIndex);
    this.eventDetector = eventDetector;
  }

  protected void processLines(IntRange range, List<Line> regionLines) {
    indexed.add(new MemoryIntField(range, new ArrayIntList(0)));

    for (Line l : regionLines) {
      processLine(l);
    }
  }

  public Event processLine(Line l) {
    Event event = match(l);

    if (event != null) {
      events.add(event);
    }

    return event;
  }

  public Event match(Line l) {
    return eventDetector.match(l);
  }

  public SortedSet<Event> getEvents() {
    return events;
  }

  public SortedSet<Event> getEvents(IntRange intRange) {
    SortedSet<Event> subEvents = new TreeSet<Event>();

    for (Event e : events) {
      if (intRange.contains(e.getOffset())) {
        subEvents.add(e);
      }
    }

    return subEvents;
  }

  public void setEventDetector(EventMatcher ed) {
    this.eventDetector = ed;
  }
}
