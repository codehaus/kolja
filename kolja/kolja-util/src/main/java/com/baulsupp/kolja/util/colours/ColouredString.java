package com.baulsupp.kolja.util.colours;

import java.util.ArrayList;
import java.util.List;

/**
 * A single colour string.  supports basic string operations
 * as well as being a CharSequence.
 */
public class ColouredString implements CharSequence {
  private ColourPair colour = null;

  private String content = null;

  /**
   * Creates a new coloured string with given colour.
   * 
   * @param color the colour, or null for default.
   * @param content the string content.
   */
  public ColouredString(ColourPair color, String content) {
    this.colour = color;
    this.content = content;
  }

  /**
   * Creates a new coloured string with the default colour.
   * 
   * @param string the string content.
   */
  public ColouredString(String string) {
    this.content = string;
  }

  /**
   * Returns the colours of the string.
   */
  public ColourPair getColorPair() {
    return colour;
  }

  /**
   * Returns the string length.
   */
  public int length() {
    return content.length();
  }

  /**
   * Returns the foreground colour, or null for default.
   */
  public Colour getForegroundColor() {
    return colour == null ? null : colour.getForeground();
  }

  /**
   * Returns the background colour, or null for default.
   */
  public Colour getBackgroundColor() {
    return colour == null ? null : colour.getBackground();
  }

  /**
   * Returns the character at position <code>pos</code>
   */
  public char charAt(int pos) {
    return content.charAt(pos);
  }

  /**
   * Returns a part of the string content as a CharSequence.
   */
  public CharSequence subSequence(int from, int to) {
    return content.subSequence(from, to);
  }

  /**
   * Returns a part of the string content as a normal string.
   */
  public String substring(int arg0, int arg1) {
    return content.substring(arg0, arg1);
  }
  
  @Override
  public int hashCode() {
    return content.hashCode();
  }
  
  @Override
  public boolean equals(Object arg0) {
    if (this == arg0) {
      return true;
    }
    
    if (!(arg0 instanceof ColouredString)) {
      return false;
    }
    
    ColouredString other = (ColouredString) arg0;
    
    if (!content.equals(other.content)) {
      return false;
    }

    if (colour == null) {
      return other.colour == null;
    } else {
      return other.colour != null && colour.equals(other.colour);
    }
  }
  
  @Override
  public String toString() {
    return content;
  }

  public String substring(int i) {
    return content.substring(i);
  }

  /**
   * Returns a new string with the same content but different colour.
   */
  public ColouredString changeColour(ColourPair colour) {
    return new ColouredString(colour, this.content);
  }

  /**
   * Splits a string using a regexp pattern.
   */
  public List<ColouredString> split(String string) {
    List<ColouredString> result = new ArrayList<ColouredString>();
    
    String[] parts = content.split(string);
    for (String s : parts) {
      result.add(new ColouredString(colour, s));
    }
    
    return result;
  }

  /**
   * Returns a part of the string content as a coloured string.
   */
  public ColouredString subPart(int from, int to) {
    String s = content.substring(from, to);
    
    return new ColouredString(colour, s);
  }

  /**
   * @param width
   * @return
   */
  public List<ColouredString> splitByWidth(int width) {
    List<ColouredString> result = new ArrayList<ColouredString>();
    
    int from = 0;
    int to = width;
    while (to < content.length()) {
      result.add(subPart(from, to));
      
      from = to;
      to = Math.min(content.length(), to + width);
    }
    
    return result;
  }
}
