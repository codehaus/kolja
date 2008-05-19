package com.baulsupp.kolja.log.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class SystemInCharSequence implements CharSequence {
  private Reader reader;

  private StringBuilder buffy = new StringBuilder();

  public SystemInCharSequence(InputStream is) {
    this.reader = new InputStreamReader(is);
  }

  public synchronized int length() {
    return buffy.length();
  }

  public synchronized char charAt(int arg0) {
    return buffy.charAt(arg0);
  }

  public synchronized CharSequence subSequence(int arg0, int arg1) {
    return buffy.subSequence(arg0, arg1);
  }

  @Override
  public synchronized String toString() {
    return buffy.toString();
  }

  public void readFully() throws IOException {
    char[] chars = new char[4096];

    while (true) {
      int read = reader.read(chars);

      if (read == -1) {
        break;
      }

      append(chars, read);
    }
  }

  private synchronized void append(char[] chars, int read) {
    buffy.append(chars, 0, read);
  }
}
