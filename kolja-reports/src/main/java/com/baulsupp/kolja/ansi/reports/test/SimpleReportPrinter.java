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
package com.baulsupp.kolja.ansi.reports.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.baulsupp.kolja.ansi.reports.ReportPrinter;
import com.baulsupp.kolja.ansi.reports.engine.ReportEngine;
import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.viewer.importing.LogFormat;
import com.baulsupp.kolja.log.viewer.request.RequestLine;
import com.baulsupp.kolja.util.colours.MultiColourString;

public class SimpleReportPrinter implements ReportPrinter {
  private List<String> lines = new ArrayList<String>();

  public String getName() {
    return "test";
  }

  public void println(MultiColourString string) {
    lines.add(string.toString());
  }

  public void printLine(Line line) {
    lines.add(line.toString());
  }

  public List<String> getLines() {
    return lines;
  }

  public void printRequest(RequestLine request) {
    lines.add(request.toString());
  }

  public void printLine() {
    lines.add("");
  }

  public void printTitle(String title) {
    // lines.add(title);
  }

  public void initialise(LogFormat format) throws IOException {
  }

  public void setReportEngine(ReportEngine reportEngine) {
  }

  public void completed() throws IOException {
  }
}
