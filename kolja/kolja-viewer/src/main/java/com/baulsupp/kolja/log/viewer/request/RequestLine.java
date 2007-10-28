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
package com.baulsupp.kolja.log.viewer.request;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.primitives.IntIterator;
import org.apache.commons.collections.primitives.IntList;

import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.util.FastIntList;

public class RequestLine implements Line {
  private int offset;

  private boolean foundStart = false;

  private boolean foundEnd = false;

  private IntList lineOffsets = new FastIntList(50);

  private HashMap<String, Object> values = new HashMap<String, Object>();
  private Object identifier = null;
  private String statusField;

  public RequestLine(Object identifier, String statusField) {
    this.identifier = identifier;
    this.statusField = statusField;
  }

  public void addLine(Line line) {
    // lineJoiner = null;

    // lines.add(line);
    lineOffsets.add(line.getOffset());
  }

  public Object getIdentifier() {
    return identifier;
  }

  public String getStatus() {
    Object value = statusField != null ? getValue(statusField) : identifier;

    return String.valueOf(value);
  }

  public Object getValue(String name) {
    return values.get(name);
  }

  public Object[] getValues(String[] fields) {
    Object[] values = new Object[fields.length];

    for (int i = 0; i < fields.length; i++) {
      values[i] = getValue(fields[i]);
    }

    return values;
  }

  public void setValue(String field, Object value) {
    values.put(field, value);
  }

  public boolean isFailed() {
    return false;
  }

  public int getOffset() {
    return offset;
  }

  public void setOffset(int lineStart) {
    this.offset = lineStart;
  }

  public int length() {
    return statusField.length();
  }

  public char charAt(int arg0) {
    return statusField.charAt(arg0);
  }

  public CharSequence subSequence(int arg0, int arg1) {
    return statusField.subSequence(arg0, arg1);
  }

  @Override
  public String toString() {
    return statusField;
  }

  public boolean isComplete() {
    return isStartFound() && isEndFound();
  }

  public boolean isEndFound() {
    return foundEnd;
  }

  public boolean isStartFound() {
    return foundStart;
  }

  public void setStartFound(boolean b) {
    this.foundStart = b;
  }

  public void setEndFound(boolean b) {
    this.foundEnd = b;
  }

  public Map<String, Object> getValues() {
    return values;
  }

  public int getMinimumKnownOffset() {
    int minimum = lineOffsets.get(0);

    IntIterator i = lineOffsets.iterator();
    while (i.hasNext()) {
      minimum = Math.min(minimum, i.next());
    }

    return minimum;
  }

  public boolean isRelevantForOffset(int offset) {
    return lineOffsets.contains(offset);
  }
}
