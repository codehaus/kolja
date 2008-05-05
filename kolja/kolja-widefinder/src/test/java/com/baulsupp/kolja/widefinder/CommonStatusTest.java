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
package com.baulsupp.kolja.widefinder;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.baulsupp.kolja.ansi.reports.SimpleReportRunner;
import com.baulsupp.kolja.ansi.reports.basic.Frequencies;
import com.baulsupp.kolja.ansi.reports.basic.FrequencyReport;
import com.baulsupp.kolja.ansi.reports.basic.Frequencies.Count;
import com.baulsupp.kolja.log.line.BasicLine;
import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.widefinder.format.HttpStatus;

public class CommonStatusTest {
  private static final HttpStatus _200 = new HttpStatus("200");
  private static final HttpStatus _404 = new HttpStatus("404");
  private static final HttpStatus _500 = new HttpStatus("500");

  private FrequencyReport<HttpStatus> pages;
  private SimpleReportRunner reportRunner;

  @Before
  public void setup() {
    pages = new FrequencyReport<HttpStatus>(WideFinderConstants.STATUS);

    reportRunner = new SimpleReportRunner();

    pages.initialise(reportRunner);
  }

  @Test
  public void testWorksWithoutAnyLines() {
    pages.completed();

    Frequencies<HttpStatus> freq = pages.getFrequencies();

    assertNotNull(freq);
    assertEquals(0, freq.size());
  }

  @Test
  public void testBuildUrlCountBasedOnInput() {
    pages.processLine(buildLine(_500));
    pages.processLine(buildLine(_404));
    pages.processLine(buildLine(_404));
    pages.processLine(buildLine(_200));
    pages.processLine(buildLine(_200));
    pages.processLine(buildLine(_200));

    pages.completed();

    Frequencies<HttpStatus> freq = pages.getFrequencies();

    assertNotNull(freq);
    assertEquals(3, freq.size());
    assertEquals(1, (int) freq.get(_500));
    assertEquals(2, (int) freq.get(_404));
    assertEquals(3, (int) freq.get(_200));
  }

  @Test
  public void testReturnsMostFrequentPages() {
    pages.processLine(buildLine(_500));
    pages.processLine(buildLine(_200));
    pages.processLine(buildLine(_200));

    pages.completed();

    List<Count<HttpStatus>> mostFreq = pages.getMostFrequent(3);

    assertNotNull(mostFreq);
    assertEquals(2, mostFreq.size());
    assertEquals("2 200", mostFreq.get(0).toString());
    assertEquals("1 500", mostFreq.get(1).toString());
  }

  @Test
  public void testDisplayShowsOutput() {
    pages.processLine(buildLine(_500));
    pages.processLine(buildLine(_200));
    pages.processLine(buildLine(_200));

    pages.completed();

    List<String> lines = reportRunner.getLines();
    assertEquals(2, lines.size());
    assertEquals("200 2", lines.get(0));
    assertEquals("500 1", lines.get(1));
  }

  private Line buildLine(HttpStatus string) {
    BasicLine line = new BasicLine("Line - " + string);

    line.setValue(WideFinderConstants.STATUS, string);

    return line;
  }
}
