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
package com.baulsupp.kolja.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class TextUtil {
  public static final String LINE_SEPERATOR;
  static {
    // LINE_SEPERATOR = System.getProperty("line.separator", "\n");
    LINE_SEPERATOR = "\n";
  }

  public static String readAll(File f) throws IOException {
    StringBuffer buffy = new StringBuffer();

    Reader r = new FileReader(f);
    int read = 0;
    char[] chars = new char[4096];
    while ((read = r.read(chars)) != -1) {
      buffy.append(chars, 0, read);
    }
    r.close();

    return buffy.toString();
  }

  private static int hardWrapHeight(String text, int width) {
    return ((text.length() - 1) / width) + 1 + newLineCount(text);
  }

  private static int newLineCount(String text) {
    int count = 0;

    int pos = 0;
    while ((pos = text.indexOf('\n', pos)) != -1) {
      pos++;
    }

    return count;
  }

  public static String[] hardWrap(String text, int width) {
    String[] result = new String[hardWrapHeight(text, width)];

    String[] starting = text.split("\n");

    int pos = 0;

    int from = 0;
    for (int i = 0; i < result.length; i++) {
      int to = Math.min(starting[pos].length(), from + width);
      result[i] = starting[pos].substring(from, to);

      if (to == starting[pos].length()) {
        pos++;
        from = 0;
      } else {
        from += result[i].length();
      }
    }
    return result;
  }

  public static String[] softWrap(String text) {
    return text.split("\n");
  }

  public static String[] softWrapAndCrop(String value, int screenWidth) {
    String[] text = softWrap(value);

    for (int i = 0; i < text.length; i++) {
      text[i] = text[i].substring(0, Math.min(screenWidth, text[i].length()));
    }

    return text;
  }

  public static String fixedWidth(String value, int length) {
    if (value.length() <= length) {
      StringBuilder buffy = new StringBuilder();
      buffy.append(value);

      int diff = length - value.length();
      while (diff-- > 0) {
        buffy.append(' ');
      }
      return buffy.toString();
    } else {
      return value.substring(0, length);
    }
  }

  public static String blank(int extra) {
    StringBuilder buffy = new StringBuilder();

    for (int i = 0; i < extra; i++) {
      buffy.append(' ');
    }

    return buffy.toString();
  }
}
