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

import com.baulsupp.kolja.ansi.AnsiUtils;
import com.baulsupp.kolja.ansi.ConsoleRenderer;
import com.baulsupp.kolja.ansi.progress.ProgressBar;
import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.viewer.request.RequestLine;
import com.baulsupp.kolja.util.colours.MultiColourString;

// TODO implement progress
public class AnsiReportPrinter implements ReportPrinter {
  protected boolean ansi;

  protected ConsoleRenderer<Line> lineRenderer;

  protected ConsoleRenderer<Line> requestRenderer;

  private ProgressBar progress;

  private boolean interactive = false;

  @SuppressWarnings("unused")
  private ReportEngine reportEngine;

  public AnsiReportPrinter() {
  }

  public void setInteractive(boolean interactive) {
    this.interactive = interactive;
  }

  public void setLineRenderer(ConsoleRenderer<Line> lineRenderer) {
    this.lineRenderer = lineRenderer;
  }

  public void setRequestRenderer(ConsoleRenderer<Line> requestRenderer) {
    this.requestRenderer = requestRenderer;
  }

  public void initialise() throws IOException {
    progress = AnsiUtils.getProgressBar(interactive);
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
    System.out.println("");
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
    this.reportEngine = reportEngine;
  }

  public void completed() throws IOException {
    progress.clear();
  }
}
