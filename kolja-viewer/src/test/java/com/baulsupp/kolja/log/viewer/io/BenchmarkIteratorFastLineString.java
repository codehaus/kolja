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
package com.baulsupp.kolja.log.viewer.io;

import java.io.BufferedReader;
import java.io.FileReader;

import com.baulsupp.kolja.log.line.LineIterator;
import com.baulsupp.kolja.log.viewer.io.fast.FastLineIterator;

/**
 * @author Yuri Schimke
 * 
 */
public class BenchmarkIteratorFastLineString extends BenchmarkIterator {
  public static void main(String[] args) throws Exception {
    BenchmarkIteratorFastLineString benchmark = new BenchmarkIteratorFastLineString();

    benchmark.run();
  }

  protected LineIterator createIterator() throws Exception {
    CharSequence content = readFileAsString();

    return new FastLineIterator(pattern, content, lineParser);
  }

  private StringBuffer readFileAsString() throws java.io.IOException {
    StringBuffer fileData = new StringBuffer(50000);
    BufferedReader reader = new BufferedReader(new FileReader(file));
    char[] buf = new char[32768];
    int numRead = 0;
    while ((numRead = reader.read(buf)) != -1) {
      fileData.append(buf, 0, numRead);
    }
    reader.close();

    return fileData;
  }
}
