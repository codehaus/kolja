package com.baulsupp.curses.application;

import java.util.ArrayList;
import java.util.List;

import jcurses.system.InputChar;

public class CommandList<T> {
  private List<Command<? super T>> commands = new ArrayList<Command<? super T>>();

  public void print() {
    for (Command<? super T> e : commands) {
      System.out.println(e.getDescription());
    }
  }

  public void add(Command<T> command) {
    commands.add(command);
  }

  public boolean run(InputChar inp, T cat) {
    for (Command<? super T> e : commands) {
      if (e.handle(cat, inp)) {
        return true;
      }
    }
    
    return false;
  }  
}
