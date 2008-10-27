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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.line.matcher.EntryMatcher;
import com.baulsupp.kolja.log.line.matcher.EntryPattern;
import com.baulsupp.kolja.log.line.matcher.RegexEntryPattern;
import com.baulsupp.kolja.log.viewer.importing.PlainTextLineParser;

/**
 * @author Yuri Schimke
 */
@RunWith(JMock.class)
public class FastLineIteratorTest {
  private Mockery context = new Mockery();
  private EntryPattern pattern;
  private EntryMatcher matcher;
  private FileInputStream is;

  @Before
  public void setup() throws FileNotFoundException {
    pattern = context.mock(EntryPattern.class);
    matcher = context.mock(EntryMatcher.class);

    File file = new File("src/test/logs/test.txt");
    is = new FileInputStream(file);
  }

  @After
  public void tearDown() throws IOException {
    is.close();
  }

  @Test
  public void testSimple() {
    context.checking(new Expectations() {
      {
        one(pattern).matcher("abc\ndef\nghi\n");
        will(returnValue(matcher));

        one(matcher).find(0);
        will(returnValue(true));

        one(matcher).start();
        will(returnValue(0));

        one(matcher).find();
        will(returnValue(true));

        one(matcher).start();
        will(returnValue(4));

        one(matcher).find();
        will(returnValue(true));

        one(matcher).start();
        will(returnValue(8));

        one(matcher).find();
        will(returnValue(false));
      }
    });

    FastLineIterator li = new FastLineIterator(pattern, "abc\ndef\nghi\n", new PlainTextLineParser());

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
    context.checking(new Expectations() {
      {
        one(pattern).matcher("def\nghi\n");
        will(returnValue(matcher));

        one(matcher).find(0);
        will(returnValue(true));

        one(matcher).start();
        will(returnValue(0));

        one(matcher).find();
        will(returnValue(true));

        one(matcher).start();
        will(returnValue(4));
      }
    });

    FastLineIterator li = new FastLineIterator(pattern, "def\nghi\n", new PlainTextLineParser(), 4, 4);

    assertTrue(li.hasNext());
    Line line = li.next();
    assertEquals("def\n", line.toString());
    assertEquals(4, line.getOffset());

    assertFalse(li.hasNext());
  }

  @Test
  public void testWorksThroughFile() throws Exception {
    ChunkedFileSequence seq = new ChunkedFileSequence(is, 100, Charset.forName("US-ASCII"), 0);

    pattern = new RegexEntryPattern(Pattern.compile("^", Pattern.MULTILINE));

    FastLineIterator li = new FastLineIterator(pattern, seq, new PlainTextLineParser());

    while (li.hasNext()) {
      Line line = li.next();
      assertEquals("012345678\n", line.toString());
    }
  }

  @Test
  public void testWorksThroughFileWithOffset() throws Exception {
    ChunkedFileSequence seq = new ChunkedFileSequence(is, 100, Charset.forName("US-ASCII"), 10);

    pattern = new RegexEntryPattern(Pattern.compile("^", Pattern.MULTILINE));

    FastLineIterator li = new FastLineIterator(pattern, seq, new PlainTextLineParser(), 10, seq.length() - 10);

    while (li.hasNext()) {
      assertEquals("012345678\n", li.next().toString());
    }
  }
}
