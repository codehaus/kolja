package com.baulsupp.curses.list;

public interface ICursesList {
  ItemModel getModel();

  void setModel(ItemModel model);

  CursesListRenderer getRenderer();

  void setRenderer(CursesListRenderer renderer);

  void reset();

  void up();

  void down();

  void moveTo(int offset);

  void pageDown();

  void pageUp();

  // TODO generalise this to a) move to model position B) reset b) load up/down
  void end();

  void home();

  void refresh();
}