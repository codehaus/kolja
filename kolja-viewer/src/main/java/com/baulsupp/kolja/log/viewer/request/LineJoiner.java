package com.baulsupp.kolja.log.viewer.request;

import java.util.Collection;

import com.baulsupp.kolja.log.line.Line;

/**
 * TODO don't build a string each time
 */
public class LineJoiner implements CharSequence {
  private Collection<Line> lines;

  private String string;

  public LineJoiner(Collection<Line> lines) {
    this.lines = lines;
  }

  public int length() {
    return toString().length();
  }

  public char charAt(int arg0) {
    return toString().charAt(arg0);
  }

  public CharSequence subSequence(int arg0, int arg1) {
    return toString().subSequence(arg0, arg1);
  }

  @Override
  public String toString() {
    if (string == null) {
      StringBuilder buffy = new StringBuilder();

      for (Line line : lines) {
        if (buffy.length() > 0) {
          buffy.append('\n');
        }

        buffy.append(line);
      }

      string = buffy.toString();
    }

    return string;
  }
}
