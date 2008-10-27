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
public class EventList extends ValueIndexer {

  private SortedSet<Event> events = new TreeSet<Event>(new Comparator<Event>() {
    public int compare(Event event, Event event1) {
      return event.getOffset() - event1.getOffset();
    }
  });
  private EventDetector eventDetector;

  public EventList(LineIndex lineIndex) {
    super(lineIndex);
  }

  public EventList(LineIndex lineIndex, EventDetector eventDetector) {
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
    Event event = null;

    event = eventDetector.readEvent(l);

    if (event != null) {
      events.add(event);
    }

    return event;
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

  public void setEventDetector(EventDetector ed) {
    this.eventDetector = ed;

  }
}
