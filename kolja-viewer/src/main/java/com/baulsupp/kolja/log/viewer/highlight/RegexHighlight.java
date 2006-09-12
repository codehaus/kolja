package com.baulsupp.kolja.log.viewer.highlight;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baulsupp.kolja.log.LogConstants;
import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.util.ColourPair;

// TODO select highlight scope, line, field etc
public class RegexHighlight implements Highlight<Line>, Serializable {
  private static final long serialVersionUID = 8950666564334952873L;

  private String contentField = LogConstants.CONTENT;
  private Pattern pattern;
  private ColourPair colours;
  
  private transient Matcher matcher;

  public RegexHighlight() {
  }
  
  public RegexHighlight(String contentField, Pattern pattern, ColourPair colours) {
    this.contentField = contentField;
    this.pattern = pattern;
    this.colours = colours;
  }

  public String getContentField() {
    return contentField;
  }

  public void setContentField(String contentField) {
    this.contentField = contentField;
  }
  
  public Pattern getPattern() {
    return pattern;
  }

  public void setPattern(Pattern pattern) {
    this.pattern = pattern;
  }
  
  public ColourPair getColours() {
    return colours;
  }

  public void setColours(ColourPair colours) {
    this.colours = colours;
  }

  private void reset(String l) {
    if (matcher == null) {
      matcher = pattern.matcher(l);
    } else {
      matcher.reset(l);
    }
  }

  public HighlightResult getHighlights(Line l) {
    String m = (String) l.getValue(contentField);

    if (m != null) {
      reset(m);
      
      if (matcher.find()) {
        return HighlightResult.row(colours);
      }
    }
    
    return null;
  }
}
