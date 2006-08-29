package com.baulsupp.kolja.log.viewer.highlight;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.baulsupp.kolja.log.LogConstants;
import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.util.ColourPair;
import com.baulsupp.kolja.util.ColourRotator;

public class FilenameHighlight implements Highlight<Line>, Serializable {
  private static final long serialVersionUID = 2264643873100280184L;
  
  private Map<String, ColourPair> colours = new HashMap<String, ColourPair>();
  
  private ColourRotator colourRotator = null;
    
  public FilenameHighlight() {
    this.colourRotator = new ColourRotator();
  }

  public FilenameHighlight(ColourPair... colours) {
    this.colourRotator = new ColourRotator(colours);
  }

  public HighlightResult getHighlights(Line viewRow) {
    String name = (String) viewRow.getValue(LogConstants.FILE_NAME);

    if (name != null) {
        return HighlightResult.column(LogConstants.FILE_NAME, getFileColour(name));
    }
    
    return null;
  }

  private ColourPair getFileColour(String name) {
    ColourPair result = colours.get(name);
    
    if (result == null) {
      result = colourRotator.next();
      colours.put(name, result);
    }
    
    return result;
  }
}
