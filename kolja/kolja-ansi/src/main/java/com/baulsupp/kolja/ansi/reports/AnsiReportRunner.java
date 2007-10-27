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
package com.baulsupp.kolja.ansi.reports;

import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;

import jline.ANSIBuffer;
import jline.Terminal;

import com.baulsupp.kolja.ansi.AnsiUtils;
import com.baulsupp.kolja.ansi.TailRenderer;
import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.viewer.renderer.Renderer;
import com.baulsupp.kolja.util.colours.MultiColourString;

public class AnsiReportRunner implements ReportRunner {
  protected boolean ansi;

  protected Renderer<Line> grid;

  protected Iterator<Line> i;

  protected TailRenderer renderer;

  protected java.util.List<TextReport> reports;

  public AnsiReportRunner() {
  }

  public void setAnsi(boolean ansi) {
    this.ansi = ansi;
  }

  public void setReports(java.util.List<TextReport> reports) {
    this.reports = reports;
  }

  public void run() throws InterruptedException, IOException {
    initialise();

    while (i.hasNext()) {
      Line l = i.next();

      processLine(l);
    }

    completed();

    display();
  }

  private void initialise() {
    for (TextReport r : reports) {
      r.initialise(this);
    }
  }

  private void completed() {
    for (TextReport r : reports) {
      r.completed();
    }
  }

  private void display() {
    boolean hasMultiple = reports.size() > 1;

    boolean first = true;

    for (TextReport r : reports) {
      if (first) {
        first = false;
      } else {
        println(new MultiColourString());
      }

      r.display(hasMultiple);
    }
  }

  private void processLine(Line line) {
    for (TextReport r : reports) {
      r.processLine(line);
    }
  }

  public void setI(Iterator<Line> i) {
    this.i = i;
  }

  public void setGrid(Renderer<Line> grid) {
    this.grid = grid;
    grid.setWidth(Terminal.getTerminal().getTerminalWidth());
    renderer = new TailRenderer(grid, ansi);
  }

  public void setRenderer(Renderer<Line> renderer) {
    this.grid = renderer;

    grid.setWidth(Terminal.getTerminal().getTerminalWidth());

    this.renderer = new TailRenderer(grid, ansi);
  }

  public void setFixedWidth(boolean b) {
    renderer.setFixedWidth(b);
  }

  public void println(MultiColourString string) {
    ANSIBuffer buffy = new ANSIBuffer();

    AnsiUtils.append(buffy, string);
    buffy.append("\n");

    if (ansi) {
      System.out.print(buffy.getAnsiBuffer());
    } else {
      System.out.print(buffy.getPlainBuffer());
    }
  }

  public void println(Line line) {
    renderer.show(line);
  }
}
