package com.baulsupp.kolja.jez;

import static org.junit.Assert.assertEquals;

import org.joda.time.DateTime;
import org.junit.Test;

public class DateTypeTest {

  @Test
  public void testParseString() {
    DateTime dateTime = new DateTime();
    assertEquals(dateTime, new DateType("x").parse(String.valueOf(dateTime.getMillis())));
  }

}
