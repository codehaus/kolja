/**
 * Copyright (c) 2002-2007 Yuri Schimke. All Rights Reserved.
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package com.baulsupp.kolja.ansi.reports.engine;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baulsupp.kolja.ansi.reports.ReportContext;
import com.baulsupp.kolja.ansi.reports.ReportPrinter;
import com.baulsupp.kolja.ansi.reports.ReportUtils;
import com.baulsupp.kolja.ansi.reports.TextReport;
import com.baulsupp.kolja.ansi.reports.TextReport.Detail;
import com.baulsupp.kolja.log.line.BasicLineIterator;
import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.line.LineIndex;
import com.baulsupp.kolja.log.line.LineIterator;
import com.baulsupp.kolja.log.util.IntRange;
import com.baulsupp.kolja.log.viewer.event.Event;
import com.baulsupp.kolja.log.viewer.event.EventDetector;
import com.baulsupp.kolja.log.viewer.importing.DefaultLineIndexFactory;
import com.baulsupp.kolja.log.viewer.importing.LineIndexFactory;
import com.baulsupp.kolja.log.viewer.importing.LogFormat;
import com.baulsupp.kolja.log.viewer.request.RequestDetector;
import com.baulsupp.kolja.log.viewer.request.RequestIndex;
import com.baulsupp.kolja.log.viewer.request.RequestLine;
import com.baulsupp.kolja.util.services.BeanFactory;

/**
 * @author Yuri Schimke
 */
public class DefaultReportEngine implements ReportEngine, ReportContext {
  private Logger log = LoggerFactory.getLogger(DefaultReportEngine.class);

  protected LineIterator i;

  protected java.util.List<TextReport<?>> reports;

  private RequestDetector requestIndex;

  private boolean showEvents;

  private EventDetector eventList;

  private boolean showRequests;

  protected ReportPrinter reportPrinter;

  private LogFormat format;

  private LineIndexFactory lineIndexFactory = new DefaultLineIndexFactory();

  private BeanFactory<TextReport<?>> reportBuilder;

  public DefaultReportEngine() {
  }

  public void setReportPrinter(ReportPrinter reportPrinter) {
    this.reportPrinter = reportPrinter;
  }

  public void setReportBuilder(BeanFactory<TextReport<?>> reportBuilder) {
    this.reportBuilder = reportBuilder;
  }

  public void setReportDescriptions(List<String> v) throws Exception {
    if (reportBuilder == null) {
      reportBuilder = ReportUtils.createReportBuilder();
    }

    setReports(ReportUtils.createReports(reportBuilder, v));
  }

  public void setReports(java.util.List<TextReport<?>> reports) {
    this.reports = reports;
  }

  public void setRequestIndex(RequestIndex requestIndex) {
    this.requestIndex = requestIndex;
  }

  public void setEventList(EventDetector eventList) {
    this.eventList = eventList;
  }

  public void setLineIndexFactory(LineIndexFactory lineIndexFactory) {
    this.lineIndexFactory = lineIndexFactory;
  }

  public Line readLine(int pos) {
    i.moveTo(pos);

    return i.next();
  }

  public boolean hasMultipleReports() {
    return reports.size() > 1;
  }

  protected void iterateThroughFile(LineIterator i) {
    while (i.hasNext()) {
      Line l = i.next();

      processLine(l);
    }
  }

  public void run(File file, LineIterator i) throws InterruptedException, IOException {
    this.i = i;

    for (TextReport<?> r : reports) {
      r.beforeFile(file);
    }

    iterateThroughFile(i);

    processRequests();

    for (TextReport<?> r : reports) {
      r.afterFile(file);
    }

    this.i = null;
  }

  protected void processLine(Line line) {
    for (TextReport<?> r : reports) {
      r.processLine(line);
    }

    if (showEvents) {
      Event event = eventList.readEvent(line);

      if (event != null) {
        for (TextReport<?> r : reports) {
          r.processEvent(event);
        }
      }
    }

    if (showRequests) {
      requestIndex.processLine(null, line);
    }
  }

  private void processRequests() {
    if (showRequests) {
      Collection<RequestLine> requests = requestIndex.getKnown();

      for (RequestLine requestLine : requests) {
        for (TextReport<?> r : reports) {
          r.processRequest(requestLine);
        }
      }
    }
  }

  public void initialise() throws IOException {
    showRequests = show(Detail.REQUESTS) && format.supportsRequests();

    showEvents = show(Detail.EVENTS) && format.supportsEvents();

    for (TextReport<?> r : reports) {
      r.initialise(reportPrinter, this);
    }
  }

  private boolean show(Detail detail) {
    for (TextReport<?> r : reports) {
      if (r.isInterested(detail)) {
        return true;
      }
    }

    return false;
  }

  public void completed() throws IOException {
    boolean first = true;

    for (TextReport<?> r : reports) {
      if (!first) {
        reportPrinter.printLine();
      } else {
        first = false;
      }

      if (reports.size() > 1) {
        reportPrinter.printTitle(r.describe());
      }

      r.completed();
    }

    reportPrinter.completed();
  }

  public void process(List<File> commandFiles) throws Exception {
    for (File file : commandFiles) {
      process(file, null);
    }
  }

  public void process(File file, IntRange intRange) throws Exception {
    log.info("Processing file " + intRange);

    LineIterator lineIterator;
    if (requiresRandomAccess()) {
      LineIndex li = lineIndexFactory.buildLineIndex(file, format);

      if (showRequests) {
        RequestIndex requestIndex = format.getRequestIndex(li);
        setRequestIndex(requestIndex);
      }

      if (showEvents) {
        EventDetector eventList = format.getEventDetector();
        setEventList(eventList);
      }

      lineIterator = new BasicLineIterator(li, intRange);
    } else {
      lineIterator = lineIndexFactory.buildForwardsLineIterator(file, format, intRange);
    }

    run(file, lineIterator);

    log.info("Finished file ");
  }

  private boolean requiresRandomAccess() {
    return showRequests || showEvents;
  }

  public void setLogFormat(LogFormat format) {
    this.format = format;
  }
}
