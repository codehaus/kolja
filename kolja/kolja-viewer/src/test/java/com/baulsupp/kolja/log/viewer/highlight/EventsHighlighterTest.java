package com.baulsupp.kolja.log.viewer.highlight;

import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.viewer.event.Event;
import com.baulsupp.kolja.log.viewer.event.EventMatcher;
import com.baulsupp.kolja.util.colours.ColourPair;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class EventsHighlighterTest {
  Mockery context = new JUnit4Mockery();

  private EventMatcher eventList = context.mock(EventMatcher.class);
  private Line line = context.mock(Line.class);
  private EventsHighlighter eventsHighlight;

  @Before
  public void setUp() {
    eventsHighlight = new EventsHighlighter(eventList, ColourPair.GREEN_ON_BLACK);
  }

  @Test
  public void testShowsHighlightForEvent() {
    final Event event = new Event(line, "event");

    context.checking(new Expectations() {{
      one(eventList).match(line); will(returnValue(event));
    }});

    HighlightResult highlight = eventsHighlight.getHighlights(line);

    assertEquals(ColourPair.GREEN_ON_BLACK, highlight.getRow());
  }

  @Test
  public void testNoHighlightsWithoutEvent() {
    context.checking(new Expectations() {{
      one(eventList).match(line); will(returnValue(null));
    }});

    HighlightResult highlight = eventsHighlight.getHighlights(line);

    assertNull(highlight);
  }
}
