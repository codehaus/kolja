package com.baulsupp.curses.application;

import java.util.Collection;

import jcurses.system.InputChar;

public interface Command<T> {
  boolean handle(T app, InputChar input);

  Collection<KeyBinding> getDescription();
}
