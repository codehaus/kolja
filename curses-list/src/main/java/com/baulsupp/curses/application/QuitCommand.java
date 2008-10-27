package com.baulsupp.curses.application;

import java.util.Collection;
import java.util.Collections;

import jcurses.system.InputChar;
import jcurses.system.Toolkit;

import com.baulsupp.curses.list.ColorList;
import com.baulsupp.curses.list.CursesListWindow;
import com.baulsupp.curses.list.Util;

public class QuitCommand implements Command<CursesListWindow<?, ?>> {
  public boolean handle(CursesListWindow<?, ?> less, InputChar input) {
    if (!Util.wasLetter(input, 'q')) {
      return false;
    }

    Toolkit.clearScreen(ColorList.blackOnWhite);
    Toolkit.shutdown();
    System.exit(-1);
    return true;
  }

  public Collection<KeyBinding> getDescription() {
    return Collections.singleton(new KeyBinding(new InputChar('q'), "General", "Quit"));
  }
}
