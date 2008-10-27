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

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.util.zip.GZIPInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuri Schimke
 */
public class GzipFileSequence {
  private static final Logger log = LoggerFactory.getLogger(GzipFileSequence.class);

  public static ChunkedFileSequence create(File file, Charset cs) throws Exception {
    return create(file, cs, 0);
  }

  public static ChunkedFileSequence create(File file, Charset cs, int initialOffset) throws Exception {
    FileInputStream fileInputStream = new FileInputStream(file);
    GZIPInputStream is = new GZIPInputStream(fileInputStream);

    if (initialOffset != 0) {
      log.warn("skipping bytes of a GZip stream");
    }

    return new ChunkedFileSequence(is, ChunkedFileSequence.MB, cs, initialOffset);
  }

}
