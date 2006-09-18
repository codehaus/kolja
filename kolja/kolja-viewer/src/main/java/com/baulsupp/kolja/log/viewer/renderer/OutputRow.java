package com.baulsupp.kolja.log.viewer.renderer;

import java.util.ArrayList;
import java.util.List;

import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.util.colours.ColourPair;
import com.baulsupp.kolja.util.colours.ColouredString;
import com.baulsupp.kolja.util.colours.MultiColourString;

public class OutputRow implements TextDisplayRow {
  protected List<MultiColourString> text = new ArrayList<MultiColourString>(10);

  protected Line line;

  public OutputRow(Line viewRow) {
    this.line = viewRow;
    text.add(new MultiColourString());
  }
  
  public void newLine() {
    text.add(new MultiColourString());
  }
  
  public void append(ColouredString string) {
    MultiColourString s = text.get(text.size() - 1);
    s.append(string);
  }

  public int getHeight() {
    return getLines().size();
  }

  public void setLine(Line line) {
    this.line = line;
  }

  public int getOffset() {
    return line.getOffset();
  }

  public List<MultiColourString> getLines() {
    return text;
  }

  public void appendLines(List<String> lines) {
    for (int i = 0; i < lines.size(); i++) {
      if (i > 0) {
        newLine();
      }

      append(new ColouredString(lines.get(i)));
    }
  }

  public void append(String string) {
    append(new ColouredString(string));
  }

  public void append(ColourPair columnColour, String string) {
    append(new ColouredString(columnColour, string));
  }
  
  public void append(MultiColourString string) {    
    MultiColourString s = text.get(text.size() - 1);
    s.append(string);
  }

  public void appendColouredLines(List<MultiColourString> lines) {
    for (int i = 0; i < lines.size(); i++) {
      if (i > 0) {
        newLine();
      }

      append(lines.get(i));
    }
  }

  public void appendLines(ColourPair columnColour, String[] lines) {
    for (int i = 0; i < lines.length; i++) {
      if (i > 0) {
        newLine();
      }

      append(new ColouredString(columnColour, lines[i]));
    }
  }
}
