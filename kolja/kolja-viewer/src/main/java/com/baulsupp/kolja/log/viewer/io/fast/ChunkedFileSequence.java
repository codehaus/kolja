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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baulsupp.kolja.log.util.IntRange;

/**
 * @author Yuri Schimke
 */
public class ChunkedFileSequence implements CharSequence {
  private static final Logger log = LoggerFactory.getLogger(ChunkedFileSequence.class);

  public static final int MB = 1024 * 1024;

  private int chunkSize;

  private Reader fileReader;

  private char[] chunkOne;
  private char[] chunkTwo;
  private char[] chunkThree;

  private int offset = 0;
  private int chunkOneAvailable = 0;
  private int chunkTwoAvailable = 0;
  private int chunkThreeAvailable = 0;
  private boolean finished;

  public static ChunkedFileSequence create(File file, Charset cs) throws Exception {
    return create(file, cs, 0);
  }

  public static ChunkedFileSequence create(File file, Charset cs, int initialOffset) throws Exception {
    return create(file, ChunkedFileSequence.MB, cs, initialOffset);
  }

  public static ChunkedFileSequence create(File file, int chunkSize, Charset cs, int initialOffset) throws Exception {
    FileInputStream fileInputStream = new FileInputStream(file);

    return new ChunkedFileSequence(fileInputStream, chunkSize, cs, initialOffset);
  }

  public ChunkedFileSequence(InputStream inputStream, int chunkSize, Charset cs, int initialOffset) throws Exception {
    this.chunkSize = chunkSize;

    boolean skipReader = false;
    if (initialOffset != 0) {
      if (isSingleByteEncoding(cs)) {
        inputStream.skip(initialOffset);
      } else {
        skipReader = true;
      }
    }

    this.fileReader = new InputStreamReader(inputStream, cs);

    if (skipReader) {
      log.warn("skipping bytes of a Decoded Reader");
      this.fileReader.skip(initialOffset);
    }

    this.offset = initialOffset;

    readInitialChunks();
  }

  private boolean isSingleByteEncoding(Charset cs) {
    return cs.newDecoder().averageCharsPerByte() == 1;
  }

  public char charAt(int index) {
    if (index < offset) {
      throw new IndexOutOfBoundsException("index " + index + " current range " + new IntRange(offset, chunkThreeAvailable));
    }

    if (index < chunkOneAvailable) {
      return chunkOne[index - offset];
    }

    if (index < chunkTwoAvailable) {
      return chunkTwo[index - chunkOneAvailable];
    }

    if (index < chunkThreeAvailable) {
      char c = chunkThree[index - chunkTwoAvailable];

      moveForward();

      return c;
    }

    throw new IndexOutOfBoundsException();
  }

  public int length() {
    return chunkThreeAvailable;
  }

  public CharSequence subSequence(int start, int end) {
    if (start < offset) {
      throw new IndexOutOfBoundsException();
    }

    if (end > chunkThreeAvailable) {
      throw new IndexOutOfBoundsException();
    }

    StringBuilder builder = new StringBuilder(end - start);

    if (start < chunkOneAvailable) {
      int from = start - offset;
      int to = Math.min(end, chunkOneAvailable) - offset - from;
      builder.append(chunkOne, from, to);
    }

    if ((start < chunkTwoAvailable && end > chunkOneAvailable)) {
      int from = Math.max(start - chunkOneAvailable, 0);
      int to = Math.min(end, chunkTwoAvailable) - chunkOneAvailable - from;
      builder.append(chunkTwo, from, to);
    }

    if (end > chunkTwoAvailable) {
      int from = Math.max(start - chunkTwoAvailable, 0);
      int to = end - chunkTwoAvailable - from;
      builder.append(chunkThree, from, to);
    }

    return builder;
  }

  private void readInitialChunks() throws IOException {
    chunkOne = new char[chunkSize];
    chunkOneAvailable = readChunk(chunkOne) + offset;
    chunkTwo = new char[chunkSize];
    chunkTwoAvailable = chunkOneAvailable + readChunk(chunkTwo);
    chunkThree = new char[chunkSize];
    chunkThreeAvailable = chunkTwoAvailable + readChunk(chunkThree);
  }

  private int readChunk(char[] chunk) throws IOException {
    int read = fileReader.read(chunk);

    return read == -1 ? 0 : read;
  }

  private void moveForward() {
    if (finished) {
      return;
    }

    char[] poppedChunk = chunkOne;
    chunkOne = chunkTwo;
    chunkTwo = chunkThree;
    chunkThree = poppedChunk;

    int read;
    try {
      read = readChunk(chunkThree);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    if (read == 0) {
      finished = true;
    }

    offset = chunkOneAvailable;
    chunkOneAvailable = chunkTwoAvailable;
    chunkTwoAvailable = chunkThreeAvailable;
    chunkThreeAvailable = chunkThreeAvailable + read;

  }
}
