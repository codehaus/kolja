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

import com.baulsupp.kolja.ansi.reports.basic.DumpEvents;
import com.baulsupp.kolja.log.line.BasicLine;
import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.viewer.event.Event;

public class DumpEventsTest {
  private DumpEvents pages;
  private SimpleReportRunner reportRunner;

  @Before
  public void setup() {
    pages = new DumpEvents();

    reportRunner = new SimpleReportRunner();

    pages.initialise(reportRunner);
  }

  @Test
  public void testDisplayShowsOutput() {
    pages.processEvent(buildEvent("/url/1"));
    pages.processEvent(buildEvent("/url/2"));

    pages.completed();

    List<String> lines = reportRunner.getLines();
    assertEquals(2, lines.size());
    assertEquals("/url/1", lines.get(0));
    assertEquals("/url/2", lines.get(1));
  }

  private Event buildEvent(String string) {
    return new Event(buildLine(string), string);
  }

  private Line buildLine(String string) {
    BasicLine line = new BasicLine("Line - " + string);

    return line;
  }
}
