/**
 * Copyright (c) 2002-2007 Yuri Schimke. All Rights Reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package com.baulsupp.kolja.ansi.reports;

import static junit.framework.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.baulsupp.kolja.log.line.BasicLine;
import com.baulsupp.kolja.log.line.Line;

public class TsvReportTest {
  private TsvReport pages;
  private SimpleReportRunner reportRunner;

  @Before
  public void setup() {
    pages = new TsvReport();

    reportRunner = new SimpleReportRunner();

    pages.initialise(reportRunner);
  }

  @Test
  public void testDisplayShowsOutput() {
    pages.setFields("url", "status");

    pages.processLine(buildLine("/url/1", 400));
    pages.processLine(buildLine("/url/2", 500));

    pages.completed();

    List<String> lines = reportRunner.getLines();
    assertEquals(2, lines.size());
    assertEquals("/url/1\t400", lines.get(0));
    assertEquals("/url/2\t500", lines.get(1));
  }

  private Line buildLine(String url, Integer status) {
    BasicLine line = new BasicLine("Line - " + url);

    line.setValue("url", url);

    line.setValue("status", status);

    return line;
  }
}
