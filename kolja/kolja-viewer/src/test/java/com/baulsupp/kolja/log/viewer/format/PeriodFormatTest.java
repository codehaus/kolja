package com.baulsupp.kolja.log.viewer.format;

import static junit.framework.Assert.assertEquals;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.junit.Before;
import org.junit.Test;

import com.baulsupp.kolja.util.TestUtil;

public class PeriodFormatTest {
  private PeriodFormat f;
  private DateTime from;
  private DateTime to;

  @Before
  public void setup() {
    f = new PeriodFormat();

    from = new DateTime();
    to = from.plusHours(1).plusMinutes(12);
  }

  @Test
  public void testIsSerializable() throws Exception {
    PeriodFormat f = new PeriodFormat();

    TestUtil.serialiseAndDeserialize(f);
  }

  @Test
  public void testNullReturnsNull() {
    assertEquals(null, f.format(null));
  }

  @Test
  public void testFormatsZeroPeriod() {
    assertEquals("0s", f.format(new Period(from, from)));
  }

  @Test
  public void testFormatsPeriodOfMillis() {
    assertEquals("0.134s", f.format(new Period(from, from.plusMillis(134))));
  }

  @Test
  public void testFormatsPeriodOfSeconds() {
    assertEquals("20.134s", f.format(new Period(from, from.plusMillis(20134))));
  }

  @Test
  public void testFormatsPeriodOfSecondsAndDays() {
    assertEquals("2d20.134s", f.format(new Period(from, from.plusDays(2).plusMillis(20134))));
  }

  @Test
  public void testFormatsPeriod() {
    assertEquals("1h12m", f.format(new Period(from, to)));
  }

  @Test
  public void testFormatsDuration() {
    assertEquals("1h12m", f.format(new Duration(from, to)));
  }

  @Test
  public void testFormatsInterval() {
    assertEquals("1h12m", f.format(new Interval(from, to)));
  }
}
