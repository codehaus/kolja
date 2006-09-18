package com.baulsupp.kolja.ansi;

import java.util.List;

import jline.ANSIBuffer;
import jline.Terminal;

import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.viewer.renderer.Renderer;
import com.baulsupp.kolja.log.viewer.renderer.TextDisplayRow;
import com.baulsupp.kolja.util.colours.Colour;
import com.baulsupp.kolja.util.colours.ColouredString;
import com.baulsupp.kolja.util.colours.MultiColourString;

public class TailRenderer implements ConsoleRenderer<Line> {
  private Renderer<Line> renderer;

  private boolean ansi;

  private int fixedWidth;

  public TailRenderer(Renderer<Line> renderer, boolean ansi) {
    this.renderer = renderer;
    this.ansi = ansi;
  }
  
  public void show(Line l) {
    TextDisplayRow row = renderer.getRow(l);  
    print(row);
  }

  private void print(TextDisplayRow row) {
    ANSIBuffer buffy = new ANSIBuffer();
    
    List<MultiColourString> lines = row.getLines();
    
    for (MultiColourString string : lines) {
      if (fixedWidth > 0 && string.length() > fixedWidth) {
        string = string.part(0, fixedWidth);
      }
      
      append(buffy, string);
      buffy.append("\n");
    }
    
    if (ansi) {
      System.out.print(buffy.getAnsiBuffer());
    } else {
      System.out.print(buffy.getPlainBuffer());
    }
  }
  
  private void append(ANSIBuffer buffy, MultiColourString string) {
    for (ColouredString s : string.getColouredStrings()) {
      append(buffy, s);
    }
  }

  private void append(ANSIBuffer buffy, ColouredString string) {
    if (string.getForegroundColor() == Colour.BLUE) {
      buffy.blue(string.toString());
    } else if (string.getForegroundColor() == Colour.RED) {
      buffy.red(string.toString());
    } else if (string.getForegroundColor() == Colour.MAGENTA) {
      buffy.magenta(string.toString());
    } else if (string.getForegroundColor() == Colour.GREEN) {
      buffy.green(string.toString());
    } else if (string.getForegroundColor() == Colour.WHITE) {
      buffy.yellow(string.toString());
    } else if (string.getForegroundColor() == Colour.BLACK) {
      buffy.black(string.toString());
    } else if (string.getForegroundColor() == Colour.CYAN) {
      buffy.cyan(string.toString());
    } else {
      buffy.append(string.toString());
    }
  }

  public void setFixedWidth(boolean b) {
    if (b) {
      this.fixedWidth = Terminal.getTerminal().getTerminalWidth();
    } else {
      this.fixedWidth = 0;
    }
  }
}
