package com.baulsupp.kolja.log.viewer.highlight;

import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.util.ColourPair;

public class FieldHighlight implements Highlight<Line> {
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
