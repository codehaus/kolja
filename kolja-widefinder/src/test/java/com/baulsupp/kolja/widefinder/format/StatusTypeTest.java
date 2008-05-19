package com.baulsupp.kolja.widefinder.format;

import static junit.framework.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class StatusTypeTest {
  private StatusType type;

  @Before
  public void setup() {
    type = new StatusType();
  }

  @Test
  public void testParses200() {
    assertEquals(HttpStatus.SUCCESS_OK, type.parse("200"));
  }

  @Test
  public void testParses404() {
    assertEquals(HttpStatus.CLIENT_ERROR_NOT_FOUND, type.parse("404"));
  }

  @Test
  public void testParses500() {
    assertEquals(HttpStatus.SERVER_ERROR_INTERNAL, type.parse("500"));
  }
}
