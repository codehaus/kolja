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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.baulsupp.kolja.ansi.reports.TextReport;
import com.baulsupp.kolja.log.viewer.importing.LogFormat;
import com.baulsupp.kolja.log.viewer.importing.PlainTextLogFormat;
import com.baulsupp.kolja.widefinder.TimeReport;

/**
 * @author Yuri Schimke
 * 
 */
public class ReportBatchTest {
  private LogFormat format;
  private List<File> files;
  private List<TextReport<?>> reports;

  @Before
  public void setup() {
    format = new PlainTextLogFormat();
    files = Collections.singletonList(new File("a.txt"));
    reports = Collections.<TextReport<?>> singletonList(new TimeReport());
  }

  @Test
  public void testBatch() {
    ReportBatch batch = new ReportBatch(format, files, reports);

    assertTrue(reports == batch.getReports());

    List<TextReport<?>> copy = batch.getReportsCopy();
    assertFalse(reports == copy);
    assertFalse(reports.get(0) == copy.get(0));
  }

}
