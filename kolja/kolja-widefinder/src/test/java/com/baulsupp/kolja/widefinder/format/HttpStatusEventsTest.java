package com.baulsupp.kolja.widefinder.format;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.baulsupp.kolja.log.line.BasicLine;
import com.baulsupp.kolja.log.viewer.event.Event;
import com.baulsupp.kolja.widefinder.format.HttpStatusEvents;

public class HttpStatusEventsTest {
  private HttpStatusEvents events;
  private BasicLine l;

  @Before
  public void setup() {
    events = new HttpStatusEvents();

    l = new BasicLine();
    l.setValue("url", "/ongoing.atom");
  }

  @Test
  public void testNonEvent() {
    l.setValue("status", "200");

    assertNull(events.match(l));
  }

  @Test
  public void test500() {
    l.setValue("status", "500");

    Event event = events.match(l);

    assertNotNull(event);
    assertEquals("500 /ongoing.atom", event.toString());
  }

  @Test
  public void test404() {
    l.setValue("status", "404");

    Event event = events.match(l);

    assertNotNull(event);
    assertEquals("404 /ongoing.atom", event.toString());
  }
}
