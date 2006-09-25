package com.baulsupp.curses.application;

import jcurses.system.InputChar;

import com.baulsupp.curses.list.CursesListWindow;
import com.baulsupp.curses.list.Util;

public class ListMovementCommand implements Command<CursesListWindow> {
  public boolean handle(CursesListWindow less, InputChar inp) {
    if (Util.isCode(inp, InputChar.KEY_DOWN, InputChar.KEY_UP, InputChar.KEY_NPAGE, InputChar.KEY_PPAGE,
        InputChar.KEY_RIGHT, InputChar.KEY_LEFT, InputChar.KEY_END, InputChar.KEY_HOME)
        || Util.wasCtrlR(inp)) {

      if (less.list.isReady()) {
        if (inp.getCode() == InputChar.KEY_DOWN) {
          less.list.down();
        } else if (inp.getCode() == InputChar.KEY_UP) {
          less.list.up();
        } else if (inp.getCode() == InputChar.KEY_NPAGE) {
          less.list.pageDown();
        } else if (inp.getCode() == InputChar.KEY_PPAGE) {
          less.list.pageUp();
        } else if (Util.wasCtrlR(inp)) {
          less.list.refresh();
        } else if (inp.getCode() == InputChar.KEY_RIGHT) {
          less.list.right();
        } else if (inp.getCode() == InputChar.KEY_LEFT) {
          less.list.left();
        } else if (inp.getCode() == InputChar.KEY_END) {
          less.list.end();
        } else if (inp.getCode() == InputChar.KEY_HOME) {
          less.list.home();
        }
      }

      return true;
    }

    return false;
  }

  public String getDescription() {
    return "Movement Commands";
  }
}
