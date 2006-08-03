package com.baulsupp.kolja.log.viewer.highlight;

import java.util.HashMap;
import java.util.Map;

import com.baulsupp.kolja.util.ColourPair;

public class HighlightResult {
  private ColourPair row;

  private Map<String, ColourPair> columns = new HashMap<String, ColourPair>();

  private Map<String, ColourPair> words = new HashMap<String, ColourPair>();

  public HighlightResult() {
  }

  public static HighlightResult word(String match, ColourPair colour) {
    HighlightResult result = new HighlightResult();
    result.words.put(match, colour);
    return result;
  }

  public static HighlightResult column(String name, ColourPair colour) {
    HighlightResult result = new HighlightResult();
    result.columns.put(name, colour);
    return result;
  }

  public static HighlightResult row(ColourPair colour) {
    HighlightResult result = new HighlightResult();
    result.row = colour;
    return result;
  }

  public void combine(HighlightResult result) {
    if (result != null) {
      if (result.row != null) {
        this.row = result.row;
      }

      this.columns.putAll(result.columns);
      this.words.putAll(result.words);
    }
  }

  public ColourPair getRow() {
    return row;
  }

  public void setRow(ColourPair row) {
    this.row = row;
  }

  public ColourPair getColumnHighlight(String name) {
    return columns.get(name);
  }

  public Map<String, ColourPair> getWords() {
    return words;
  }

  public void highlightWord(String match, ColourPair colour) {
    words.put(match, colour);
  }
}
