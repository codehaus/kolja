package com.baulsupp.kolja.log.viewer.highlight;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.util.ColourPair;

public class BasicSearchHighlight implements Highlight<Line> {
  private Pattern pattern;
  
  private transient Matcher matcher;

  private ColourPair colour;

  public BasicSearchHighlight(ColourPair colour) {
    this.colour = colour;
  }

  public HighlightResult getHighlights(Line viewRow) {
    HighlightResult result = null;
    if (pattern != null) {
      if (matcher == null) {
        matcher = pattern.matcher(viewRow);
      } else {
        matcher.reset(viewRow);
      }

      while (matcher.find()) {
        String match = matcher.group();
        
        if (result == null) {
          result = new HighlightResult();
        }
        result.highlightWord(match, colour);
      }
    }
    return result;
  }

  public void setPattern(Pattern pattern) {
    this.pattern = pattern;
    this.matcher = null;
  }

  public void clear() {
    this.matcher = null;
    this.pattern = null;
  }
}
