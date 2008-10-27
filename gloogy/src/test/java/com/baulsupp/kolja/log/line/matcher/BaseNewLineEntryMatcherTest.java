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
package com.baulsupp.kolja.log.line.matcher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Yuri Schimke
 * 
 */
public abstract class BaseNewLineEntryMatcherTest<T extends EntryMatcher> {
  protected T matcher;

  @Before
  public void setup() {
    matcher = createMatcher("a\nb\ncdefg\n\n");
  }

  protected abstract T createMatcher(String string);

  @Test
  public void testFindsNewLines() {
    assertTrue(matcher.find(0));
    assertEquals(0, matcher.start());

    assertTrue(matcher.find());
    assertEquals(2, matcher.start());

    assertTrue(matcher.find());
    assertEquals(4, matcher.start());

    assertTrue(matcher.find());
    assertEquals(10, matcher.start());

    // assertTrue(matcher.find());
    // assertEquals(11, matcher.start());

    assertFalse(matcher.find());
  }

  @Test
  public void testCanBeReset() {
    assertTrue(matcher.find(0));
    assertEquals(0, matcher.start());

    matcher.reset("b\n");

    assertTrue(matcher.find(0));
    assertEquals(0, matcher.start());

    assertFalse(matcher.find());
  }
}
