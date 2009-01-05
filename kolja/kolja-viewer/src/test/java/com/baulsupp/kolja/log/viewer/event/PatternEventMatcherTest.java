package com.baulsupp.kolja.log.viewer.event;

import com.baulsupp.kolja.log.line.BasicLine;
import junit.framework.TestCase;

import java.util.regex.Pattern;

public class PatternEventMatcherTest extends TestCase {
  private PatternEventMatcher matcher;

  @Override
  protected void setUp() throws Exception {
    super.setUp();

    Pattern pattern = Pattern.compile(" (AB) ");
    matcher = new PatternEventMatcher("content", pattern, "value %s value");
  }

  public void testMatchesValidString() {
    Event event = matcher.match(line("before AB after"));

    assertNotNull(event);

    assertEquals("value AB value", event.toString());
  }

  public void testDoesNotMatchInvalidString() {
    Event event = matcher.match(line("before AC after"));

    assertNull(event);
  }

  public void testDoesNotMatchStringWithoutField() {
    matcher.setContentField("xx");

    Event event = matcher.match(line("before AC after"));

    assertNull(event);
  }

  private BasicLine line(String s) {
    BasicLine line = new BasicLine(s);

    line.setValue("content", s);

    return line;
  }
}
