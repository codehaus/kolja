package com.baulsupp.kolja.log.line;

import junit.framework.TestCase;

public class BasicLineTest extends TestCase {
  public void testGetValue() {
    BasicLine l = new BasicLine("A");
    
    assertNull(l.getValue("a"));
    
    l.setValue("a", "A");
    
    assertEquals("A", l.getValue("a"));
  }
}
