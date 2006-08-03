package com.baulsupp.kolja.log.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ReloadableCharBuffer extends WrappedCharBuffer {
  private File file;

  public static ReloadableCharBuffer fromFileReloadable(File f) throws IOException {
    return new ReloadableCharBuffer(f);
  }

  private static ByteBuffer getByteBuffer(File f) throws FileNotFoundException, IOException {
    FileInputStream fis = new FileInputStream(f);
    ByteBuffer bb = fis.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, fis.available());
    return bb;
  }

  public ReloadableCharBuffer(File file) throws IOException {
    super(getByteBuffer(file));
    this.file = file;
  }

  public void reload() throws IOException {
    boolean isTruncated = false;

    if (file.length() < this.length()) {
      isTruncated = true;
    }

    setBuffer(getByteBuffer(file));

    if (isTruncated) {
      throw new TruncationException();
    }
  }

  public boolean hasFileGrown() {
    return this.length() != file.length();
  }
}
