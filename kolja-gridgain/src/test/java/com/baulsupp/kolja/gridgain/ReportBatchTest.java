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

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.baulsupp.kolja.log.viewer.importing.LogFormat;
import com.baulsupp.kolja.log.viewer.importing.PlainTextLogFormat;

/**
 * @author Yuri Schimke
 * 
 */
public class ReportBatchTest {
  private LogFormat format;
  private List<File> files;

  private List<String> reportDescriptions;

  @Before
  public void setup() {
    format = new PlainTextLogFormat();
    files = Collections.singletonList(new File("a.txt"));
    reportDescriptions = Arrays.asList("report");
  }

  @Test
  public void testBatch() {
    ReportBatch batch = new ReportBatch(format, files, reportDescriptions);

    Assert.assertEquals(reportDescriptions, batch.getReportDescriptions());
  }
}
