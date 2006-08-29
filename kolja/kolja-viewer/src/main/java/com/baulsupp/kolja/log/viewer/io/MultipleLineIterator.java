package com.baulsupp.kolja.log.viewer.io;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.baulsupp.kolja.log.line.Line;

public class MultipleLineIterator implements Iterator<Line> {
  public static final String FILE_NAME = "filename";
  
  private List<NamedIterator> iterators = new ArrayList<NamedIterator>();

  public boolean hasNext() {
    for (NamedIterator i : iterators) {
      if (i.hasNext()) {
        return true;
      }
    }
    
    return false;
  }

  public Line next() {
    for (NamedIterator i : iterators) {
      if (i.hasNext()) {
        return i.next();
      }
    }
    
    return null;
  }

  public void remove() {
    throw new UnsupportedOperationException();
  }

  public void add(String filename, Iterator<Line> iterator) {
    iterators.add(new NamedIterator(filename, iterator));
  }
}
