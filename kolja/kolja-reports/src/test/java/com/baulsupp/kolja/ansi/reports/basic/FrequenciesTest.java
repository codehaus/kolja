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
package com.baulsupp.kolja.ansi.reports.basic;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Yuri Schimke
 * 
 */
public class FrequenciesTest {
  @Before
  public void setup() {
  }

  @Test
  public void testMergable() {
    Frequencies<String> freq1 = new Frequencies<String>();
    freq1.increment("a");
    freq1.increment("b");

    Frequencies<String> freq2 = new Frequencies<String>();
    freq2.increment("b");
    freq2.increment("c");

    freq1.merge(freq2);

    assertEquals(1, freq1.get("a"));
    assertEquals(2, freq1.get("b"));
    assertEquals(1, freq1.get("c"));
  }
}
