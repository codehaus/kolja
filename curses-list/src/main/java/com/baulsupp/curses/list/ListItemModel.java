package com.baulsupp.curses.list;

import java.util.List;
import java.util.ListIterator;

public class ListItemModel<T> implements ItemModel<T> {
  private List<T> list;

  private ListIterator<T> iterator;

  int position = 0;

  private int lastPosition;

  public ListItemModel(List<T> list) {
    this.list = list;
    this.iterator = list.listIterator();
  }

  public void moveTo(int position) {
    lastPosition = -1;
    this.position = position;
    iterator = list.listIterator(this.position);
  }

  public boolean hasNext() {
    return iterator.hasNext();
  }

  public T next() {
    T result = iterator.next();

    lastPosition = position;
    position++;

    return result;
  }

  public boolean hasPrevious() {
    return iterator.hasPrevious();
  }

  public T previous() {
    T result = iterator.previous();

    position--;
    lastPosition = position;

    return result;
  }

  public void moveToEnd() {
    moveTo(list.size());
  }

  public void moveToStart() {
    moveTo(0);
  }

  public int lastPosition() {
    return lastPosition;
  }

  public void addItemModelListener(ItemModelListener listener) {
  }

  public void removeItemModelListener(ItemModelListener listener) {
  }
}
