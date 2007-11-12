package com.baulsupp.kolja.log.line;

import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baulsupp.kolja.log.util.IntRange;

// TODO optimise for paging up and down. i.e. don't stick to boundaries!
public class BasicLineIterator implements LineIterator {
  private static final Logger log = LoggerFactory.getLogger(BasicLineIterator.class);

  private LineIndex index;

  private IntRange currentRange = null;

  private ListIterator<Line> currentIterator = null;

  private static final int BUFFER = 4096;

  public BasicLineIterator(LineIndex index) {
    this.index = index;

    int to = Math.min(BUFFER, index.length());
    currentRange = new IntRange(0, to);
    
    if (to > 0) {
      currentIterator = index.get(currentRange).listIterator();
    } else {
      List<Line> emptyList = Collections.emptyList();
      currentIterator = emptyList.listIterator();
    }
  }
  
  public boolean hasNext() {
    boolean hasNext = currentIterator.hasNext();

    while (!hasNext) {
      int length = index.length();

      int newFrom = currentRange.getTo();
      int newTo = Math.min(length, newFrom + BUFFER);

      if (newFrom < newTo) {
        currentRange = new IntRange(newFrom, newTo);

        currentIterator = index.get(currentRange).listIterator();

        hasNext = currentIterator.hasNext();
      } else {
        break;
      }
    }

    // log.debug("hasNext " + hasNext);

    return hasNext;
  }

  public Line next() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }

    Line l = currentIterator.next();

    // log.debug("next " + l.getOffset());

    return l;
  }

  // TODO iterator that can move to end, or swap half way
  // TODO optimise
  public void moveToEnd() {
    moveTo(index.length());
  }

  public void moveToStart() {
    moveTo(0);
  }

  private List<Line> EMPTY_LIST = Collections.emptyList();

  // TODO optimise!!!!
  // TODO should we move half way, even though its expensive to do. Or edge?
  public void moveTo(int position) {
    // log.debug("moveTo " + position);
    currentIterator = EMPTY_LIST.listIterator();
    currentRange = new IntRange(position, position);
    // log.debug("resetting " + currentRange + "");
  }

  public boolean hasPrevious() {
    boolean hasPrevious = currentIterator.hasPrevious();

    while (!hasPrevious && currentRange.getFrom() > 0) {
      int newFrom = Math.max(0, currentRange.getFrom() - BUFFER);
      int newTo = currentRange.getFrom();

      currentRange = new IntRange(newFrom, newTo);

      log.debug("new range " + currentRange);

      List<Line> list = index.get(currentRange);
      currentIterator = list.listIterator(list.size());

      hasPrevious = currentIterator.hasPrevious();
    }

    // log.debug("hasPrevious " + hasPrevious);

    return hasPrevious;
  }

  public Line previous() {
    if (!hasPrevious()) {
      throw new NoSuchElementException();
    }

    Line l = currentIterator.previous();

    // log.debug("previous " + l.getOffset());

    return l;
  }

  public void remove() {
    throw new UnsupportedOperationException();
  }
}
