package com.baulsupp.kolja.log.viewer;

import java.util.SortedSet;

import junit.framework.TestCase;

import com.baulsupp.kolja.log.line.BasicLine;
import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.util.IntRange;
import com.baulsupp.kolja.log.viewer.event.BasicEventDetector;
import com.baulsupp.kolja.log.viewer.event.Event;
import com.baulsupp.kolja.log.viewer.event.EventList;
import com.baulsupp.kolja.log.viewer.event.EventMatcher;

public class TestEventList extends TestCase {
  @SuppressWarnings("serial")
  public void testEventLog() {
    MockLineIndex index = new MockLineIndex();

    index.addLine(new BasicLine(0, "Exception: OutOfMemoryException"));
    index.addLine(new BasicLine(100, "nothing to see here"));
    index.addLine(new BasicLine(200, "nothing to see here"));
    index.addLine(new BasicLine(300, "Exception: ClassNotFoundException"));

    BasicEventDetector ed = new BasicEventDetector();

    ed.addEventMatcher(new EventMatcher() {
      public Event match(Line l) {
        if (l.toString().contains("OutOfMemoryException")) {
          return new Event(l, "Out Of Memory");
        }

        return null;
      }
    });

    ed.addEventMatcher(new EventMatcher() {
      public Event match(Line l) {
        if (l.toString().contains("ClassNotFoundException")) {
          return new Event(l, "Missing Class");
        }

        return null;
      }
    });

    EventList el = new EventList(index);
    el.setEventDetector(ed);

    el.ensureAllKnown();

    SortedSet<Event> events = el.getEvents();

    assertEquals(2, events.size());

    Event e1 = events.first();

    assertEquals(0, e1.getOffset());
    assertEquals("Out Of Memory", e1.toString());

    Event e2 = events.last();

    assertEquals(300, e2.getOffset());
    assertEquals("Missing Class", e2.toString());

    SortedSet<Event> s = el.getEvents(new IntRange(250, 350));

    assertEquals(1, s.size());
    assertEquals("Missing Class", s.first().toString());
  }
}
