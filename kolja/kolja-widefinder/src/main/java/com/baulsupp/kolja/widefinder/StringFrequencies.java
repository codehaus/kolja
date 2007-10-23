/**
 * Copyright (c) 2002-2007 Yuri Schimke. All Rights Reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package com.baulsupp.kolja.widefinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.collections.ComparatorUtils;

/**
 * Frequency Counting of Strings.
 * 
 * @author Yuri Schimke
 */
public class StringFrequencies implements Iterable<StringFrequencies.Count> {
  private SortedMap<String, Count> counts = new TreeMap<String, Count>();

  public static final Comparator<Count> COUNT_COMPARATOR = new Comparator<Count>() {
    public int compare(Count c1, Count c2) {
      if (c1.getCount() < c2.getCount()) {
        return -1;
      } else if (c1.getCount() == c2.getCount()) {
        return 0;
      } else {
        return 1;
      }
    }
  };

  public void increment(String url) {
    Count count = counts.get(url);

    if (count == null) {
      counts.put(url, new Count(url, 1));
    } else {
      count.increment();
    }
  }

  public class Count {
    private String url;
    private int i;

    public Count(String url, int i) {
      this.url = url;
      this.i = i;
    }

    public void increment() {
      this.i++;
    }

    @Override
    public String toString() {
      return i + " " + url;
    }

    public int getCount() {
      return i;
    }
  }

  @SuppressWarnings("unchecked")
  public List<Count> getMostFrequent(int top) {
    List<Count> results = new ArrayList<Count>();

    results.addAll(counts.values());

    Collections.sort(results, ComparatorUtils.reversedComparator(COUNT_COMPARATOR));

    if (results.size() > top) {
      results = results.subList(0, top);
    }

    return results;
  }

  public Iterator<Count> iterator() {
    return counts.values().iterator();
  }

  public int size() {
    return counts.size();
  }

  public int get(String string) {
    Count c = counts.get(string);
    return c != null ? c.getCount() : 0;
  }
}
