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
package com.baulsupp.kolja.ansi;

import java.util.List;

import jline.ANSIBuffer;
import jline.Terminal;

import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.viewer.renderer.Renderer;
import com.baulsupp.kolja.log.viewer.renderer.TextDisplayRow;
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

      AnsiUtils.append(buffy, string);
      buffy.append("\n");
    }

    if (ansi) {
      System.out.print(buffy.getAnsiBuffer());
    } else {
      System.out.print(buffy.getPlainBuffer());
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
