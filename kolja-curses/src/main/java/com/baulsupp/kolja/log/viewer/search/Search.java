package com.baulsupp.kolja.log.viewer.search;

import com.baulsupp.curses.list.ItemModel;

public interface Search {
  void setModel(ItemModel model);

  int previous(int i);

  int next(int i);
}