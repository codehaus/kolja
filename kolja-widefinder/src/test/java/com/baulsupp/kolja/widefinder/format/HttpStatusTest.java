package com.baulsupp.kolja.widefinder.format;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.fail;

import org.junit.Test;

public class HttpStatusTest {
  @Test
  public void testDoesNotAcceptNull() {
    try {
      new HttpStatus(null);
      fail();
    } catch (IllegalArgumentException iae) {
      // expected
    }
  }

  @Test
  public void testGetterReturnsCode() {
    HttpStatus status = new HttpStatus("200");

    assertEquals("200", status.getCode());
  }

  @Test
  public void testToString() {
    HttpStatus status = new HttpStatus("200");

    assertEquals("200", status.toString());
  }

  @Test
  public void testHashCodeIsEquals() {
    HttpStatus status1 = new HttpStatus("200");
    HttpStatus status2 = new HttpStatus("200");

    assertEquals(status1.hashCode(), status1.hashCode());
    assertEquals(status1.hashCode(), status2.hashCode());
  }

  @Test
  public void testEquals() {
    HttpStatus status1a = new HttpStatus("200");
    HttpStatus status1b = new HttpStatus("200");
    HttpStatus status2 = new HttpStatus("201");
    String string = "200";

    assertEquals(status1a, status1a);
    assertEquals(status1a, status1b);
    assertFalse(status1a.equals(status2));
    assertFalse(status1a.equals(string));
    assertFalse(status1a.equals(null));
  }

}
