package com.baulsupp.kolja.log.viewer.io;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.line.LineParser;
import com.baulsupp.kolja.log.viewer.importing.ConfigurableLineFormat;
import com.baulsupp.kolja.log.viewer.importing.ConfigurableLogFormat;
import com.baulsupp.kolja.log.viewer.importing.LogFormat;

public class FileLineIterator implements Iterator<Line> {
  private boolean tailing;
  
  private Matcher entryMatcher;
  private BufferingStringBuilder content;
  private LineParser lineParser;

  public FileLineIterator(BufferingStringBuilder content, Pattern entryPattern, LineParser lineParser) throws IOException {
    this.entryMatcher = entryPattern.matcher("");
    this.lineParser = lineParser;
    this.content = content;
  }

  public static FileLineIterator load(LogFormat format, File file) throws IOException {
    Pattern entryPattern = format.getEntryPattern();
    LineParser lineParser = format.getLineParser();

    BufferingStringBuilder content = new FileBufferingStringBuilder(file);
    return new FileLineIterator(content, entryPattern, lineParser);
  }
  
  public static FileLineIterator loadFromStdin(LogFormat format) throws IOException {
    ConfigurableLineFormat lineFormat = ((ConfigurableLogFormat) format).getLineFormat();
    
    Pattern entryPattern = lineFormat.getEntryPattern();

    BufferingStringBuilder content = new StreamBufferingStringBuilder(System.in);
    return new FileLineIterator(content, entryPattern, lineFormat.getLineParser());
  }

  public boolean hasNext() {
    if (tailing) {
      return true;
    } else {
      readAhead();

      return !content.isEmpty();
    }
  }

  public Line next() {
    readAhead();

    if (tailing) {
      content.waitFor();
    } else if (content.isEmpty()) {
      throw new NoSuchElementException();
    }

    String lineString;
    entryMatcher.reset(content);
    if (entryMatcher.find(1)) {
      lineString = content.trim(entryMatcher.start());
    } else {
      lineString = content.trim();
    }
    
    // TODO is this needed?
    lineString = lineString.trim();

    return lineParser.parse(lineString);
  }

  private void readAhead() {
    try {
      content.readAhead();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void remove() { 
    throw new UnsupportedOperationException();
  }

  public void setTailing(boolean b) {
    this.tailing = b;
  }

  public boolean isTailing() {
    return tailing;
  }
}
