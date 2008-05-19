package com.baulsupp.kolja.log.viewer.io;

import junit.framework.TestCase;

import org.joda.time.DateTime;

import com.baulsupp.kolja.log.LogConstants;
import com.baulsupp.kolja.log.line.BasicLine;
import com.baulsupp.kolja.log.line.Line;

public class LineComparatorTest extends TestCase {
  private LineComparator c = new LineComparator(LogConstants.DATE);

  public void testCompare() {
    Line l1null = createLine(null);
    Line l2null = createLine(null);
    Line lMinus = createLine(new DateTime(System.currentTimeMillis() - 1000));
    Line l1Now = createLine(new DateTime(System.currentTimeMillis()));
    Line l2Now = createLine(new DateTime(System.currentTimeMillis()));

    assertTrue(c.compare(l1null, l2null) < 0);
    assertTrue(c.compare(l1Now, l2Now) < 0);
    assertTrue(c.compare(lMinus, l2Now) < 0);
  }

  private Line createLine(DateTime date) {
    BasicLine l = new BasicLine();

    l.setValue(LogConstants.DATE, date);

    return l;
  }
}
