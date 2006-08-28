package com.baulsupp.kolja.log.viewer.io;

import java.io.IOException;
import java.io.Reader;

public abstract class BufferingStringBuilder implements CharSequence {
  protected StringBuilder readContent = new StringBuilder();
  protected Reader reader = null;
  
  public BufferingStringBuilder() {
  }

  public abstract void readAhead() throws IOException;

  public char charAt(int arg0) {
    return readContent.charAt(arg0);
  }

  public int length() {
    return readContent.length();
  }

  public CharSequence subSequence(int arg0, int arg1) {
    return readContent.subSequence(arg0, arg1);
  }

  public boolean isEmpty() {
    return readContent.length() == 0;
  }

  public void waitFor() throws IOException {
    while (readContent.length() == 0) {
      readAhead();
      
      if (readContent.length() > 0) {
        break;
      }
        
      try {
        Thread.sleep(250);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public String trim(int i) {
    String s = readContent.substring(0, i).toString();
    
    readContent.delete(0, i);
    
    return s;
  }

  public String trim() {
    String s = readContent.toString();
    
    readContent.setLength(0);
    
    return s;
  }
}
