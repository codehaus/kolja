package com.baulsupp.kolja.log.viewer.io;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.baulsupp.kolja.log.line.BasicLine;
import com.baulsupp.kolja.log.line.Line;

import junit.framework.TestCase;

public class NamedIteratorTest extends TestCase {
  /**
   * Exception in thread "main" java.lang.UnsupportedOperationException
   * at java.util.AbstractMap.put(AbstractMap.java:228)
   * at com.baulsupp.kolja.log.line.BasicLine.setValue(BasicLine.java:63)
   * at com.baulsupp.kolja.log.viewer.io.NamedIterator.next(NamedIterator.java:24)
   */
  public void testKolja23() {
    BasicLine l1 = new BasicLine("b1");
    l1.setValues(new HashMap<String, Object>());
    BasicLine l2 = new BasicLine("b2");
    l2.setFailed();
    List<Line> l = Arrays.asList((Line) l1, l2);
    NamedIterator ni = new NamedIterator("A", l.iterator());
    
    ni.next();
    
    // Was throwing UnsupportedOperationException
    ni.next();
  }
}
