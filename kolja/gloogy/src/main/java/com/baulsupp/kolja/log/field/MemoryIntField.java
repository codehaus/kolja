package com.baulsupp.kolja.log.field;

import org.apache.commons.collections.primitives.IntCollections;
import org.apache.commons.collections.primitives.IntList;

import com.baulsupp.kolja.log.util.CollectionSearch;
import com.baulsupp.kolja.log.util.IntRange;

public class MemoryIntField implements IntField {
  private IntRange range;

  private IntList values;

  public MemoryIntField(IntRange range, IntList values) {
    this.range = range;
    this.values = values;
  }

  // TODO addAll rather than merge, reduce number of increments
  public MemoryIntField merge(MemoryIntField other) {
    IntRange newRange = range.merge(other.range);

    IntList newList;

    if (range.getFrom() < other.range.getFrom()) {
      values.addAll(other.values);
      newList = values;
    } else {
      other.values.addAll(values);
      newList = other.values;
    }

    return new MemoryIntField(newRange, newList);
  }

  public IntRange getRange() {
    return range;
  }

  public IntList get(IntRange region) {
    assert (region.isInclusiveSubsetOf(range));

    if (values.isEmpty() || region.isEmpty())
      return IntCollections.EMPTY_INT_LIST;

    if (range.equals(region))
      return values.subList(0, values.size());

    int from = CollectionSearch.binarySearch(values, region.getFrom());

    if (!CollectionSearch.wasFound(from)) {
      from = CollectionSearch.insertionPoint(from);

      if (from == values.size())
        return IntCollections.EMPTY_INT_LIST;
    }

    int to = CollectionSearch.binarySearch(values, region.getTo());

    if (CollectionSearch.wasFound(to)) {
      assert (from <= to);
    } else {
      to = CollectionSearch.insertionPoint(to);
    }

    return values.subList(from, to);
  }

  public String toString() {
    return "[" + range + "](" + values.size() + ")";
  }
}
