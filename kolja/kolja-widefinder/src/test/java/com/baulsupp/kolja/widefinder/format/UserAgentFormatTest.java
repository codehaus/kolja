package com.baulsupp.kolja.widefinder.format;

import static junit.framework.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.baulsupp.kolja.widefinder.format.UserAgentFormat;

public class UserAgentFormatTest {
  private UserAgentFormat format;

  @Before
  public void setup() {
    format = new UserAgentFormat();
  }

  @Test
  public void testFirefox() {
    assertEquals("Firefox 1.x", format
        .format("Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.8.0.7) Gecko/20060909 Firefox/1.5.0.7"));
  }

  @Test
  public void testUnknown() {
    assertEquals("1234567890", format.format("1234567890"));
  }

  @Test
  public void testNull() {
    assertEquals(null, format.format(null));
  }

}
