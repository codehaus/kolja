package com.baulsupp.curses.application;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jcurses.system.InputChar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandList<T> {
  private static final Logger logger = LoggerFactory.getLogger(CommandList.class);
  
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

  public Map<String, Collection<KeyBinding>> getBindings() {
    Map<String, Collection<KeyBinding>> result = new HashMap<String, Collection<KeyBinding>>();

    for (Command<? super T> c : commands) {
      for (KeyBinding key : c.getDescription()) {
        Collection<KeyBinding> group = result.get(key.getCategory());
        
        if (group == null) {
          group = new ArrayList<KeyBinding>();
          result.put(key.getCategory(), group);
        }
        
        group.add(key);
      }
    }
    
    logger.info("commands " + result);
    
    return result;
  }  
}
