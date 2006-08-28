package com.baulsupp.kolja.log.viewer.io;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileBufferingStringBuilder extends BufferingStringBuilder {
  private File file;
  private long fileLength = 0;
  private long position = 0;

  public FileBufferingStringBuilder(File file) throws IOException {
    this.file = file;
    openFile();
  }

  private void openFile() throws IOException {
    this.fileLength = file.length();
    this.reader = new FileReader(file);
    
    reader.skip(position);    
  }
  
  public void readAhead() throws IOException {
      if (readContent.length() < 8192) {
        openFileIfNeeded();
        
        char[] buffer = new char[8192];

        int read = reader.read(buffer);

        if (read > 0) {
          readContent.append(buffer, 0, read);
          position += read;
        }
      }
  }

  protected void openFileIfNeeded() throws IOException {
    if (file.length() > fileLength) {
      openFile();
    }
  }


}
