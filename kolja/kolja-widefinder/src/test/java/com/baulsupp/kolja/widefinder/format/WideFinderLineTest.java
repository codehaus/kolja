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
package com.baulsupp.kolja.widefinder.format;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTime;
import org.junit.Test;

import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.widefinder.WideFinderConstants;

/**
 * @author Yuri Schimke
 * 
 */
public class WideFinderLineTest {
  String line1 = "72-48-42-121.dyn.grandenetworks.net - - [01/Oct/2006:06:33:45 -0700] \"GET /ongoing/ongoing.atom HTTP/1.1\" 200 44877 \"-\" \"Onfolio/2.02\"\n";

  @Test
  public void testHandlesFailedLines() {
    Line line = new WideFinderLine("abc");

    assertEquals("abc", line.toString());
    assertTrue(line.isFailed());
  }

  @Test
  public void parsesLine() {
    Line line = new WideFinderLine(line1);

    assertEquals(line1, line.toString());
    assertFalse(line.isFailed());

    assertEquals("72-48-42-121.dyn.grandenetworks.net", line.getValue(WideFinderConstants.IPADDRESS));
    DateTime dateTime = new DateTime(2006, 10, 1, 6, 33, 45, 0);
    assertEquals(dateTime, line.getValue(WideFinderConstants.DATE));
    assertEquals("GET", line.getValue(WideFinderConstants.ACTION));
    assertEquals("/ongoing/ongoing.atom", line.getValue(WideFinderConstants.URL));
    assertEquals(HttpStatus.SUCCESS_OK, line.getValue(WideFinderConstants.STATUS));
    assertEquals(44877l, line.getValue(WideFinderConstants.SIZE));
    assertEquals(new UserAgent("Onfolio/2.02"), line.getValue(WideFinderConstants.USER_AGENT));
  }
}
