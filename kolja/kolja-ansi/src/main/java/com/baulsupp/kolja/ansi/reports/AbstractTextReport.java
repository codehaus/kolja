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

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;

import org.springframework.util.Assert;

import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.viewer.event.Event;
import com.baulsupp.kolja.log.viewer.request.RequestLine;
import com.baulsupp.kolja.util.colours.MultiColourString;

/**
 * Base class for reports.
 * 
 * @author Yuri Schimke
 */
public abstract class AbstractTextReport implements TextReport {
  protected ReportPrinter reportRunner;
  private HashSet<Detail> details;

  public AbstractTextReport() {
    this(Detail.LINES);
  }

  public AbstractTextReport(Detail... selected) {
    this.details = new HashSet<Detail>(Arrays.asList(selected));
  }

  public void initialise(ReportPrinter reportRunner) {
    Assert.notNull(reportRunner);

    this.reportRunner = reportRunner;
  }

  public boolean isInterested(Detail detail) {
    return details.contains(detail);
  }

  public void beforeFile(File file) {
  }

  public void processLine(Line line) {
  }

  public void processRequest(RequestLine line) {
  }

  public void processEvent(Event event) {
  }

  public void afterFile(File file) {
  }

  public void completed() {
  }

  protected void printTitleIfNeeded() {
    // TODO print title for multiple reports
    // if (this.reportRunner.hasMultipleReports()) {
    println(describe());
    println("");
    // }
  }

  public void println(MultiColourString string) {
    reportRunner.println(string);
  }

  public void printLine(Line line) {
    reportRunner.printLine(line);
  }

  public void printRequestLine(RequestLine line) {
    reportRunner.printRequest(line);
  }

  protected void printLinesForRequests(RequestLine l) {
    int[] offsets = l.getLineOffsets();

    for (int i : offsets) {
      printLine(readLine(i));
    }
  }

  protected Line readLine(int i) {
    // TODO implement this
    // return reportRunner.readLine(i);

    return null;
  }

  public void println(String string) {
    reportRunner.println(new MultiColourString(string));
  }
}
