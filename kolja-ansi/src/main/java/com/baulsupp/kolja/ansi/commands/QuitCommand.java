package com.baulsupp.kolja.ansi.commands;

import com.baulsupp.kolja.ansi.Cat;

public class QuitCommand implements Command {
  public void execute(Cat cat) {
    cat.quit();
  }

  public String getDescription() {
    return "Quit";
  }
}
