package com.baulsupp.kolja.log.filter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.line.LineIndex;
import com.baulsupp.kolja.log.line.LineListener;
import com.baulsupp.kolja.log.util.IntRange;

// add caching for LineIndex or Filter

public class FilteredLineIndex implements LineIndex {
  private Filter filter;

  private LineIndex lineIndex;

  private Set<LineListener> listeners = new HashSet<LineListener>();

  public FilteredLineIndex(LineIndex lineIndex, Filter filter) {
    this.lineIndex = lineIndex;
    this.filter = filter;
  }

  public List<Line> get(IntRange region) {
    List<Line> lines = lineIndex.get(region);

    List<Line> newLines = new ArrayList<Line>();

    for (Line l : lines) {
      if (filter.lineMatches(l))
        newLines.add(l);
    }

    fireListeners(region, newLines);

    return newLines;
  }

  public void fireListeners(IntRange region, List<Line> lines) {
    if (listeners.size() > 0) {
      for (LineListener listener : listeners) {
        listener.linesAvailable(region, lines);
      }
    }
  }

  public void addLineListener(LineListener listener) {
    listeners.add(listener);
  }

  public void removeLineListener(LineListener listener) {
    listeners.remove(listener);
  }

  public int length() {
    return lineIndex.length();
  }
}
