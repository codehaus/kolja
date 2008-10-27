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
package com.baulsupp.kolja.log.line;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.baulsupp.kolja.log.util.IntRange;

/**
 * @author Yuri Schimke
 * 
 */
@RunWith(JMock.class)
public class BasicLineIteratorTest {
  private Mockery context = new Mockery();
  private LineIndex li;
  private BasicLine line1 = new BasicLine("line one");

  @Before
  public void setup() {
    li = context.mock(LineIndex.class);
  }

  @Test
  public void testStandard() {
    context.checking(new Expectations() {
      {
        atLeast(1).of(li).length();
        will(returnValue(100));

        one(li).get(new IntRange(0, 100));
        will(returnValue(Collections.singletonList(line1)));
      }
    });

    BasicLineIterator lineIterator = new BasicLineIterator(li);

    assertTrue(lineIterator.hasNext());

    Line l = lineIterator.next();
    assertEquals(line1, l);

    assertFalse(lineIterator.hasNext());
  }

  @Test
  public void testLargeFile() {
    context.checking(new Expectations() {
      {
        atLeast(1).of(li).length();
        will(returnValue(10000));

        one(li).get(new IntRange(0, 4096));
        will(returnValue(Collections.singletonList(line1)));

        one(li).get(new IntRange(4096, 8192));
        will(returnValue(Collections.emptyList()));

        one(li).get(new IntRange(8192, 10000));
        will(returnValue(Collections.emptyList()));
      }
    });

    BasicLineIterator lineIterator = new BasicLineIterator(li);

    assertTrue(lineIterator.hasNext());

    Line l = lineIterator.next();
    assertEquals(line1, l);

    assertFalse(lineIterator.hasNext());
  }

  @Test
  public void testLargeFileWithOffsets() {
    context.checking(new Expectations() {
      {
        atLeast(1).of(li).length();
        will(returnValue(10000));

        one(li).get(new IntRange(3000, 7096));
        will(returnValue(Collections.singletonList(line1)));

        one(li).get(new IntRange(7096, 8000));
        will(returnValue(Collections.emptyList()));
      }
    });

    BasicLineIterator lineIterator = new BasicLineIterator(li, new IntRange(3000, 8000));

    assertTrue(lineIterator.hasNext());

    Line l = lineIterator.next();
    assertEquals(line1, l);

    assertFalse(lineIterator.hasNext());
  }

}
