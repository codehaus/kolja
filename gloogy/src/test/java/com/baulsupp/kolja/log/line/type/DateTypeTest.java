package com.baulsupp.kolja.log.line.type;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

import com.baulsupp.kolja.util.TestUtil;

public class DateTypeTest {
  @Test
  public void testIsSerializable() throws Exception {
    DateType f = new DateType("date", "dd MM yyyy");

    DateType f2 = TestUtil.serialiseAndDeserialize(f);

    String s = "28 02 1999";
    assertEquals(f.parse(s), f2.parse(s));
  }

}
