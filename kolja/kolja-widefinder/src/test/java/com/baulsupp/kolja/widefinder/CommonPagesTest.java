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
import com.baulsupp.kolja.log.line.BasicLine;
import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.widefinder.StringFrequencies.Count;

public class CommonPagesTest {
  private CommonPages pages;
  private SimpleReportRunner reportRunner;

  @Before
  public void setup() {
    pages = new CommonPages();

    reportRunner = new SimpleReportRunner();

    pages.initialise(reportRunner);
  }

  @Test
  public void testWorksWithoutAnyLines() {
    pages.completed();

    StringFrequencies freq = pages.getUrlFrequencies();

    assertNotNull(freq);
    assertEquals(0, freq.size());
  }

  @Test
  public void testBuildUrlCountBasedOnInput() {
    pages.processLine(buildLine("/url/1"));
    pages.processLine(buildLine("/url/2"));
    pages.processLine(buildLine("/url/2"));
    pages.processLine(buildLine("/url/3"));
    pages.processLine(buildLine("/url/3"));
    pages.processLine(buildLine("/url/3"));

    pages.completed();

    StringFrequencies freq = pages.getUrlFrequencies();

    assertNotNull(freq);
    assertEquals(3, freq.size());
    assertEquals(1, (int) freq.get("/url/1"));
    assertEquals(2, (int) freq.get("/url/2"));
    assertEquals(3, (int) freq.get("/url/3"));
  }

  @Test
  public void testReturnsMostFrequentPages() {
    pages.processLine(buildLine("/url/1"));
    pages.processLine(buildLine("/url/1"));
    pages.processLine(buildLine("/url/2"));
    pages.processLine(buildLine("/url/2"));
    pages.processLine(buildLine("/url/2"));
    pages.processLine(buildLine("/url/3"));
    pages.processLine(buildLine("/url/3"));
    pages.processLine(buildLine("/url/3"));
    pages.processLine(buildLine("/url/3"));
    pages.processLine(buildLine("/url/4"));
    pages.processLine(buildLine("/url/5"));
    pages.processLine(buildLine("/url/6"));

    pages.completed();

    List<Count> mostFreq = pages.getMostFrequentUrls(3);

    assertNotNull(mostFreq);
    assertEquals(3, mostFreq.size());
    assertEquals("4 /url/3", mostFreq.get(0).toString());
    assertEquals("3 /url/2", mostFreq.get(1).toString());
    assertEquals("2 /url/1", mostFreq.get(2).toString());
  }

  @Test
  public void testDisplayShowsOutput() {
    pages.processLine(buildLine("/url/1"));
    pages.processLine(buildLine("/url/1"));
    pages.processLine(buildLine("/url/2"));

    pages.completed();

    pages.display();

    List<String> lines = reportRunner.getLines();
    assertEquals(2, lines.size());
    assertEquals("2 /url/1", lines.get(0));
    assertEquals("1 /url/2", lines.get(1));
  }

  private Line buildLine(String string) {
    BasicLine line = new BasicLine("Line - " + string);

    line.setValue(WideFinderConstants.URL, string);

    return line;
  }
}
