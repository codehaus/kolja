package com.baulsupp.kolja.log.line;

import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.baulsupp.kolja.log.util.IntRange;

// TODO optimise for paging up and down. i.e. don't stick to boundaries!
public class BasicLineIterator implements LineIterator {
  private static final Logger log = LoggerFactory.getLogger(BasicLineIterator.class);

  private LineIndex index;

  private IntRange currentRange = null;

  private ListIterator<Line> currentIterator = null;

  private IntRange intRange;

  private static final int BUFFER = 4096;

  public BasicLineIterator(LineIndex index) {
    this(index, null);
  }

  public BasicLineIterator(LineIndex index, IntRange intRange) {
    this.index = index;
    this.intRange = intRange;

    if (intRange != null) {
      Assert.isTrue(intRange.getFrom() >= 0);
      Assert.isTrue(intRange.getTo() >= intRange.getFrom());
    }

    moveToStart();
  }

  public boolean hasNext() {
    boolean hasNext = currentIterator.hasNext();

    while (!hasNext) {
      int length = maximumOffset();

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

    return hasNext;
  }

  public Line next() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }

    Line l = currentIterator.next();

    return l;
  }

  public void moveToEnd() {
    moveTo(maximumOffset());
  }

  private int maximumOffset() {
    int fileLength = index.length();

    if (intRange != null) {
      return Math.min(fileLength, intRange.getTo());
    }

    return fileLength;
  }

  public void moveToStart() {
    moveTo(minimumOffset());
  }

  private int minimumOffset() {
    if (intRange != null) {
      return intRange.getFrom();
    }

    return 0;
  }

  private List<Line> EMPTY_LIST = Collections.emptyList();

  public void moveTo(int position) {
    currentIterator = EMPTY_LIST.listIterator();
    currentRange = new IntRange(position, position);
  }

  public boolean hasPrevious() {
    boolean hasPrevious = currentIterator.hasPrevious();

    int minimumOffset = minimumOffset();
    while (!hasPrevious && currentRange.getFrom() > minimumOffset) {
      int newFrom = Math.max(minimumOffset, currentRange.getFrom() - BUFFER);
      int newTo = currentRange.getFrom();

      currentRange = new IntRange(newFrom, newTo);

      log.debug("new range " + currentRange);

      List<Line> list = index.get(currentRange);
      currentIterator = list.listIterator(list.size());

      hasPrevious = currentIterator.hasPrevious();
    }

    return hasPrevious;
  }

  public Line previous() {
    if (!hasPrevious()) {
      throw new NoSuchElementException();
    }

    Line l = currentIterator.previous();

    return l;
  }

  public void remove() {
    throw new UnsupportedOperationException();
  }
}
