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
package com.baulsupp.kolja.gridgain;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Collections;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.baulsupp.kolja.ansi.reports.ReportEngine;
import com.baulsupp.kolja.ansi.reports.ReportEngineFactory;
import com.baulsupp.kolja.ansi.reports.TextReport;
import com.baulsupp.kolja.log.util.IntRange;
import com.baulsupp.kolja.log.viewer.importing.LogFormat;

/**
 * @author Yuri Schimke
 * 
 */
@RunWith(JMock.class)
public class GridReportJobTest {
  private Mockery context = new Mockery();

  private File fileA = new File("a.txt");

  private LogFormat format;
  private List<TextReport<?>> reports;

  private ReportEngineFactory reportEngineFactory;
  private ReportEngine reportEngine;

  private TextReport<?> report;

  @Before
  public void setup() {
    format = context.mock(LogFormat.class);
    report = context.mock(TextReport.class);
    reports = Collections.<TextReport<?>> singletonList(report);

    reportEngineFactory = context.mock(ReportEngineFactory.class);
    reportEngine = context.mock(ReportEngine.class);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testRuns() throws Exception {
    checkSetup();

    context.checking(new Expectations() {
      {
        one(reportEngine).process(fileA, null);
      }
    });

    checkCompletion();

    GridReportJob job = new GridReportJob(format, fileA, reports, null);
    job.setReportEngineFactory(reportEngineFactory);

    List<TextReport<?>> results = (List<TextReport<?>>) job.execute();

    assertTrue(results == reports);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testRunsWithRange() throws Exception {
    checkSetup();

    context.checking(new Expectations() {
      {
        one(reportEngine).process(fileA, new IntRange(10000, 20000));
      }
    });

    checkCompletion();

    GridReportJob job = new GridReportJob(format, fileA, reports, new IntRange(10000, 20000));
    job.setReportEngineFactory(reportEngineFactory);

    List<TextReport<?>> results = (List<TextReport<?>>) job.execute();

    assertTrue(results == reports);
  }

  private void checkCompletion() throws Exception {
    context.checking(new Expectations() {
      {
        one(reportEngine).completed();
        one(report).cleanup();
      }
    });
  }

  private void checkSetup() throws Exception {
    context.checking(new Expectations() {
      {
        one(reportEngineFactory).createEngine();
        will(returnValue(reportEngine));

        one(reportEngine).setLogFormat(format);

        one(reportEngine).setReportPrinter(with(aNonNull(NullReportPrinter.class)));

        one(reportEngine).setReports(reports);

        one(reportEngine).initialise();
      }
    });
  }

}
