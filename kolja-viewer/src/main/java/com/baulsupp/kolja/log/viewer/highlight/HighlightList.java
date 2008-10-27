/**
 * Copyright (c) 2002-2007 Yuri Schimke. All Rights Reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package com.baulsupp.kolja.log.viewer.highlight;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * List of highlighters.
 * 
 * @author Yuri Schimke
 * 
 * @param <T>
 *          the type of the element being highlighted.
 */
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

  public int size() {
    return highlights.size();
  }
}
