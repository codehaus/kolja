package com.baulsupp.kolja.ansi;

import java.awt.Color;
import java.util.List;

import jline.ANSIBuffer;

import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.viewer.renderer.Renderer;
import com.baulsupp.kolja.log.viewer.renderer.TextDisplayRow;
import com.baulsupp.kolja.util.ColouredString;
import com.baulsupp.kolja.util.MultiColourString;

public class TailRenderer implements ConsoleRenderer<Line> {
  private Renderer<Line> renderer;

  private boolean ansi;

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
    if (string.getForegroundColor() == Color.BLUE) {
      buffy.blue(string.toString());
    } else if (string.getForegroundColor() == Color.RED) {
      buffy.red(string.toString());
    } else if (string.getForegroundColor() == Color.MAGENTA) {
      buffy.magenta(string.toString());
    } else if (string.getForegroundColor() == Color.GREEN) {
      buffy.green(string.toString());
    } else {
      buffy.append(string.toString());
    }
  }
}
