package com.baulsupp.kolja.ansi;

import java.io.IOException;
import java.util.Iterator;
import java.util.regex.Pattern;

import jline.Terminal;

import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.viewer.highlight.BasicSearchHighlight;
import com.baulsupp.kolja.log.viewer.renderer.FieldRenderer;
import com.baulsupp.kolja.log.viewer.renderer.Renderer;
import com.baulsupp.kolja.util.ColourPair;

public class Cat {
  protected boolean ansi;

  // protected boolean byRequest;

  protected Renderer<Line> grid;

  protected Iterator<Line> i;

  private volatile boolean isQuit = false;

  protected volatile boolean isPaused = false;

  protected TailRenderer renderer;

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

  private boolean isPaused() {
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
    while (true) {
      String c = readCommand();

      if (c.equals("q")) {
        quit();
        break;
      } else if (c.equals(" ")) {
        setPaused(!isPaused());
      }
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

  public void load(Iterator<Line> bli, Renderer<Line> grid) {
    this.i = bli;

    this.grid = grid;

    grid.setWidth(Terminal.getTerminal().getTerminalWidth());

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

    grid.setWidth(Terminal.getTerminal().getTerminalWidth());

    this.renderer = new TailRenderer(grid, ansi);
  }

  public void setFixedWidth(boolean b) {
    renderer.setFixedWidth(b);
  }
}
