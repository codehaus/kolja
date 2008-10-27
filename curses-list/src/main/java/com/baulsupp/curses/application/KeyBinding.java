package com.baulsupp.curses.application;

import jcurses.system.InputChar;

public class KeyBinding {
  private InputChar inputChar;

  private String category;

  private String command;
  
  public KeyBinding(InputChar inputChar, String category, String command) {
    this.inputChar = inputChar;
    this.category = category;
    this.command = command;
  }

  public String getCategory() {
    return category;
  }

  public String getCommand() {
    return command;
  }

  public InputChar getInputChar() {
    return inputChar;
  }
  
  @Override
  public String toString() {
    return inputChar + " - " + command;
  }
}
