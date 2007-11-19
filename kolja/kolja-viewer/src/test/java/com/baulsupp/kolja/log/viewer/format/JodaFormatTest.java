package com.baulsupp.kolja.log.viewer.format;

import static junit.framework.Assert.assertEquals;

import org.joda.time.DateTime;
import org.junit.Test;

import com.baulsupp.kolja.util.TestUtil;

public class JodaFormatTest {
  @Test
  public void testIsSerializable() throws Exception {
    JodaFormat f = new JodaFormat("dd MM yyyy");

    JodaFormat f2 = TestUtil.serialiseAndDeserialize(f);

    DateTime date = new DateTime();
    assertEquals(f.format(date), f2.format(date));
  }
}
