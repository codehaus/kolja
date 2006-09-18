package com.baulsupp.kolja.log.viewer.request;

import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.viewer.highlight.Highlight;
import com.baulsupp.kolja.log.viewer.highlight.HighlightResult;
import com.baulsupp.kolja.util.colours.ColourPair;

public class RequestHighlight implements Highlight<Line> {
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
