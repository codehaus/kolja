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
package com.baulsupp.kolja.ansi;

import java.io.IOException;

import jline.ANSIBuffer;
import jline.Terminal;

import com.baulsupp.kolja.ansi.progress.NullProgressBar;
import com.baulsupp.kolja.ansi.progress.ProgressBar;
import com.baulsupp.kolja.ansi.progress.StandardProgressBar;
import com.baulsupp.kolja.util.colours.Colour;
import com.baulsupp.kolja.util.colours.ColouredString;
import com.baulsupp.kolja.util.colours.MultiColourString;

public class AnsiUtils {
  public static void append(ANSIBuffer buffy, MultiColourString string) {
    for (ColouredString s : string.getColouredStrings()) {
      append(buffy, s);
    }
  }

  public static void append(ANSIBuffer buffy, ColouredString string) {
    if (string.getForegroundColor() == Colour.BLUE) {
      buffy.blue(string.toString());
    } else if (string.getForegroundColor() == Colour.RED) {
      buffy.red(string.toString());
    } else if (string.getForegroundColor() == Colour.MAGENTA) {
      buffy.magenta(string.toString());
    } else if (string.getForegroundColor() == Colour.GREEN) {
      buffy.green(string.toString());
    } else if (string.getForegroundColor() == Colour.WHITE) {
      buffy.yellow(string.toString());
    } else if (string.getForegroundColor() == Colour.BLACK) {
      buffy.black(string.toString());
    } else if (string.getForegroundColor() == Colour.CYAN) {
      buffy.cyan(string.toString());
    } else {
      buffy.append(string.toString());
    }
  }

  public static int getWidth() {
    int terminalWidth = Terminal.getTerminal().getTerminalWidth();

    if (terminalWidth < 0) {
      terminalWidth = 80;
    }

    return terminalWidth;
  }

  public static ProgressBar getProgressBar(boolean interactive) throws IOException {
    if (interactive && isTerminal()) {
      return new StandardProgressBar();
    } else {
      return new NullProgressBar();
    }
  }

  private static boolean isTerminal() {
    throw new RuntimeException();
  }
}
