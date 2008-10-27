package com.baulsupp.kolja.log.line;

public class LineIteratorUtil {
  public static void moveToLastTen(LineIterator bli) {
    bli.moveToEnd();
    for (int i = 0; i < 10; i++) {
      if (!bli.hasPrevious()) {
        break;
      }
      
      bli.previous();
    }
  }
}
