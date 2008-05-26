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

import jline.ANSIBuffer;
import jline.Terminal;

import com.baulsupp.kolja.ansi.AnsiUtils;
import com.baulsupp.kolja.ansi.ConsoleRenderer;
import com.baulsupp.kolja.ansi.TailRenderer;
import com.baulsupp.kolja.ansi.progress.ProgressBar;
import com.baulsupp.kolja.ansi.reports.engine.ReportEngine;
import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.viewer.importing.LogFormat;
import com.baulsupp.kolja.log.viewer.renderer.Renderer;
import com.baulsupp.kolja.log.viewer.request.RequestLine;
import com.baulsupp.kolja.util.colours.MultiColourString;

// TODO implement progress
public class AnsiReportPrinter implements ReportPrinter {

  protected ConsoleRenderer<Line> lineRenderer;

  protected ConsoleRenderer<Line> requestRenderer;

  private ProgressBar progress;

  private boolean interactive = false;

  protected boolean ansi = true;

  private boolean fixedWidth = false;

  public AnsiReportPrinter() {
  }

  public String getName() {
    return "console";
  }

  public void setInteractive(boolean interactive) {
    this.interactive = interactive;
  }

  public void setAnsi(boolean ansi) {
    this.ansi = ansi;
  }

  public void setFixedWidth(boolean fixedWidth) {
    this.fixedWidth = fixedWidth;
  }

  public void setLineRenderer(ConsoleRenderer<Line> lineRenderer) {
    this.lineRenderer = lineRenderer;
  }

  public void setRequestRenderer(ConsoleRenderer<Line> requestRenderer) {
    this.requestRenderer = requestRenderer;
  }

  public void initialise(LogFormat format) throws IOException {
    Renderer<Line> renderer = format.getLineRenderer();
    ConsoleRenderer<Line> lineRenderer = createRenderer(renderer, ansi, fixedWidth);
    setLineRenderer(lineRenderer);

    renderer = format.getRequestRenderer();
    ConsoleRenderer<Line> requestRenderer = createRenderer(renderer, ansi, fixedWidth);
    setRequestRenderer(requestRenderer);

    progress = AnsiUtils.getProgressBar(interactive);
  }

  private static ConsoleRenderer<Line> createRenderer(Renderer<Line> renderer, boolean ansi, boolean fixedWidth) {
    renderer.setWidth(Terminal.getTerminal().getTerminalWidth());
    TailRenderer tr = new TailRenderer(renderer, ansi);
    tr.setFixedWidth(fixedWidth);
    return tr;
  }

  // @Override
  // protected void iterateThroughFile(File f, BasicLineIterator i) {
  // long total = f.length();
  //
  // while (i.hasNext()) {
  // Line l = i.next();
  //
  // progress.showProgress(l.getOffset(), total);
  //
  // processLine(l);
  // }
  //
  // progress.clear();
  // }

  public void printLine() {
    progress.clear();

    System.out.println();
  }

  public void printTitle(String title) {
    progress.clear();

    System.out.println(title);
  }

  public void println(MultiColourString string) {
    progress.clear();

    ANSIBuffer buffy = new ANSIBuffer();

    AnsiUtils.append(buffy, string);
    buffy.append("\n");

    if (ansi) {
      System.out.print(buffy.getAnsiBuffer());
    } else {
      System.out.print(buffy.getPlainBuffer());
    }
  }

  public void printLine(Line line) {
    progress.clear();

    lineRenderer.show(line);
  }

  public void printRequest(RequestLine request) {
    progress.clear();

    requestRenderer.show(request);
  }

  public void setReportEngine(ReportEngine reportEngine) {
  }

  public void completed() throws IOException {
    progress.clear();
  }
}
