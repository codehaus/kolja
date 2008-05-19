package com.baulsupp.kolja.ansi.commands;

import com.baulsupp.kolja.ansi.Cat;

public class HelpCommand implements Command {
  public void execute(Cat cat) {
    cat.getCommands().print(System.out);
  }

  public String getDescription() {
    return "Help";
  }
}
