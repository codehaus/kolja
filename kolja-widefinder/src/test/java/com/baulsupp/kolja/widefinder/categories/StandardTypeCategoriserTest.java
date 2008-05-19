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
package com.baulsupp.kolja.widefinder.categories;

import static junit.framework.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Yuri Schimke
 * 
 */
public class StandardTypeCategoriserTest {
  protected StandardTypeCategoriser typeCategoriser;

  @Before
  public void setup() {
    typeCategoriser = createCategoriser();
  }

  protected StandardTypeCategoriser createCategoriser() {
    return new StandardTypeCategoriser();
  }

  @Test
  public void testExtension() {
    assertEquals("atom", typeCategoriser.getExtension("/ongoing/ongoing.atom"));
    assertEquals("txt", typeCategoriser.getExtension("/ongoing/ongoing.txt"));
  }

  @Test
  public void testExtensionWithQuery() {
    assertEquals("xml", typeCategoriser
        .getExtension("/ongoing/picInfo.xml?o=http://www.tbray.org/ongoing/When/200x/2004/10/01/AutumnLeaves"));
  }

  @Test
  public void testAtomFeed() {
    FileType type = typeCategoriser.getFileType("/ongoing/ongoing.atom");

    assertEquals("application/atom+xml", type.getType());
    assertEquals("Feed", type.getCategory());
  }

  @Test
  public void testTextFile() {
    FileType type = typeCategoriser.getFileType("/ongoing/ongoing.txt");

    assertEquals("text/plain", type.getType());
    assertEquals("Text", type.getCategory());
  }

  @Test
  public void testUnknown() {
    FileType type = typeCategoriser.getFileType("/ongoing/ongoing");

    assertEquals(null, type.getType());
    assertEquals("Unknown", type.getCategory());
  }

  @Test
  public void testOngoingPage() {
    FileType type = typeCategoriser.getFileType("/ongoing/When/200x/2006/06/15/Switch-From-Mac");

    assertEquals(null, type.getType());
    assertEquals("Unknown", type.getCategory());
  }

  @Test
  public void testOngoingImage() {
    FileType type = typeCategoriser.getFileType("/ongoing/When/200x/2004/10/01/IMG_2686.png");

    assertEquals("image/png", type.getType());
    assertEquals("Image", type.getCategory());
  }
}
