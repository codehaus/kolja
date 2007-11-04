/**
 * Copyright (c) 2002-2007 Yuri Schimke. All Rights Reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
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
      if (i > 0 || !text.isEmpty()) {
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
