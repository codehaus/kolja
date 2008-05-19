package com.baulsupp.kolja.log.util;

public class IntRange {
  int from;

  int to;

  public IntRange() {
  }

  public IntRange(IntRange other) {
    this.from = other.from;
    this.to = other.to;
  }

  public IntRange(int from, int to) {
    this.from = from;
    this.to = to;
  }

  public int getFrom() {
    return from;
  }

  public void setFrom(int from) {
    this.from = from;
  }

  public int getTo() {
    return to;
  }

  public void setTo(int to) {
    this.to = to;
  }

  public int getLength() {
    ensureValid();

    return to - from;
  }

  private void ensureValid() {
    assert (isValid());
  }

  public boolean isValid() {
    return (to >= from) && (from >= 0);
  }

  public boolean meets(IntRange other) {
    if (other.getLength() == 0 || this.getLength() == 0)
      return false;

    return from == other.to || to == other.from;
  }

  public boolean isInclusiveSubsetOf(IntRange other) {
    ensureValid();
    other.ensureValid();

    if (other.getLength() == 0 || this.getLength() == 0)
      return false;

    return other.from <= from && to <= other.to;
  }

  public boolean contains(int x) {
    return x >= from && x < to;
  }

  private boolean within(int x) {
    return x > from && x < to - 1;
  }

  public boolean isOverlapping(IntRange other) {
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

  public IntRange merge(IntRange other) {
    if (isOverlapping(other))
      throw new IllegalArgumentException("ranges overlap");

    if (!meets(other))
      throw new IllegalArgumentException("ranges do not meet");

    if (other.getLength() == 0 || this.getLength() == 0)
      throw new IllegalArgumentException("range is empty");

    IntRange result = new IntRange();

    result.from = Math.min(this.from, other.from);
    result.to = Math.max(this.to, other.to);

    return result;
  }

  public boolean isEmpty() {
    return getLength() == 0;
  }

  public boolean isCompletelyBefore(IntRange other) {
    if (other.isEmpty() || this.isEmpty())
      return false;

    if (isOverlapping(other))
      return false;

    return (from < other.from);
  }

  public boolean isCompletelyAfter(IntRange other) {
    if (other.isEmpty() || this.isEmpty())
      return false;

    if (isOverlapping(other))
      return false;

    return (to > other.to);
  }

  public boolean equals(Object other) {
    if (!(other instanceof IntRange))
      return false;

    return equals((IntRange) other);
  }

  public boolean equals(IntRange other) {
    if (getLength() == 0 && other.getLength() == 0)
      return true;

    return from == other.from && to == other.to;
  }

  public int hashCode() {
    if (getLength() == 0)
      return 0;

    return from ^ to;
  }

  public boolean isBefore(int point) {
    if (isEmpty())
      return false;

    return to <= point;
  }

  public boolean isAfter(int point) {
    if (isEmpty())
      return false;

    return from > point;
  }

  public String toString() {
    return "" + from + ":" + to;
  }
}