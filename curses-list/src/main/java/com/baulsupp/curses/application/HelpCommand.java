package com.baulsupp.curses.application;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

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

  public Collection<KeyBinding> getDescription() {
    return Collections.singleton(new KeyBinding(new InputChar('h'), "General", "Help"));
  }
  
  void showHelp(CommandList<?> list) {
    Map<String, Collection<KeyBinding>> bindings = list.getBindings();
    
    StringBuilder buffy = new StringBuilder();
    
    for (Map.Entry<String, Collection<KeyBinding>> e : bindings.entrySet()) {
      buffy.append("    " + e.getKey() + "\n");
      buffy.append("    " + underlines(e.getKey().length()) + "\n");
      
      for (KeyBinding key : e.getValue()) {
        buffy.append(key + "\n");        
      }
      
      buffy.append("\n");
    }    
    
    new Message("Help", buffy.toString(), "ok").show();
  }

  private String underlines(int i) {
    return "--------------------".substring(0, Math.min(20, i));
  }
}
