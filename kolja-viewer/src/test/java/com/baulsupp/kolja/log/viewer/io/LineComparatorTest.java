package com.baulsupp.kolja.log.viewer.io;

import java.util.Date;

import com.baulsupp.kolja.log.line.BasicLine;
import com.baulsupp.kolja.log.line.Line;

import junit.framework.TestCase;

public class LineComparatorTest extends TestCase {
  private LineComparator c = new LineComparator("date");
  
  public void testCompare() {
    Line l1null = createLine(null);
    Line l2null = createLine(null);
    Line lMinus = createLine(new Date(System.currentTimeMillis() - 1000));
    Line l1Now = createLine(new Date(System.currentTimeMillis()));
    Line l2Now = createLine(new Date(System.currentTimeMillis()));
    
    assertTrue(c.compare(l1null, l2null) < 0);
    assertTrue(c.compare(l1Now, l2Now) < 0);
    assertTrue(c.compare(lMinus, l2Now) < 0);
  }

  private Line createLine(Date date) {
    BasicLine l = new BasicLine();
    
    l.setValue("date", date);
    
    return l;
  }
}
