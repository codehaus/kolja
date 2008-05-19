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
package com.baulsupp.kolja.log.viewer.request;

import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.viewer.highlight.Highlight;
import com.baulsupp.kolja.log.viewer.highlight.HighlightResult;
import com.baulsupp.kolja.util.colours.ColourPair;

public class RequestHighlight implements Highlight<Line> {
  private static final long serialVersionUID = -1190636041437396063L;

  private RequestIndex requestIndex;
  private Object value;

  private ColourPair colour;
  private String highlightField;

  public RequestHighlight(ColourPair colour, RequestIndex requestIndex, String highlightField) {
    this.colour = colour;
    this.requestIndex = requestIndex;
    this.highlightField = highlightField;
  }

  public HighlightResult getHighlights(Line viewRow) {
    HighlightResult result = null;

    if (isDefined()) {
      Object rowValue = requestIndex.getRequestIdentifier(viewRow);

      if (matches(rowValue)) {
        if (highlightField != null) {
          result = HighlightResult.column(highlightField, colour);
        } else {
          result = HighlightResult.row(colour);
        }
      }
    }

    return result;
  }

  private boolean matches(Object rowValue) {
    return rowValue != null && value.equals(rowValue);
  }

  private boolean isDefined() {
    return requestIndex != null && value != null;
  }

  public void clear() {
    this.value = null;
  }

  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }
}
