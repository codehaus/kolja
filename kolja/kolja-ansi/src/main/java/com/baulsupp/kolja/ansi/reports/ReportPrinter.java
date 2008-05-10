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

import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.viewer.request.RequestLine;
import com.baulsupp.kolja.util.colours.MultiColourString;

public interface ReportPrinter {
  void println(MultiColourString string);

  void printLine(Line line);

  void printLine();

  void printTitle(String title);

  void printRequest(RequestLine request);

  void initialise() throws IOException;

  void setReportEngine(ReportEngine reportEngine);

  void completed() throws IOException;
}