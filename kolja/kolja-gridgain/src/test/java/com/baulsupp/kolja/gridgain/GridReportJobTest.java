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

import static junit.framework.Assert.assertEquals;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.baulsupp.kolja.ansi.reports.TextReport;
import com.baulsupp.kolja.ansi.reports.engine.ReportEngine;
import com.baulsupp.kolja.ansi.reports.engine.ReportEngineFactory;
import com.baulsupp.kolja.ansi.reports.engine.file.NullReportPrinter;
import com.baulsupp.kolja.log.util.LongRange;
import com.baulsupp.kolja.log.viewer.importing.LogFormat;
import com.baulsupp.kolja.util.services.BeanFactory;

/**
 * @author Yuri Schimke
 * 
 */
@RunWith(JMock.class)
public class GridReportJobTest {
  private Mockery context = new Mockery();

  private File fileA = new File("a.txt");

  private LogFormat format;

  private ReportEngineFactory reportEngineFactory;
  private ReportEngine reportEngine;
  private BeanFactory<TextReport<?>> reportBuilder;

  private TextReport<?> report;

  private List<String> reportDescriptions;

  @SuppressWarnings("unchecked")
  @Before
  public void setup() {
    format = context.mock(LogFormat.class);
    report = context.mock(TextReport.class);
    reportBuilder = context.mock(BeanFactory.class);

    reportDescriptions = Arrays.asList("report");

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

    GridReportJob job = new GridReportJob(format, fileA, reportDescriptions, null);
    job.setReportBuilder(reportBuilder);
    job.setReportEngineFactory(reportEngineFactory);

    List<TextReport<?>> results = (List<TextReport<?>>) job.execute();

    assertEquals(1, results.size());
    assertEquals(report, results.get(0));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testRunsWithRange() throws Exception {
    checkSetup();

    context.checking(new Expectations() {
      {
        one(reportEngine).process(fileA, new LongRange(10000, 20000));
      }
    });

    checkCompletion();

    GridReportJob job = new GridReportJob(format, fileA, reportDescriptions, new LongRange(10000, 20000));
    job.setReportBuilder(reportBuilder);
    job.setReportEngineFactory(reportEngineFactory);

    List<TextReport<?>> results = (List<TextReport<?>>) job.execute();

    assertEquals(1, results.size());
    assertEquals(report, results.get(0));
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

        one(reportBuilder).create("report");
        will(returnValue(report));

        one(reportEngine).setReports(Arrays.<TextReport<?>> asList(report));

        one(reportEngine).initialise();
      }
    });
  }

}
