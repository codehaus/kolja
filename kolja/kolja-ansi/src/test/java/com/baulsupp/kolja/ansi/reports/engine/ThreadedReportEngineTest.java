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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.Invocation;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.action.CustomAction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.baulsupp.kolja.ansi.reports.ReportPrinter;
import com.baulsupp.kolja.ansi.reports.TextReport;
import com.baulsupp.kolja.ansi.reports.engine.file.FileDivider;
import com.baulsupp.kolja.ansi.reports.engine.file.FileSection;
import com.baulsupp.kolja.ansi.reports.engine.file.NullReportContext;
import com.baulsupp.kolja.ansi.reports.engine.file.NullReportPrinter;
import com.baulsupp.kolja.log.util.IntRange;
import com.baulsupp.kolja.log.viewer.importing.LogFormat;

/**
 * @author Yuri Schimke
 * 
 */
@RunWith(JMock.class)
public class ThreadedReportEngineTest {
  Mockery context = new JUnit4Mockery();
  private ThreadedReportEngine engine;
  private LogFormat format;
  private ReportPrinter reportPrinter;
  private TextReport<?> report;
  private TextReport<?> reportCopy1;
  private TextReport<?> reportCopy2;
  private File fileA = new File("a.txt");
  private ExecutorService executor;
  private FileDivider fileDivider;
  private ReportEngineFactory reportEngineFactory;
  protected ReportEngine localReportEngine;
  private IntRange range1;
  private IntRange range2;

  @Before
  public void setup() {
    engine = new ThreadedReportEngine();

    format = context.mock(LogFormat.class);
    reportPrinter = context.mock(ReportPrinter.class);
    report = context.mock(TextReport.class);
    reportCopy1 = context.mock(TextReport.class, "copy1");
    reportCopy2 = context.mock(TextReport.class, "copy2");
    executor = context.mock(ExecutorService.class);
    fileDivider = context.mock(FileDivider.class);
    reportEngineFactory = context.mock(ReportEngineFactory.class);
    localReportEngine = context.mock(ReportEngine.class);

    engine.setLogFormat(format);
    engine.setReportPrinter(reportPrinter);
    engine.setReports(Collections.<TextReport<?>> singletonList(report));

    engine.setExecutor(executor);
    engine.setFileDivider(fileDivider);
    engine.setReportEngineFactory(reportEngineFactory);

    range1 = new IntRange(0, 10000);
    range2 = new IntRange(10000, 20000);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testStandardRun() throws Exception {
    checkSetup(true);

    final List<FileSection> sections = Arrays.asList(new FileSection(fileA, range1), new FileSection(fileA, range2));

    context.checking(new Expectations() {
      {
        one(fileDivider).split(Collections.singletonList(fileA), Runtime.getRuntime().availableProcessors());
        will(returnValue(sections));

        exactly(2).of(executor).execute(with(aNonNull(FutureTask.class)));
        will(new CustomAction("execute future") {
          public Object invoke(Invocation invocation) throws Throwable {
            FutureTask<?> task = (FutureTask<?>) invocation.getParameter(0);
            task.run();
            return null;
          }
        });

        one(report).newInstance();
        will(returnValue(reportCopy1));

        one(report).newInstance();
        will(returnValue(reportCopy2));

        exactly(2).of(reportEngineFactory).createEngine();
        will(returnValue(localReportEngine));

        exactly(2).of(localReportEngine).setLogFormat(format);

        exactly(2).of(localReportEngine).setReportPrinter(with(aNonNull(NullReportPrinter.class)));

        one(localReportEngine).setReports(Collections.<TextReport<?>> singletonList(reportCopy1));

        one(localReportEngine).setReports(Collections.<TextReport<?>> singletonList(reportCopy2));

        exactly(2).of(localReportEngine).initialise();

        one(localReportEngine).process(fileA, range1);

        one(localReportEngine).process(fileA, range2);

        one((TextReport) report).merge((TextReport) reportCopy1);
        one((TextReport) report).merge((TextReport) reportCopy2);

        one(report).initialise(with(equal(reportPrinter)), with(aNonNull(NullReportContext.class)));

        one(report).completed();

        exactly(2).of(localReportEngine).completed();

        one(executor).shutdown();
        one(executor).awaitTermination(10, TimeUnit.SECONDS);
      }
    });

    checkLifecycle();

    engine.initialise();

    engine.process(Arrays.asList(fileA));

    engine.completed();
  }

  private void checkLifecycle() throws IOException {
    context.checking(new Expectations() {
      {
      }
    });
  }

  private void checkSetup(final boolean events) throws IOException {
    context.checking(new Expectations() {
      {
      }
    });
  }

}
