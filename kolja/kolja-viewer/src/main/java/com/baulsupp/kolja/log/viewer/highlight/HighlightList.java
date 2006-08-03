package com.baulsupp.kolja.log.viewer.highlight;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HighlightList<T> implements Highlight<T>, Serializable {
  private static final long serialVersionUID = 4473793483886967535L;

  private List<Highlight<T>> highlights = new ArrayList<Highlight<T>>(5);

  public HighlightList() {
  }

  public List<Highlight<T>> getHighlights() {
    return highlights;
  }

  public void setHighlights(List<Highlight<T>> highlights) {
    this.highlights = highlights;
  }

  public HighlightList(Highlight<T> highlight) {
    addHighlight(highlight);
  }

  public void addHighlight(Highlight<T> h) {
    if (h == null) {
      throw new NullPointerException();
    }
    
    highlights.add(h);
  }

  public HighlightResult getHighlights(T viewRow) {
    HighlightResult result = null;
    for (Highlight<T> h : highlights) {
      HighlightResult extra = h.getHighlights(viewRow);
      if (extra != null) {
        if (result == null) {
          result = new HighlightResult();
        }
        result.combine(extra);
      }
    }
    return result;
  }
}
