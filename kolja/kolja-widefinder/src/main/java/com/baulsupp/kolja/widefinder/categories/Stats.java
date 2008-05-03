/**
 * Copyright (c) 2002-2007 Yuri Schimke. All Rights Reserved.
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package com.baulsupp.kolja.widefinder.categories;

import java.util.Comparator;

import com.baulsupp.kolja.log.viewer.format.BytesFormat;
import com.baulsupp.kolja.util.BaseComparator;

/**
 * @author Yuri Schimke
 */
public class Stats {
  public static final Comparator<Stats> BY_SIZE = new BaseComparator<Stats>() {
    private static final long serialVersionUID = 2667379143660344536L;

    public int compare(Stats o1, Stats o2) {
      long thisVal = o1.getBytes();
      long anotherVal = o2.getBytes();
      return (thisVal < anotherVal ? -1 : (thisVal == anotherVal ? 0 : 1));
    }
  };

  private String type;
  private long bytes = 0;
  private int count = 0;

  public Stats(String type) {
    this.type = type;
  }

  /**
   * @param bytes2
   */
  public void addBytes(Long bytes) {
    if (bytes != null) {
      this.bytes += bytes;
    }

    count++;
  }

  @Override
  public String toString() {
    return type + ": " + BytesFormat.formatBytes(bytes) + " " + count + " hits";
  }

  public long getBytes() {
    return bytes;
  }

  public int getCount() {
    return count;
  }

  public String getType() {
    return type;
  }

}
