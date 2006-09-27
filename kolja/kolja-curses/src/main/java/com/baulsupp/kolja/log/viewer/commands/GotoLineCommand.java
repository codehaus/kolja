package com.baulsupp.kolja.log.viewer.commands;

import java.util.Collection;
import java.util.Collections;

import jcurses.system.InputChar;

import com.baulsupp.curses.application.Command;
import com.baulsupp.curses.application.KeyBinding;
import com.baulsupp.curses.list.TextDialog;
import com.baulsupp.curses.list.Util;
import com.baulsupp.less.Less;

public class GotoLineCommand implements Command<Less> {
  public boolean handle(Less less, InputChar input) {
    if (!Util.wasLetter(input, 'h')) {
      return false;
    }
    
    String a = TextDialog.getValue("Goto Line");

    if (a != null) {
        if (a.equals("")) {
          less.end();
        } else {
          int line = Integer.parseInt(a);
          less.moveTo(less.getLineNumbers().offset(line - 1));
        }
    }
    
    return true;
  }

  public Collection<KeyBinding> getDescription() {
    return Collections.singleton(new KeyBinding(new InputChar('g'), "Movement", "Goto Line"));
  }
}
