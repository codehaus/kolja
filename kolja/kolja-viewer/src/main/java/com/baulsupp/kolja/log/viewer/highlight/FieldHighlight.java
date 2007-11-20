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

import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.util.colours.ColourPair;

public class FieldHighlight implements Highlight<Line> {
  private static final long serialVersionUID = -5580085902466682833L;

  private String fieldName;
  private Object value;

  private ColourPair colour;

  public FieldHighlight(ColourPair colour, String fieldName) {
    this.colour = colour;
    this.fieldName = fieldName;
  }

  public HighlightResult getHighlights(Line viewRow) {
    HighlightResult result = null;

    if (isDefined()) {
      Object rowValue = viewRow.getValue(fieldName);

      if (matches(rowValue)) {
        result = HighlightResult.column(fieldName, colour);
      }
    }

    return result;
  }

  private boolean matches(Object rowValue) {
    return rowValue != null && value.equals(rowValue);
  }

  private boolean isDefined() {
    return fieldName != null && value != null;
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
