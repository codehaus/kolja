package com.baulsupp.kolja.log.viewer.io;

import java.util.Collections;

import junit.framework.TestCase;

import com.baulsupp.kolja.log.line.BasicLine;
import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.viewer.io.MultipleLineIterator;

public class MultipleLineIndexTest extends TestCase {
  public void testRemove() {
    MultipleLineIterator mli = new MultipleLineIterator();
    
    try {
      mli.remove();
      fail("expected exception");
    } catch (UnsupportedOperationException usoe) {
      // expected
    }
  }

  public void testNone() {
    MultipleLineIterator mli = new MultipleLineIterator();
    
    assertFalse(mli.hasNext());
  }
  
  public void testOne() {
    MultipleLineIterator mli = new MultipleLineIterator();
    
    Line testLine = new BasicLine("A");
    mli.add("1", Collections.singleton(testLine).iterator());
    
    assertTrue(mli.hasNext());
    Line l = mli.next();
    assertNotNull(l);
    assertEquals("1", l.getValue(MultipleLineIterator.FILE_NAME));
    assertEquals("A", l.toString());

    assertFalse(mli.hasNext());
  }
  
  public void testTwo() {
    MultipleLineIterator mli = new MultipleLineIterator();
    
    Line testLine = new BasicLine("A");
    mli.add("1", Collections.singleton(testLine).iterator());
    
    Line l = mli.next();

    assertFalse(mli.hasNext());

    testLine = new BasicLine("B");
    mli.add("2", Collections.singleton(testLine).iterator());
    
    assertTrue(mli.hasNext());
    l = mli.next();
    assertNotNull(l);
    assertEquals("2", l.getValue(MultipleLineIterator.FILE_NAME));
    assertEquals("B", l.toString());

    assertFalse(mli.hasNext());
  }
}
