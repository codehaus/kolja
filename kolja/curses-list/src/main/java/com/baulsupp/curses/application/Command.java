package com.baulsupp.curses.application;

import jcurses.system.InputChar;

public interface Command<T> {
  boolean handle(T app, InputChar input);

  String getDescription();
}
