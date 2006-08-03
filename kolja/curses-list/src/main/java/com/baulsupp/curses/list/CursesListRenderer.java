package com.baulsupp.curses.list;

public interface CursesListRenderer<T> {
  DisplayRow getRow(T item);
}
