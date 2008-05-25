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
package com.baulsupp.kolja.log.viewer.io.fast;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.nio.charset.Charset;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Yuri Schimke
 * 
 */
public class ChunkedFileSequenceTest {
  private ChunkedFileSequence seq;
  private String expected = "012345678\n";
  private String expected3 = expected + expected + expected;

  @Before
  public void setup() throws Exception {
    File file = new File("src/test/logs/test.txt");
    seq = ChunkedFileSequence.create(file, 10, Charset.forName("US-ASCII"), 0);
  }

  @Test
  public void testReadForwardThroughFile() {
    for (int i = 0; i < seq.length(); i++) {
      char c = seq.charAt(i);
      assertEquals(expected.charAt(i % 10), c);
    }
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testFailsToReadBack() throws Exception {
    Assert.assertEquals('9', seq.charAt(38));

    seq.charAt(0);
  }

  @Test
  public void testSubSequence() {
    for (int i = 0; i < expected3.length(); i++) {
      for (int j = i; j < expected3.length(); j++) {
        assertEquals(expected3.subSequence(i, j), seq.subSequence(i, j).toString());
      }
    }
  }
}
