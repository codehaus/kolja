package com.baulsupp.kolja.log.util;

public class LongRange {
  private long from;

  private long to;

  public LongRange() {
  }

  public LongRange(LongRange other) {
    this.from = other.from;
    this.to = other.to;
  }

  public LongRange(long from, long to) {
    this.from = from;
    this.to = to;
  }

  public long getFrom() {
    return from;
  }

  public void setFrom(long from) {
    this.from = from;
  }

  public long getTo() {
    return to;
  }

  public void setTo(long to) {
    this.to = to;
  }

  public long getLength() {
    ensureValid();

    return to - from;
  }

  private void ensureValid() {
    assert (isValid());
  }

  public boolean isValid() {
    return (to >= from) && (from >= 0);
  }

  public boolean meets(LongRange other) {
    if (other.getLength() == 0 || this.getLength() == 0)
      return false;

    return from == other.to || to == other.from;
  }

  public boolean isInclusiveSubsetOf(LongRange other) {
    ensureValid();
    other.ensureValid();

    if (other.getLength() == 0 || this.getLength() == 0)
      return false;

    return other.from <= from && to <= other.to;
  }

  public boolean contains(long x) {
    return x >= from && x < to;
  }

  private boolean within(long x) {
    return x > from && x < to - 1;
  }

  public boolean isOverlapping(LongRange other) {
    if (other.getLength() == 0 || this.getLength() == 0)
      return false;

    if (this.isInclusiveSubsetOf(other) || other.isInclusiveSubsetOf(this))
      return true;

    if (other.within(from))
      return true;

    if (other.within(to))
      return true;

    return false;
  }

  public LongRange merge(LongRange other) {
    if (isOverlapping(other))
      throw new IllegalArgumentException("ranges overlap");

    if (!meets(other))
      throw new IllegalArgumentException("ranges do not meet");

    if (other.getLength() == 0 || this.getLength() == 0)
      throw new IllegalArgumentException("range is empty");

    LongRange result = new LongRange();

    result.from = Math.min(this.from, other.from);
    result.to = Math.max(this.to, other.to);

    return result;
  }

  public boolean isEmpty() {
    return getLength() == 0;
  }

  public boolean isCompletelyBefore(LongRange other) {
    if (other.isEmpty() || this.isEmpty())
      return false;

    if (isOverlapping(other))
      return false;

    return (from < other.from);
  }

  public boolean isCompletelyAfter(LongRange other) {
    if (other.isEmpty() || this.isEmpty())
      return false;

    if (isOverlapping(other))
      return false;

    return (to > other.to);
  }

  public boolean equals(Object other) {
    if (!(other instanceof LongRange))
      return false;

    return equals((LongRange) other);
  }

  public boolean equals(LongRange other) {
    if (getLength() == 0 && other.getLength() == 0)
      return true;

    return from == other.from && to == other.to;
  }

  public int hashCode() {
    if (getLength() == 0)
      return 0;

    return (int) from ^ (int) to;
  }

  public boolean isBefore(long point) {
    if (isEmpty())
      return false;

    return to <= point;
  }

  public boolean isAfter(long point) {
    if (isEmpty())
      return false;

    return from > point;
  }

  public String toString() {
    return "" + from + ":" + to;
  }
}