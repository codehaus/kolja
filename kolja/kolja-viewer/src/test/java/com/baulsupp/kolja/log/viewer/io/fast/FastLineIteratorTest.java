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

import java.util.regex.Pattern;

import org.junit.Test;

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
    assertEquals("abc\n", li.next().toString());

    assertTrue(li.hasNext());
    assertEquals("def\n", li.next().toString());

    assertTrue(li.hasNext());
    assertEquals("ghi\n", li.next().toString());

    assertFalse(li.hasNext());
  }

}
