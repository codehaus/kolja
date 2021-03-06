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
package com.baulsupp.kolja.ansi.reports.basic;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalTime;

import com.baulsupp.kolja.ansi.reports.BaseTextReport;
import com.baulsupp.kolja.log.LogConstants;
import com.baulsupp.kolja.log.viewer.request.RequestLine;

public class ActiveRequests extends BaseTextReport<ActiveRequests> {
  private static final long serialVersionUID = -6837582094819173737L;

  private LocalTime from;

  private LocalTime to;

  private boolean full;

  private List<RequestLine> activeRequests = new ArrayList<RequestLine>();

  private Interval interval;

  private boolean initialised = false;

  public ActiveRequests() {
    super(Detail.REQUESTS);
  }

  public void setFrom(LocalTime from) {
    this.from = from;
  }

  public void setTo(LocalTime to) {
    this.to = to;
  }

  public void setFull(boolean full) {
    this.full = full;
  }

  @Override
  public void processRequest(RequestLine line) {
    if (isActive(line)) {
      activeRequests.add(line);
    }
  }

  public boolean isActive(RequestLine line) {
    if (!initialised) {
      interval = calculateInterval(line);
      initialised = true;
    }

    if (interval == null) {
      return true;
    }

    Interval requestInterval = (Interval) line.getValue(LogConstants.INTERVAL);

    if (requestInterval == null) {
      return false;
    }

    return requestInterval.overlaps(interval) || requestInterval.abuts(interval);
  }

  private Interval calculateInterval(RequestLine line) {
    if (from == null || to == null) {
      return null;
    }

    DateTime t = (DateTime) line.getValue(LogConstants.DATE);

    return new Interval(from.toDateTime(t), to.toDateTime(t));
  }

  @Override
  public void afterFile(File file) {
    println(file.getName());

    for (RequestLine l : activeRequests) {
      printRequestLine(l);

      if (full) {
        printLinesForRequests(l);

        println("");
      }
    }
  }

  public List<RequestLine> getRequests() {
    return activeRequests;
  }

  public String describe() {
    return "Active Requests " + interval;
  }
}
