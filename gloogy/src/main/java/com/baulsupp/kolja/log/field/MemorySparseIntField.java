package com.baulsupp.kolja.log.field;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.collections.primitives.IntList;

import com.baulsupp.kolja.log.util.IntRange;

// TODO easy optimisations by caching range start points
// TODO optimise by returning multiple next() results in array.

public class MemorySparseIntField implements SparseIntField {
  private List<MemoryIntField> knownFields = new ArrayList<MemoryIntField>(100);

  private IntRange totalRange = new IntRange();

  public MemorySparseIntField() {
  }

  public boolean isKnown(IntRange range) {
    MemoryIntField field = findEnclosingField(range);
    return field != null;
  }

  private MemoryIntField findEnclosingField(IntRange range) {
    assert (!range.isEmpty());

    for (MemoryIntField currentField : knownFields) {
      IntRange currentRange = currentField.getRange();

      if (currentRange.isCompletelyBefore(range))
        continue;

      if (currentRange.isCompletelyAfter(range))
        return null;

      if (range.isInclusiveSubsetOf(currentRange))
        return currentField;
    }

    return null;
  }

  public MemoryIntField findEnclosingField(int point) {
    assert (point >= 0);

    for (MemoryIntField currentField : knownFields) {
      IntRange currentRange = currentField.getRange();

      if (currentRange.isBefore(point))
        continue;

      if (currentRange.contains(point)) {
        return currentField;
      }

      if (currentRange.isAfter(point))
        return null;
    }

    return null;
  }

  public void add(MemoryIntField newField) {
    if (!isCompletelyUnknown(newField.getRange())) {
      throw new IllegalArgumentException("not completely unknown" + newField);
    }

    if (totalRange.isEmpty()) {
      knownFields.add(newField);
      totalRange = new IntRange(newField.getRange());
      return;
    }

    IntRange newRange = newField.getRange();

    if (newRange.getFrom() < totalRange.getFrom())
      totalRange.setFrom(newRange.getFrom());

    ListIterator<MemoryIntField> i = knownFields.listIterator();
    while (i.hasNext()) {
      MemoryIntField currentField = i.next();
      IntRange currentRange = currentField.getRange();

      boolean meets = currentRange.meets(newRange);

      if (!meets && newRange.isCompletelyBefore(currentRange)) {
        knownFields.add(i.previousIndex(), newField);
        return;
      }

      if (meets) {
        newField = newField.merge(currentField);
        newRange = newField.getRange();

        if (i.hasNext()) {
          MemoryIntField nextField = i.next();
          if (nextField.getRange().meets(newRange)) {
            newField = newField.merge(nextField);
            i.remove();
          }
          i.previous();
        }

        i.set(newField);

        return;
      }
    }

    knownFields.add(newField);
    totalRange.setTo(newRange.getTo());
  }

  private boolean isCompletelyUnknown(IntRange range) {
    IntRange[] unknown = listUnknownRanges(range);

    if (unknown.length != 1)
      return false;

    return range.equals(unknown[0]);
  }

  public IntRange[] listUnknownRanges(IntRange range) {
    ArrayList<IntRange> list = new ArrayList<IntRange>();

    // log.info("" + range);

    int from = range.getFrom();
    int to = range.getTo();

    for (MemoryIntField currentField : knownFields) {
      IntRange currentRange = currentField.getRange();

      // TODO check this doesn't break things
      if (currentRange.isBefore(range.getFrom())) {
        continue;
      }
      if (currentRange.isAfter(range.getTo())) {
        break;
      }

      if (currentRange.isAfter(from)) {
        // log.info("a from " + from + " to " + currentRange.getFrom());
        list.add(new IntRange(from, currentRange.getFrom()));
      }

      from = currentRange.getTo();

      if (from >= to)
        break;
    }

    if (from < to) {
      // log.info("b from " + from + " to " + to);
      list.add(new IntRange(from, to));
    }

    return list.toArray(new IntRange[list.size()]);
  }

  public IntRange getTotalRange() {
    return totalRange;
  }

  // TODO should this return null
  public IntList get(IntRange region) {
    MemoryIntField field = findEnclosingField(region);
    return field != null ? field.get(region) : null;
  }

  public String toString() {
    return knownFields.toString();
  }
}
