package com.baulsupp.kolja.util;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class MultiColourString implements CharSequence {
  private int length;

  private List<ColouredString> parts = new ArrayList<ColouredString>(3);

  public MultiColourString(String string) {
    this(null, string);
  }

  public MultiColourString(ColourPair pair, String string) {
    parts.add(new ColouredString(pair, string));
    this.length = string.length();
  }

  public MultiColourString(List<ColouredString> text) {
    parts.addAll(text);
    for (ColouredString string : text) {
      length += string.length();
    }
  }

  public MultiColourString() {
  }

  public MultiColourString(MultiColourString string) {
    this.length = string.length;
    this.parts.addAll(string.parts);
  }

  public MultiColourString(ColouredString string) {
    this.length = string.length();
    this.parts.add(string);
  }

  public List<ColouredString> getColouredStrings() {
    return parts;
  }

  public void append(MultiColourString string) {
    parts.addAll(string.parts);
    this.length += string.length();
  }

  public void append(ColouredString string) {
    parts.add(string);
    this.length += string.length();
  }

  public void setColour(ColourPair colors) {
    String text = toString();

    parts.clear();

    parts.add(new ColouredString(colors, text));
  }

  public void setColour(ColourPair colors, int start, int end) {
    if (start < 0 || start >= length || end > length || start > end) {
      throw new IndexOutOfBoundsException();
    }

    int until = start;
    int left = end - start;

    ListIterator<ColouredString> li = parts.listIterator();

    while (li.hasNext()) {
      ColouredString s = li.next();

      if (left == 0) {
        return;
      }

      if (until >= s.length()) {
        until -= s.length();
        continue;
      }

      if (until == 0 && left == s.length()) {
        li.set(new ColouredString(colors, s.toString()));
        left -= s.length();
      } else {
        int upto = Math.min(until + left, s.length());
        String[] parts = new String[] { s.substring(0, until), s.substring(until, upto), s.substring(upto) };
        until = 0;
        left -= parts[1].length();
        li.remove();

        if (parts[0].length() > 0) {
          li.add(new ColouredString(s.getColorPair(), parts[0]));
        }

        li.add(new ColouredString(colors, parts[1]));

        if (parts[2].length() > 0) {
          li.add(new ColouredString(s.getColorPair(), parts[2]));
        }
      }
    }
  }

  public int length() {
    return length;
  }

  public char charAt(int i) {
    if (i < 0 || i >= length) {
      throw new IndexOutOfBoundsException();
    }

    for (ColouredString s : parts) {
      if (i < s.length()) {
        return s.charAt(i);
      } else {
        i -= s.length();
      }
    }

    throw new IndexOutOfBoundsException();
  }

  public CharSequence subSequence(int start, int end) {
    return toString().subSequence(start, end);
  }

  @Override
  public String toString() {
    StringBuilder buffy = new StringBuilder();

    for (ColouredString s : parts) {
      buffy.append(s);
    }

    return buffy.toString();
  }

  @Override
  public int hashCode() {
    return toString().hashCode();
  }

  @Override
  public boolean equals(Object arg0) {
    if (this == arg0) {
      return true;
    }

    if (!(arg0 instanceof MultiColourString)) {
      return false;
    }

    MultiColourString other = (MultiColourString) arg0;

    return length == other.length && parts.equals(other.parts);
  }

  public List<MultiColourString> hardWrap(int width) {
    List<MultiColourString> result = softWrap();

    ListIterator<MultiColourString> li = result.listIterator();
    while (li.hasNext()) {
      MultiColourString s = li.next();

      if (s.length() > width) {
        li.remove();

        for (MultiColourString string : s.splitByWidth(width)) {
          li.add(string);
        }
      }
    }

    return result;
  }

  private List<MultiColourString> splitByWidth(int width) {
    List<MultiColourString> result = new ArrayList<MultiColourString>();

    ListIterator<ColouredString> li = parts.listIterator();

    MultiColourString current = null;
    
    while (li.hasNext()) {
      ColouredString s = li.next();

      while (true) {
        if (current == null) {
          current = new MultiColourString();
          result.add(current);
        }
        
        int extra = (s.length() + current.length) - width;
        if (extra <= 0) {
          current.append(s);
          if (extra == 0) {
            current = null;
          }
          break;
        } else {
          current.append(s.subPart(0, s.length() - extra));
  
          s = s.subPart(s.length() - extra, s.length());
          
          current = null;
        }
      }
    }

    return result;
  }

  public List<MultiColourString> softWrap() {
    List<MultiColourString> result = new ArrayList<MultiColourString>();

    MultiColourString current = new MultiColourString();
    result.add(current);

    ListIterator<ColouredString> li = parts.listIterator();

    while (li.hasNext()) {
      ColouredString s = li.next();

      String text = s.toString();

      if (!text.contains("\n")) {
        current.append(s);
      } else {
        List<ColouredString> parts = s.split("\n");
        
        if (parts.isEmpty()) {
          current = new MultiColourString();
          result.add(current);
        } else {
          for (int i = 0; i < parts.size(); i++) {
            // TODO this looks wrong
            boolean shouldSkipNewline = i == 0;
            
            if (!shouldSkipNewline) {
              current = new MultiColourString();
              result.add(current);
            }
  
            current.append(parts.get(i));
          }
        }
      }
    }

    return result;
  }

  public MultiColourString part(int start, int end) {
    if (start < 0 || start >= length || end > length || start > end) {
      throw new IndexOutOfBoundsException();
    }

    MultiColourString result = new MultiColourString();

    int until = start;
    int left = end - start;

    ListIterator<ColouredString> li = parts.listIterator();

    while (li.hasNext()) {
      ColouredString s = li.next();

      if (left == 0) {
        return result;
      }

      if (until >= s.length()) {
        until -= s.length();
        continue;
      }

      if (until == 0 && left == s.length()) {
        result.append(s);
      } else {
        int upto = Math.min(until + left, s.length());
        String[] parts = new String[] { s.substring(0, until), s.substring(until, upto), s.substring(upto) };
        until = 0;
        left -= parts[1].length();

        result.append(new ColouredString(s.getColorPair(), parts[1]));
      }
    }

    return result;
  }

  public void append(String string) {
    append(new ColouredString(string));
  }
  
  public static void main(String[] args) {
    String s = "a\nb";
    
    System.out.println(s.contains("\n"));
    System.out.println(s.contains("^"));
    System.out.println(s.contains("\\n"));
    System.out.println(s.contains("[\\n\\r]"));
    System.out.println(s.contains("[\n\r]"));
  }
}
