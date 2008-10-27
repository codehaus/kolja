package com.baulsupp.kolja.widefinder.format;

import static junit.framework.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.baulsupp.kolja.log.viewer.format.BytesFormat;

public class BytesFormatTest {
  private BytesFormat format;

  @Before
  public void setup() {
    format = new BytesFormat();
  }

  @Test
  public void testBytes() {
    assertEquals("100b", format.format(100l));
  }

  @Test
  public void testKiloBytes() {
    assertEquals("1KB", format.format(BytesFormat.KB));
  }

  @Test
  public void testMegaBytes() {
    assertEquals("1MB", format.format(BytesFormat.MB));
  }

  @Test
  public void testSupportsDecimalPlaces() {
    format = new BytesFormat(1);
    assertEquals("1.1MB", format.format(BytesFormat.MB * 1.1));
  }

  @Test
  public void testGigaBytes() {
    assertEquals("1GB", format.format(BytesFormat.GB));
  }

  @Test
  public void testNull() {
    assertEquals(null, format.format(null));
  }
}
