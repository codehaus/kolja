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

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.gridgain.grid.GridJob;
import org.gridgain.grid.GridJobResult;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.baulsupp.kolja.ansi.reports.TextReport;
import com.baulsupp.kolja.ansi.reports.engine.file.FileDivider;
import com.baulsupp.kolja.ansi.reports.engine.file.FileSection;
import com.baulsupp.kolja.log.util.LongRange;
import com.baulsupp.kolja.log.viewer.importing.PlainTextLogFormat;
import com.baulsupp.kolja.util.services.BeanFactory;

/**
 * @author Yuri Schimke
 * 
 */
@RunWith(JMock.class)
public class GridReportSplitterTest {
  private Mockery context = new Mockery();

  private File fileA = new File("a.txt");
  private File fileB = new File("b.txt");

  private GridReportSplitter splitter;
  private PlainTextLogFormat format;

  private List<String> reportDescriptions;

  private FileDivider fileDivider;

  private BeanFactory<TextReport<?>> reportBuilder;

  private TextReport<?> report;

  private GridJobResult result1;

  private GridJobResult result2;

  private TextReport<?> report1;

  private TextReport<?> report2;

  @SuppressWarnings("unchecked")
  @Before
  public void setup() {
    splitter = new GridReportSplitter();

    reportBuilder = context.mock(BeanFactory.class);
    splitter.setReportBuilder(reportBuilder);

    fileDivider = context.mock(FileDivider.class);
    splitter.setFileDivider(fileDivider);

    reportDescriptions = Arrays.asList("report");

    format = new PlainTextLogFormat();

    report = context.mock(TextReport.class, "report");

    result1 = context.mock(GridJobResult.class, "result1");

    result2 = context.mock(GridJobResult.class, "result2");

    report1 = context.mock(TextReport.class, "report1");
    report2 = context.mock(TextReport.class, "report2");
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testSplitsFilesIntoJobs() throws Exception {
    final List<File> files = Arrays.asList(fileA, fileB);

    final List<FileSection> sections = Arrays.asList(new FileSection(fileA, null), new FileSection(fileB, new LongRange(0, 10000)),
        new FileSection(fileB, new LongRange(10000, 20000)));

    context.checking(new Expectations() {
      {
        one(fileDivider).split(files, 10);
        will(returnValue(sections));

        one(reportBuilder).create("report");
        will(returnValue(report));

        one(result1).getData();
        will(returnValue(Arrays.<TextReport<?>> asList(report1)));
        one(result2).getData();
        will(returnValue(Arrays.<TextReport<?>> asList(report2)));

        one((TextReport) report).merge(report1);
        one((TextReport) report).merge(report2);
      }
    });

    ReportBatch batch = new ReportBatch(format, files, reportDescriptions);

    Collection<? extends GridJob> jobs = splitter.split(10, batch);

    assertEquals(3, jobs.size());
    Iterator<? extends GridJob> i = jobs.iterator();

    GridReportJob job1 = (GridReportJob) i.next();
    assertEquals(fileA, job1.getFile());
    Assert.assertNull(job1.getLongRange());

    GridReportJob job2 = (GridReportJob) i.next();
    assertEquals(fileB, job2.getFile());
    assertEquals(new LongRange(0, 10000), job2.getLongRange());

    GridReportJob job3 = (GridReportJob) i.next();
    assertEquals(fileB, job3.getFile());
    assertEquals(new LongRange(10000, 20000), job3.getLongRange());

    List<GridJobResult> parts = Arrays.asList(result1, result2);

    List<TextReport<?>> result = splitter.reduce(parts);

    assertEquals(1, result.size());
    assertEquals(report, result.get(0));
  }
}
