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

import java.io.IOException;
import java.util.Iterator;
import java.util.regex.Pattern;

import jline.Terminal;

import com.baulsupp.kolja.ansi.commands.CommandList;
import com.baulsupp.kolja.ansi.commands.HelpCommand;
import com.baulsupp.kolja.ansi.commands.PauseCommand;
import com.baulsupp.kolja.ansi.commands.QuitCommand;
import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.viewer.highlight.BasicSearchHighlight;
import com.baulsupp.kolja.log.viewer.renderer.FieldRenderer;
import com.baulsupp.kolja.log.viewer.renderer.Renderer;
import com.baulsupp.kolja.util.colours.ColourPair;

public class Cat {
  protected boolean ansi;

  protected Renderer<Line> grid;

  protected Iterator<Line> i;

  private volatile boolean isQuit = false;

  protected volatile boolean isPaused = false;

  protected TailRenderer renderer;

  protected CommandList commands = new CommandList();

  public Cat() {
    createDefaultCommands();
  }

  public boolean isAnsi() {
    return ansi;
  }

  public void setAnsi(boolean ansi) {
    this.ansi = ansi;
  }

  public boolean isQuit() {
    return isQuit;
  }

  public void setQuit(boolean isQuit) {
    this.isQuit = isQuit;
  }

  public boolean isPaused() {
    return isPaused;
  }

  public void quit() {
    this.isQuit = true;
  }

  public void setPaused(boolean paused) {
    this.isPaused = paused;
  }

  protected boolean isRunning() {
    return !(isPaused || isQuit);
  }

  protected void processInput() throws IOException {
    while (!isQuit()) {
      String c = readCommand();

      commands.run(c, this);
    }
  }

  private String readCommand() throws IOException {
    Terminal t = Terminal.getTerminal();

    return String.valueOf((char) t.readVirtualKey(System.in));
  }

  public void run() throws InterruptedException, IOException {
    while (true) {
      while (isRunning() && i.hasNext()) {
        showNextLine();
      }

      if (isEnd()) {
        return;
      }

      if (isPaused) {
        Thread.sleep(200);
      }
    }
  }

  protected boolean isEnd() {
    return isQuit || !i.hasNext();
  }

  protected void showNextLine() {
    Line l = i.next();
    renderer.show(l);
  }

  public void setI(Iterator<Line> i) {
    this.i = i;
  }

  public void setGrid(Renderer<Line> grid) {
    this.grid = grid;
    grid.setWidth(AnsiUtils.getWidth());
    renderer = new TailRenderer(grid, ansi);
  }

  public void addHighlightTerm(String highlightTerm) {
    if (grid instanceof FieldRenderer) {
      BasicSearchHighlight basicSearchHighlight = new BasicSearchHighlight(ColourPair.RED_ON_BLACK);
      basicSearchHighlight.setPattern(Pattern.compile(highlightTerm));
      ((FieldRenderer) grid).addHighlight(basicSearchHighlight);
    }
  }

  public void setRenderer(Renderer<Line> renderer) {
    this.grid = renderer;

    grid.setWidth(AnsiUtils.getWidth());

    this.renderer = new TailRenderer(grid, ansi);
  }

  public void setFixedWidth(boolean b) {
    renderer.setFixedWidth(b);
  }

  protected void createDefaultCommands() {
    commands.add("h", new HelpCommand());
    commands.add(" ", new PauseCommand());
    commands.add("q", new QuitCommand());
  }

  public CommandList getCommands() {
    return commands;
  }
}
