package com.baulsupp.kolja.log.viewer.format;

import static junit.framework.Assert.assertEquals;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import com.baulsupp.kolja.util.TestUtil;

public class JodaFormatTest {
  private JodaFormat f;

  @Before
  public void setup() {
    f = new JodaFormat("dd MM yyyy");
  }

  @Test
  public void testIsSerializable() throws Exception {
    JodaFormat f2 = TestUtil.serialiseAndDeserialize(f);

    DateTime date = new DateTime();
    assertEquals(f.format(date), f2.format(date));
  }

  @Test
  public void testNullReturnsNull() {
    assertEquals(null, f.format(null));
  }
}
