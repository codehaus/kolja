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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.baulsupp.kolja.log.util.LongRange;

/**
 * @author Yuri Schimke
 * 
 */
public class DefaultFileDivider implements FileDivider {
  // MB
  private int blockSize = 2 * 1024 * 1024;

  public void setBlockSize(int blockSize) {
    this.blockSize = blockSize;
  }

  public List<FileSection> split(List<File> files, int workers) {
    List<FileSection> result = new ArrayList<FileSection>();

    for (File f : files) {
      if (!f.exists()) {
        throw new IllegalArgumentException("file missing: " + f);
      }

      long length = f.length();

      if (length <= blockSize) {
        result.add(new FileSection(f, null));
      } else {
        long offset = 0;

        long chunkSize = length / workers + 1;
        chunkSize = Math.max(blockSize, chunkSize);
        chunkSize = Math.min(Integer.MAX_VALUE / 2, chunkSize);

        for (int i = 0; i < workers; i++) {
          if (offset < length) {
            long to = Math.min(offset + chunkSize, length);
            result.add(new FileSection(f, new LongRange(offset, to)));
            offset = to;
          }
        }
      }
    }

    return result;
  }
}
