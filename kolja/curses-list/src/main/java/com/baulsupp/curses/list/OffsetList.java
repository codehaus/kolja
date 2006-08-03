/**
 * $Source$ Copyright (c) 2004 Yuri Schimke. All rights reserved.
 */
package com.baulsupp.curses.list;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * TODO: Document me!
 * 
 * @author <a href="mailto:yschimke@aspecthuntley.com.au">Yuri Schimke</a>
 * @version $Revision$
 */
public class OffsetList implements Iterable<DisplayRow> {
  private LinkedList<DisplayRow> rows = new LinkedList<DisplayRow>();

  private LinkedList<Integer> offsets = new LinkedList<Integer>();

  public boolean isEmpty() {
    return rows.isEmpty();
  }

  public Iterator<DisplayRow> iterator() {
    return rows.iterator();
  }

  public int rowCount() {
    return rows.size();
  }

  public DisplayRow getLastRow() {
    return rows.getLast();
  }

  public DisplayRow getFirstRow() {
    return rows.getFirst();
  }

  public void removeFirst() {
    rows.removeFirst();
    offsets.removeFirst();
  }

  public void removeLast() {
    rows.removeLast();
    offsets.removeLast();
  }

  public int getMinRowOffset() {
    try {
      Integer i = offsets.getFirst();
      return i.intValue();
    } catch (NoSuchElementException nsee) {
      return 0;
    }
  }

  public int getMaxRowOffset() {
    try {
      Integer i = offsets.getLast();
      return i.intValue();
    } catch (NoSuchElementException nsee) {
      return 0;
    }
  }

  public void addFirst(DisplayRow r, int i) {
    rows.addFirst(r);
    offsets.addFirst(new Integer(i));
  }

  public void addLast(DisplayRow r, int i) {
    rows.addLast(r);
    offsets.addLast(new Integer(i));
  }

  public void clear() {
    rows.clear();
    offsets.clear();
  }
}
