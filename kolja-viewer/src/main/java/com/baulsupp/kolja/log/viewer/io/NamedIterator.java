package com.baulsupp.kolja.log.viewer.io;

import java.util.Iterator;

import com.baulsupp.kolja.log.LogConstants;
import com.baulsupp.kolja.log.line.Line;

public class NamedIterator implements Iterator<Line> {
  private Iterator<Line> delegate;
  private String name;

  public NamedIterator(String filename, Iterator<Line> iterator) {
    this.name = filename;
    this.delegate = iterator;
  }

  public boolean hasNext() {
    return delegate.hasNext();
  }

  public Line next() {
    Line l = delegate.next();
    
    l.setValue(LogConstants.FILE_NAME, name);
    
    return l;
  }

  public void remove() {
    delegate.remove();
  }
  
  public String getName() {
    return name;
  }
}