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
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.widefinder.WideFinderConstants;

/**
 * @author Yuri Schimke
 * 
 */
public class WideFinderLineParserTest {
  private WideFinderLineParser parser;

  @Before
  public void setup() {
    parser = new WideFinderLineParser();
  }

  @Test
  public void testParsesLine() {
    Line line = parser.parse("ac");

    assertTrue(line instanceof WideFinderLine);
  }

  @Test
  public void testDefinesFields() {
    Set<String> set = new HashSet<String>();

    set.add(WideFinderConstants.IPADDRESS);
    set.add(WideFinderConstants.DATE);
    set.add(WideFinderConstants.ACTION);
    set.add(WideFinderConstants.URL);
    set.add(WideFinderConstants.STATUS);
    set.add(WideFinderConstants.USER_AGENT);
    set.add(WideFinderConstants.REFERRER);
    set.add(WideFinderConstants.USER);

    assertEquals(set, parser.getNames());
  }
}
