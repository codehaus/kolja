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
package com.baulsupp.kolja.bank;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import com.baulsupp.kolja.ansi.reports.AbstractTextReport;
import com.baulsupp.kolja.log.LogConstants;
import com.baulsupp.kolja.log.viewer.request.RequestLine;

public class ActiveRequests extends AbstractTextReport {
  private LocalTime time;

  private List<RequestLine> activeRequests = new ArrayList<RequestLine>();

  public ActiveRequests() {
    super(Detail.REQUESTS);
  }

  public void setTime(LocalTime time) {
    this.time = time;
  }

  @Override
  public void processRequest(RequestLine line) {
    if (isActive(line)) {
      activeRequests.add(line);
    }
  }

  public boolean isActive(RequestLine line) {
    DateTime start = (DateTime) line.getValue(LogConstants.DATE);
    DateTime end = (DateTime) line.getValue(LogConstants.DATE + "-end");

    return TimeUtil.spansTime(start, end, time);
  }

  @Override
  public void display(boolean showHeader) {
    if (showHeader) {
      println("Requests at " + time);
    }

    for (RequestLine l : activeRequests) {
      println(l);
    }
  }

  public List<RequestLine> getRequests() {
    return activeRequests;
  }
}
