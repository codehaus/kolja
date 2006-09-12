package com.baulsupp.kolja.log.viewer.highlight;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.util.ColourPair;
import com.baulsupp.kolja.util.ColourRotator;

public class RotatingHighlight implements Highlight<Line>, Serializable {
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
