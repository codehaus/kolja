package com.baulsupp.kolja.ansi.commands;

import com.baulsupp.kolja.ansi.Cat;

public interface Command {
  void execute(Cat cat);

  String getDescription();
}
