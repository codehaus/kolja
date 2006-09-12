package com.baulsupp.kolja.log.util;

/**
 * @author yuri
 */
public class LimitedCharBuffer implements Cloneable, CharSequence {
  private CharSequence buffer;

  private int start;

  private int end;

  LimitedCharBuffer(CharSequence buffer, int start, int end) {
    // TODO remove
    if (start > end)
      throw new IllegalArgumentException("start > end");
    if (start < 0)
      throw new IllegalArgumentException("start < 0");
    if (end < 0)
      throw new IllegalArgumentException("end < 0");

    this.buffer = buffer;
    this.start = start;
    this.end = end;
  }

  public int length() {
    return end - start;
  }

  public char charAt(int index) {
    return buffer.charAt(index + start);
  }

  public String toString() {
    int l = length();

    StringBuilder sb = new StringBuilder(l);

    for (int i = 0; i < l; i++) {
      sb.append(buffer.charAt(i + start));
    }

    String s = sb.toString();

    return s;
  }

  public CharSequence subSequence(int start, int end) {
    LimitedCharBuffer other;
    try {
      other = (LimitedCharBuffer) this.clone();
    } catch (CloneNotSupportedException e) {
      throw new Error(e);
    }

    other.start += start;
    other.end = other.start + (end - start);

    return other;
  }
}
