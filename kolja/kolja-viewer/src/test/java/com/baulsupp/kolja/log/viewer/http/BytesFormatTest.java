package com.baulsupp.kolja.log.viewer.http;

import static junit.framework.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

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
  public void testGigaBytes() {
    assertEquals("1GB", format.format(BytesFormat.GB));
  }

  @Test
  public void testNull() {
    assertEquals(null, format.format(null));
  }
}
