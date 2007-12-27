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

import java.util.ArrayList;
import java.util.List;

import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.viewer.request.RequestLine;
import com.baulsupp.kolja.util.colours.MultiColourString;

public class SimpleReportRunner implements ReportRunner {
  private List<String> lines = new ArrayList<String>();

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

  public Line readLine(int i) {
    return null;
  }

  public void printLine() {
    lines.add("");
  }

  public void printTitle(String title) {
    lines.add(title);
  }

  public boolean hasMultipleReports() {
    return false;
  }
}
