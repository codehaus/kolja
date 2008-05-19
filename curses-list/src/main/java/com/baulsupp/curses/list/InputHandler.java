package com.baulsupp.curses.list;

import jcurses.system.InputChar;

public interface InputHandler {
  boolean handleInput(InputChar inp);
}
