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
package com.baulsupp.kolja.ansi.reports.engine.file;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.baulsupp.kolja.log.util.LongRange;

/**
 * @author Yuri Schimke
 * 
 */
public class DefaultFileDividerTest {

  private DefaultFileDivider divider;
  private File fileA;
  private File fileB;

  @Before
  public void setup() {
    divider = new DefaultFileDivider();

    fileA = new File("src/test/log/text.log");
    fileB = new File("src/test/log/text.log");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailsOnMissingFiles() {
    divider.split(Arrays.asList(new File("fdsklj")), 1);
  }

  @Test
  public void testDividesSmallFiles() {
    List<FileSection> parts = divider.split(Arrays.asList(fileA, fileB), 1);

    Assert.assertEquals(2, parts.size());

    FileSection sectionA = parts.get(0);
    assertEquals(fileA, sectionA.getFile());
    assertNull(sectionA.getLongRange());

    FileSection sectionB = parts.get(1);
    assertEquals(fileB, sectionB.getFile());
    assertNull(sectionB.getLongRange());
  }

  @Test
  public void testDividesLargeFile() {
    divider.setBlockSize(400);

    List<FileSection> parts = divider.split(Arrays.asList(fileA), 2);

    Assert.assertEquals(2, parts.size());

    FileSection sectionA = parts.get(0);
    assertEquals(fileA, sectionA.getFile());
    assertEquals(new LongRange(0, 400), sectionA.getLongRange());

    FileSection sectionB = parts.get(1);
    assertEquals(fileA, sectionB.getFile());
    assertEquals(new LongRange(400, 768), sectionB.getLongRange());
  }

  @Test
  public void testDividesOall() {
    divider = new DefaultFileDivider() {
      @Override
      protected long getFileLength(File f) {
        return 45275432643l;
      }
    };

    List<FileSection> parts = divider.split(Arrays.asList(fileA), 32);

    Assert.assertEquals(169, parts.size());
    Assert.assertEquals(new LongRange(45097156440l, 45275432643l), parts.get(parts.size() - 1).getLongRange());

    System.out.println(parts.get(parts.size() - 1).getLongRange());
  }

  @Test
  public void testDividesOallWithMaximum() {
    divider = new DefaultFileDivider() {
      @Override
      protected long getFileLength(File f) {
        return 45275432643l;
      }
    };

    divider.setMaximumBlockSize(100000000l);

    List<FileSection> parts = divider.split(Arrays.asList(fileA), 32);

    Assert.assertEquals(453, parts.size());
    Assert.assertEquals(new LongRange(45200000000l, 45275432643l), parts.get(parts.size() - 1).getLongRange());
  }

}
