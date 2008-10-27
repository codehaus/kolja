package com.baulsupp.kolja.ansi.commands;

import java.io.PrintStream;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.baulsupp.kolja.ansi.Cat;

public class CommandList {
  private SortedMap<String, Command> commands = new TreeMap<String, Command>();

  public void print(PrintStream out) {
    System.out.println("Commands");
    System.out.println("========");
    for (Map.Entry<String, Command> e : commands.entrySet()) {
      System.out.println(e.getKey() + " - " + e.getValue().getDescription());
    }
  }

  public void add(String c, Command command) {
    commands.put(c, command);
  }

  public void run(String c, Cat cat) {
    Command command = commands.get(c);
    
    if (command != null) {
      command.execute(cat);
    }
  }  
}
