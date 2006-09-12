package com.baulsupp.kolja.log.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author yuri
 */
public class WrappedCharBuffer implements Cloneable, CharSequence {
  private ByteBuffer buffer;

  private byte[] array;

  protected WrappedCharBuffer(ByteBuffer buffer) {
    setBuffer(buffer);
  }

  protected final void setBuffer(ByteBuffer buffer) {
    this.buffer = buffer;

    if (buffer.hasArray())
      array = buffer.array();
    else
      array = null;
  }

  public static WrappedCharBuffer fromSingleByteEncoding(ByteBuffer buffer) {
    return new WrappedCharBuffer(buffer);
  }

  public boolean isReadOnly() {
    return buffer.isReadOnly();
  }

  public int length() {
    return buffer.limit();
  }

  public char charAt(int index) {
    char c;
    if (array == null) {
      c = (char) (buffer.get(index) & 0xff);
    } else {
      c = (char) (array[index] & 0xff);
    }
    return c;
  }

  public String toString() {
    int l = length();

    StringBuilder sb = new StringBuilder(l);

    if (array != null) {
      for (int i = 0; i < l; i++) {
        sb.append((char) (array[i] & 0xff));
      }
    } else {
      for (int i = 0; i < l; i++) {
        sb.append((char) (buffer.get(i) & 0xff));
      }
    }

    String s = sb.toString();

    return s;
  }

  public CharSequence subSequence(int start, int end) {
    return new LimitedCharBuffer(this, start, end);
  }

  public static WrappedCharBuffer fromFile(File f) throws IOException {
    FileInputStream fis = new FileInputStream(f);
    ByteBuffer bb = fis.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, fis.available());

    return WrappedCharBuffer.fromSingleByteEncoding(bb);
  }
}
