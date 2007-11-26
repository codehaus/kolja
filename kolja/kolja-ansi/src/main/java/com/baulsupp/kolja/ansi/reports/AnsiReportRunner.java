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
import java.io.IOException;
import java.util.Collection;

import jline.ANSIBuffer;

import com.baulsupp.kolja.ansi.AnsiUtils;
import com.baulsupp.kolja.ansi.ConsoleRenderer;
import com.baulsupp.kolja.ansi.progress.ProgressBar;
import com.baulsupp.kolja.ansi.reports.TextReport.Detail;
import com.baulsupp.kolja.log.line.BasicLineIterator;
import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.viewer.event.Event;
import com.baulsupp.kolja.log.viewer.event.EventList;
import com.baulsupp.kolja.log.viewer.request.RequestIndex;
import com.baulsupp.kolja.log.viewer.request.RequestLine;
import com.baulsupp.kolja.util.colours.MultiColourString;

public class AnsiReportRunner implements ReportRunner {
  protected boolean ansi;

  protected BasicLineIterator i;

  protected ConsoleRenderer<Line> lineRenderer;

  protected ConsoleRenderer<Line> requestRenderer;

  protected java.util.List<TextReport> reports;

  private RequestIndex requestIndex;

  private boolean showEvents;

  private EventList eventList;

  private boolean showRequests;

  private ProgressBar progress;

  private boolean interactive = true;

  public AnsiReportRunner() {
  }

  public void setReports(java.util.List<TextReport> reports) {
    this.reports = reports;
  }

  public void setInteractive(boolean interactive) {
    this.interactive = interactive;
  }

  public void setRequestIndex(RequestIndex requestIndex) {
    this.requestIndex = requestIndex;
  }

  public void setLineRenderer(ConsoleRenderer<Line> lineRenderer) {
    this.lineRenderer = lineRenderer;
  }

  public void setRequestRenderer(ConsoleRenderer<Line> requestRenderer) {
    this.requestRenderer = requestRenderer;
  }

  public void setEventList(EventList eventList) {
    this.eventList = eventList;
  }

  public void run(File file, BasicLineIterator i) throws InterruptedException, IOException {
    this.i = i;

    for (TextReport r : reports) {
      r.beforeFile(file);
    }

    long total = file.length();

    while (i.hasNext()) {
      Line l = i.next();

      progress.showProgress(l.getOffset(), (int) total);

      processLine(l);
    }

    progress.clear();

    processRequests();

    for (TextReport r : reports) {
      r.afterFile(file);
    }

    this.i = null;
  }

  private void processRequests() {
    if (showRequests) {
      Collection<RequestLine> requests = requestIndex.getKnown();

      for (RequestLine requestLine : requests) {
        for (TextReport r : reports) {
          r.processRequest(requestLine);
        }
      }
    }
  }

  public void initialise() throws IOException {
    progress = AnsiUtils.getProgressBar(interactive);

    showRequests = show(Detail.REQUESTS);

    showEvents = show(Detail.EVENTS);

    for (TextReport r : reports) {
      r.initialise(this);
    }
  }

  private boolean show(Detail detail) {
    for (TextReport r : reports) {
      if (r.isInterested(detail)) {
        return true;
      }
    }

    return false;
  }

  public void completed() {
    for (TextReport r : reports) {
      r.completed();
    }
  }

  private void processLine(Line line) {
    for (TextReport r : reports) {
      r.processLine(line);
    }

    if (showEvents) {
      Event event = eventList.readEvent(line);

      if (event != null) {
        for (TextReport r : reports) {
          r.processEvent(event);
        }
      }
    }

    if (showRequests) {
      requestIndex.processLine(null, line);
    }
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

  public Line readLine(int pos) {
    i.moveTo(pos);

    return i.next();
  }

  public void printLine(Line line) {
    progress.clear();

    lineRenderer.show(line);
  }

  public void printRequest(RequestLine request) {
    progress.clear();

    requestRenderer.show(request);
  }
}
