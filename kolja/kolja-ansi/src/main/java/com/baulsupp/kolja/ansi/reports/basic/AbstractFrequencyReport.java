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
package com.baulsupp.kolja.ansi.reports.basic;

import java.util.List;
import java.util.SortedMap;

import com.baulsupp.kolja.ansi.reports.AbstractTextReport;
import com.baulsupp.kolja.ansi.reports.basic.Frequencies.Count;
import com.baulsupp.kolja.log.line.Line;

public abstract class AbstractFrequencyReport<T> extends AbstractTextReport {
  private Frequencies<T> counts;
  protected Integer count = null;

  public AbstractFrequencyReport() {
    counts = new Frequencies<T>();
  }

  public AbstractFrequencyReport(SortedMap<T, Count<T>> map) {
    counts = new Frequencies<T>(map);
  }

  @Override
  public void processLine(Line line) {
    T value = getValue(line);

    increment(value);
  }

  protected abstract T getValue(Line line);

  protected void increment(T t) {
    counts.increment(t);
  }

  @Override
  public void completed() {
    printTitle();

    if (count != null) {
      displayMostFrequent();
    } else {
      displayFrequencies();
    }
  }

  public void displayFrequencies() {
    for (Count<T> c : getFrequencies()) {
      println(toString(c.getItem()) + " " + c.getCount());
    }
  }

  protected String toString(T item) {
    return String.valueOf(item);
  }

  public void displayMostFrequent() {
    for (Count<T> c : getMostFrequent(count)) {
      println(c.getCount() + " " + toString(c.getItem()));
    }
  }

  public void setCount(Integer count) {
    this.count = count;
  }

  public Frequencies<T> getFrequencies() {
    return counts;
  }

  public List<Count<T>> getMostFrequent(int urlCount) {
    return counts.getMostFrequent(urlCount);
  }

}
