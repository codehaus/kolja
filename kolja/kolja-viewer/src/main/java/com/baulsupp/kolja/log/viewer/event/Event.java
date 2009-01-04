package com.baulsupp.kolja.log.viewer.event;

import com.baulsupp.kolja.log.line.Line;

public class Event {
  private Line line;
  private String message;

  public Event(Line l) {
    this.line = l;
  }

  public Event(Line l, String s) {
    this.line = l;
    this.message = s;
  }

  public Line getLine() {
    return line;
  }

  public String getMessage() {
    return message;
  }

  public String toString() {
    return message != null ? message : line.toString();
  }

  public int getOffset() {
    return line.getIntOffset();
  }
}
