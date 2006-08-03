package com.baulsupp.kolja.log.line;

import java.util.Iterator;

public interface LineIterator extends Iterator<Line> {
  void moveTo(int position);

  boolean hasNext();

  // null for the end
  Line next();

  boolean hasPrevious();

  // null for the end
  Line previous();

  void moveToEnd();

  void moveToStart();
}
