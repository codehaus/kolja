package com.baulsupp.kolja.widefinder.format;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.baulsupp.kolja.widefinder.format.BytesType;

public class BytesTypeTest {
  private BytesType type;

  @Before
  public void setup() {
    type = new BytesType();
  }

  @Test
  public void testParsesDash() {
    assertNull(type.parse("-"));
  }

  @Test
  public void testParsesNumber() {
    assertEquals(1l, type.parse("1"));
  }
}
