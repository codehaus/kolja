package com.baulsupp.curses.application;

import jcurses.system.InputChar;
import jcurses.util.Message;

import com.baulsupp.curses.list.CursesListWindow;
import com.baulsupp.curses.list.Util;

public class HelpCommand implements Command<CursesListWindow> {
  public boolean handle(CursesListWindow less, InputChar input) {
    if (!Util.wasLetter(input, 'h')) {
      return false;
    }

    showHelp(less.getCommands());
    
    return true;
  }

  public String getDescription() {
    return "h - Help";
  }
  
  void showHelp(CommandList list) {
    new Message("Help", "Help\nUse arrow keys to move.  q to quit.", "ok").show();
  }
}
