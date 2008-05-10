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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.gridgain.grid.GridException;
import org.gridgain.grid.GridJob;
import org.junit.Before;
import org.junit.Test;

import com.baulsupp.kolja.ansi.reports.TextReport;
import com.baulsupp.kolja.log.viewer.importing.PlainTextLogFormat;
import com.baulsupp.kolja.widefinder.TimeReport;

/**
 * @author Yuri Schimke
 * 
 */
public class GridReportSplitterTest {
  private static final File FILE_A = new File("a.txt");
  private static final File FILE_B = new File("b.txt");

  private GridReportSplitter splitter;
  private PlainTextLogFormat format;
  private List<TextReport<?>> reports;

  @Before
  public void setup() {
    splitter = new GridReportSplitter();

    format = new PlainTextLogFormat();
    reports = Collections.<TextReport<?>> singletonList(new TimeReport());
  }

  @Test
  public void testSplitsTwoFilesIntoTwoJobs() throws GridException {
    List<File> files = Arrays.asList(FILE_A, FILE_B);

    ReportBatch batch = new ReportBatch(format, files, reports);

    Collection<? extends GridJob> jobs = splitter.split(10, batch);

    assertEquals(2, jobs.size());
    Iterator<? extends GridJob> i = jobs.iterator();

    GridReportJob job1 = (GridReportJob) i.next();
    assertEquals(FILE_A, job1.getFile());

    GridReportJob job2 = (GridReportJob) i.next();
    assertEquals(FILE_B, job2.getFile());
  }
}
