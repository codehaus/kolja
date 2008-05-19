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

import java.util.HashMap;
import java.util.Map;

import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.util.colours.ColourPair;
import com.baulsupp.kolja.util.colours.ColourRotator;

public class RotatingHighlight implements Highlight<Line> {
  private static final long serialVersionUID = 2264643873100280184L;

  private Map<Object, ColourPair> colours = new HashMap<Object, ColourPair>();

  private ColourRotator colourRotator = null;

  private String field;

  public RotatingHighlight() {
    this.colourRotator = new ColourRotator();
  }

  public RotatingHighlight(String field) {
    this.colourRotator = new ColourRotator();
    this.field = field;
  }

  public RotatingHighlight(String field, ColourPair... colours) {
    this.colourRotator = new ColourRotator(colours);
    this.field = field;
  }

  public HighlightResult getHighlights(Line viewRow) {
    String name = (String) viewRow.getValue(field);

    if (name != null) {
      return HighlightResult.column(field, getColour(name));
    }

    return null;
  }

  private ColourPair getColour(Object name) {
    ColourPair result = colours.get(name);

    if (result == null) {
      result = colourRotator.next();
      colours.put(name, result);
    }

    return result;
  }
}
