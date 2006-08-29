package com.baulsupp.kolja.log.viewer.io;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.TreeSet;

import com.baulsupp.kolja.log.LogConstants;
import com.baulsupp.kolja.log.line.Line;

public class MultipleLineIterator implements Iterator<Line> {
  private List<NamedIterator> iterators = new ArrayList<NamedIterator>(20);

  private TreeSet<Line> availableLines = new TreeSet<Line>(new LineComparator(LogConstants.DATE, LogConstants.FILE_NAME));
  
  public boolean hasNext() {
    if (!availableLines.isEmpty()) {
      return true;
    }
    
    loadAhead();
    
    return !availableLines.isEmpty();
  }

  private void loadAhead() {
    if (availableLines.size() < 1000) {
      for (NamedIterator i : iterators) {
        if (i.hasNext()) {
          availableLines.add(i.next());
        }
        if (i.hasNext()) {
          availableLines.add(i.next());
        }
      }
    }
  }

  public Line next() {
    loadAhead();   
    if (availableLines.isEmpty()) {
      throw new NoSuchElementException();
    } else { 
      Iterator<Line> i = availableLines.iterator();
      Line next = i.next();
      i.remove();
      return next;
    }
  }

  public void remove() {
    throw new UnsupportedOperationException();
  }

  public void add(String filename, Iterator<Line> iterator) {
    iterators.add(new NamedIterator(filename, iterator));
  }
}
