package com.baulsupp.kolja.log.util;

import org.apache.commons.collections.primitives.IntList;

public class CollectionSearch {
  /**
   * @see Collections#binarySearch(java.util.List, java.lang.Object)
   */
  public static int binarySearch(IntList list, int key) {
    int low = 0;
    int high = list.size() - 1;

    while (low <= high) {
      int mid = (low + high) >> 1;
      int midVal = list.get(mid);
      int cmp = midVal - key;

      if (cmp < 0)
        low = mid + 1;
      else if (cmp > 0)
        high = mid - 1;
      else
        return mid; // key found
    }
    return -(low + 1); // key not found
  }

  public static boolean wasFound(int x) {
    return x >= 0;
  }

  public static int insertionPoint(int x) {
    assert (x < 0);

    return (-x) - 1;
  }
}