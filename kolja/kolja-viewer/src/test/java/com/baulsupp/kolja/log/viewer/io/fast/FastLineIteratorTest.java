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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

import org.junit.Test;

import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.util.IntRange;
import com.baulsupp.kolja.log.viewer.importing.PlainTextLineParser;

/**
 * @author Yuri Schimke
 */
public class FastLineIteratorTest {
  @Test
  public void testSimple() {
    FastLineIterator li = new FastLineIterator(Pattern.compile("^", Pattern.MULTILINE), "abc\ndef\nghi\n",
        new PlainTextLineParser());

    assertTrue(li.hasNext());
    Line line = li.next();
    assertEquals("abc\n", line.toString());
    assertEquals(0, line.getOffset());

    assertTrue(li.hasNext());
    assertTrue(li.hasNext());

    assertTrue(li.hasNext());
    line = li.next();
    assertEquals("def\n", line.toString());
    assertEquals(4, line.getOffset());

    assertTrue(li.hasNext());
    line = li.next();
    assertEquals("ghi\n", line.toString());
    assertEquals(8, line.getOffset());

    assertFalse(li.hasNext());
  }

  @Test
  public void testCanBeRestrictedToRange() {
    FastLineIterator li = new FastLineIterator(Pattern.compile("^", Pattern.MULTILINE), "abc\ndef\nghi\n",
        new PlainTextLineParser(), new IntRange(4, 8));

    assertTrue(li.hasNext());
    Line line = li.next();
    assertEquals("def\n", line.toString());
    assertEquals(4, line.getOffset());

    assertFalse(li.hasNext());
  }

  @Test
  public void testWorksThroughFile() throws Exception {
    File file = new File("src/test/logs/test.txt");
    ChunkedFileSequence seq = new ChunkedFileSequence(file, 10, Charset.forName("US-ASCII"));

    FastLineIterator li = new FastLineIterator(Pattern.compile("^", Pattern.MULTILINE), seq, new PlainTextLineParser());

    while (li.hasNext()) {
      assertEquals("012345678\n", li.next().toString());
    }
  }

  @Test
  public void testWorksThroughFileWithOffset() throws Exception {
    File file = new File("src/test/logs/test.txt");
    ChunkedFileSequence seq = new ChunkedFileSequence(file, 10, Charset.forName("US-ASCII"), 9);

    FastLineIterator li = new FastLineIterator(Pattern.compile("^", Pattern.MULTILINE), seq, new PlainTextLineParser(),
        new IntRange(10, seq.length()));

    while (li.hasNext()) {
      assertEquals("012345678\n", li.next().toString());
    }
  }
}
