package com.baulsupp.kolja.ansi.commands;

import com.baulsupp.kolja.ansi.Cat;

public class PauseCommand implements Command {
  public void execute(Cat cat) {
    if (cat.isPaused()) {
      System.out.println("<Resumed>");
      cat.setPaused(false);
    } else {
      cat.setPaused(true);
      System.out.println("<Paused>");
    }    
  }

  public String getDescription() {
    return "Pause";
  }
}
