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

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Collections;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

import com.baulsupp.kolja.ansi.reports.TextReport;
import com.baulsupp.kolja.log.viewer.importing.LogFormat;
import com.baulsupp.kolja.log.viewer.importing.PlainTextLogFormat;

/**
 * @author Yuri Schimke
 * 
 */
public class ReportBatchTest {
  private Mockery context = new Mockery();

  private LogFormat format;
  private List<File> files;
  private List<TextReport<?>> reports;
  private TextReport<?> report;

  private TextReport<?> report2;

  @Before
  public void setup() {
    format = new PlainTextLogFormat();
    files = Collections.singletonList(new File("a.txt"));
    report = context.mock(TextReport.class, "report1");
    report2 = context.mock(TextReport.class, "report2");
    reports = Collections.<TextReport<?>> singletonList(report);
  }

  @Test
  public void testBatch() {
    context.checking(new Expectations() {
      {
        one(report).newInstance();
        will(returnValue(report2));
      }
    });

    ReportBatch batch = new ReportBatch(format, files, reports);

    assertTrue(reports == batch.getReports());

    List<TextReport<?>> copy = batch.getReportsCopy();
    assertSame(report2, copy.get(0));
  }

}
