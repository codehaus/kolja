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

import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.viewer.event.Event;
import com.baulsupp.kolja.log.viewer.request.RequestLine;
import com.baulsupp.kolja.util.Mergeable;
import com.baulsupp.kolja.util.services.NamedService;

/**
 * Text Report for running a calculation.
 * 
 * @author Yuri Schimke
 */
public interface TextReport<T extends TextReport<T>> extends Mergeable<T>, NamedService {
  enum Detail {
    LINES, REQUESTS, EVENTS;
  }

  void initialise(ReportPrinter reportPrinter, ReportContext reportContext);

  boolean isInterested(Detail detail);

  void beforeFile(File file);

  void processLine(Line line);

  void processRequest(RequestLine line);

  void processEvent(Event event);

  void afterFile(File file);

  void completed();

  String describe();

  void cleanup();
}
