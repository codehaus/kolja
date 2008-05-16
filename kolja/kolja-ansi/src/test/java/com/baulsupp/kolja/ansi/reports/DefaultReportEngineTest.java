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
package com.baulsupp.kolja.ansi.reports;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.baulsupp.kolja.ansi.reports.TextReport.Detail;
import com.baulsupp.kolja.log.line.BasicLine;
import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.line.LineIndex;
import com.baulsupp.kolja.log.util.IntRange;
import com.baulsupp.kolja.log.viewer.event.EventDetector;
import com.baulsupp.kolja.log.viewer.importing.LineIndexFactory;
import com.baulsupp.kolja.log.viewer.importing.LogFormat;

/**
 * @author Yuri Schimke
 * 
 */
@RunWith(JMock.class)
public class DefaultReportEngineTest {
  Mockery context = new JUnit4Mockery();
  private DefaultReportEngine engine;
  private EventDetector eventList;
  private LineIndexFactory lineIndexFactory;
  private LineIndex lineIndex;
  private LogFormat format;
  private ReportPrinter reportPrinter;
  private TextReport<?> report;
  private File fileA = new File("a.txt");

  private Line line1 = new BasicLine("sample line 1");

  @Before
  public void setup() {
    engine = new DefaultReportEngine();

    eventList = context.mock(EventDetector.class);
    lineIndex = context.mock(LineIndex.class);
    lineIndexFactory = context.mock(LineIndexFactory.class);
    format = context.mock(LogFormat.class);
    reportPrinter = context.mock(ReportPrinter.class);
    report = context.mock(TextReport.class);

    engine.setLineIndexFactory(lineIndexFactory);
    engine.setLogFormat(format);
    engine.setReportPrinter(reportPrinter);
    engine.setReports(Collections.<TextReport<?>> singletonList(report));
  }

  @Test
  public void testStandardRun() throws Exception {
    checkSetup(true);

    context.checking(new Expectations() {
      {
        atLeast(1).of(lineIndex).length();
        will(returnValue(20));

        one(lineIndex).get(new IntRange(0, 20));
        will(returnValue(Collections.singletonList(line1)));

        one(eventList).readEvent(line1);
        will(returnValue(null));
      }
    });

    checkLifecycle();

    engine.initialise();

    engine.process(Arrays.asList(fileA));

    engine.completed();
  }

  @Test
  public void testWithRange() throws Exception {
    checkSetup(true);

    context.checking(new Expectations() {
      {
        atLeast(1).of(lineIndex).length();
        will(returnValue(200000));

        one(lineIndex).get(new IntRange(180000, 184096));
        will(returnValue(Collections.singletonList(line1)));

        one(lineIndex).get(new IntRange(184096, 188192));
        will(returnValue(Collections.emptyList()));

        one(lineIndex).get(new IntRange(188192, 190000));
        will(returnValue(Collections.emptyList()));

        one(eventList).readEvent(line1);
        will(returnValue(null));
      }
    });

    checkLifecycle();

    engine.initialise();

    engine.process(fileA, new IntRange(180000, 190000));

    engine.completed();
  }

  private void checkLifecycle() throws IOException {
    context.checking(new Expectations() {
      {
        one(report).beforeFile(fileA);

        one(report).processLine(line1);

        one(report).afterFile(fileA);

        one(report).completed();

        one(reportPrinter).completed();
      }
    });
  }

  private void checkSetup(final boolean events) throws IOException {
    context.checking(new Expectations() {
      {
        one(report).isInterested(Detail.REQUESTS);
        will(returnValue(false));

        one(report).isInterested(Detail.EVENTS);
        will(returnValue(events));

        one(report).initialise(reportPrinter, engine);

        one(lineIndexFactory).buildLineIndex(fileA, format);
        will(returnValue(lineIndex));

        one(format).supportsEvents();
        will(returnValue(events));

        if (events) {
          one(format).getEventDetector();
          will(returnValue(eventList));
        }
      }
    });
  }
}
