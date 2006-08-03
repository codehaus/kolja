package com.baulsupp.kolja.util;

public class MathUtil {
  public static final int min(int min, int... values) {
    for (int i : values) {
      if (i < min) {
        min = i;
      }
    }
    return min;
  }
}
