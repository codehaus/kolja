package com.baulsupp.curses.list;

public interface ItemModel<T> {
  void moveTo(int position);

  boolean hasNext();

  T next();

  int lastPosition();

  boolean hasPrevious();

  T previous();

  void moveToEnd();

  void moveToStart();

  void addItemModelListener(ItemModelListener listener);

  void removeItemModelListener(ItemModelListener listener);
}
