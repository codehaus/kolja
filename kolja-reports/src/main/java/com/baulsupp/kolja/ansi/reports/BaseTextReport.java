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
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;

import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.viewer.event.Event;
import com.baulsupp.kolja.log.viewer.request.RequestLine;
import com.baulsupp.kolja.util.colours.MultiColourString;

/**
 * Base class for reports.
 * 
 * @author Yuri Schimke
 */
public class BaseTextReport<T extends BaseTextReport<T>> implements TextReport<T>, Cloneable, Serializable {
  private static final long serialVersionUID = -415375410477390374L;

  protected ReportPrinter reportPrinter;
  private HashSet<Detail> details;
  private ReportContext reportContext;

  public BaseTextReport() {
    this(Detail.LINES);
  }

  public BaseTextReport(Detail... selected) {
    this.details = new HashSet<Detail>(Arrays.asList(selected));
  }

  public void initialise(ReportPrinter reportPrinter, ReportContext reportContext) {
    Assert.notNull(reportPrinter);
    Assert.notNull(reportContext);

    this.reportPrinter = reportPrinter;
    this.reportContext = reportContext;

    validate();
  }

  public void cleanup() {
    this.reportPrinter = null;
    this.reportContext = null;
  }

  /**
   * Validate Report Settings
   */
  protected void validate() {
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

  protected void printTitle() {
    reportPrinter.printTitle(describe());
  }

  public void println(MultiColourString string) {
    reportPrinter.println(string);
  }

  public void printLine(Line line) {
    reportPrinter.printLine(line);
  }

  public void printRequestLine(RequestLine line) {
    reportPrinter.printRequest(line);
  }

  protected void printLinesForRequests(RequestLine l) {
    int[] offsets = l.getLineOffsets();

    for (int i : offsets) {
      printLine(readLine(i));
    }
  }

  protected Line readLine(int i) {
    return reportContext.readLine(i);
  }

  public void println(String string) {
    reportPrinter.println(new MultiColourString(string));
  }

  @SuppressWarnings("unchecked")
  public T newInstance() {
    try {
      T clone = (T) super.clone();

      clone.reportPrinter = null;
      clone.reportContext = null;

      return clone;
    } catch (CloneNotSupportedException e) {
      throw new IllegalStateException(e);
    }
  }

  public void merge(T partReport) throws Exception {
  }

  public String describe() {
    return ClassUtils.getShortName(getClass());
  }

  public String getName() {
    return null;
  }
}
