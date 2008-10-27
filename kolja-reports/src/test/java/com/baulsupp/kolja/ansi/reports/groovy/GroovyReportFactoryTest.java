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
package com.baulsupp.kolja.ansi.reports.groovy;

import static junit.framework.Assert.assertEquals;

import java.io.ByteArrayInputStream;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.Resource;

import com.baulsupp.kolja.ansi.reports.TextReport;

public class GroovyReportFactoryTest {
  private Mockery context = new Mockery();

  private GroovyReportFactory factory;

  private Resource resource;

  @Before
  public void setup() {
    factory = new GroovyReportFactory();
    resource = context.mock(Resource.class);
  }

  @Test
  public void testBuildsReport() throws Exception {
    context.checking(new Expectations() {
      {
        one(resource).getInputStream();
        will(returnValue(new ByteArrayInputStream(
            "class MyReport extends com.baulsupp.kolja.ansi.reports.BaseTextReport {String describe() { return \"a\" }}".getBytes())));
      }
    });

    TextReport<?> report = factory.buildReport(resource);

    assertEquals("a", report.describe());
  }
}
