package com.baulsupp.kolja.log.viewer.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StreamBufferingStringBuilder extends BufferingStringBuilder {
  private InputStream stream;

  public StreamBufferingStringBuilder(InputStream in) {
    this.reader = new InputStreamReader(in);
    this.stream = in;
  }

  @Override
  public void readAhead() throws IOException {
    int toRead = Math.min(stream.available(), 8192 - readContent.length());
    
    char[] buffy = new char[toRead];
    int read = reader.read(buffy);
    
    readContent.append(buffy, 0, read);
  }
}
