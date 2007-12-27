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
package com.baulsupp.kolja.ansi.progress;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;

import jline.ConsoleReader;

import org.springframework.util.Assert;

import com.baulsupp.kolja.log.viewer.format.BytesFormat;

public class StandardProgressBar implements ProgressBar {
  private ConsoleReader cr;
  private int last = Integer.MAX_VALUE;
  private static final int JUMP = 512 * 1024;

  private BytesFormat bytesFormat = new BytesFormat();
  private boolean drawn;
  private int barWidth = 20;

  // private int width;

  public StandardProgressBar() throws IOException {
    cr = new ConsoleReader(new ByteArrayInputStream(new byte[0]), new PrintWriter(System.out));
    // width = AnsiUtils.getWidth();
  }

  public void clear() {
    if (drawn) {
      drawLine(repetition(' ', 20) + repetition(' ', 20));

      drawn = false;
    }
  }

  // http://luka.tnode.com/posts/view/157
  public void showProgress(int completed, int total) {
    Assert.isTrue(completed >= 0);
    Assert.isTrue(total >= 0);
    Assert.isTrue(completed <= total);

    if (completed < (last + JUMP)) {
      return;
    }

    last = completed;

    String result = getText(completed, total);
    drawLine(result);

    drawn = true;
  }

  private void drawLine(String result) {
    try {
      cr.setCursorPosition(0);
      cr.killLine();
      cr.putString(result);
      cr.redrawLine();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private String getText(int completed, int total) {
    String of = format(completed) + " of " + format(total);
    String padding = repetition(' ', Math.max(1, 20 - of.length()));
    return of + padding + "[" + getBar(completed, total) + "]";
  }

  private String format(int total) {
    return bytesFormat.format(total);
  }

  private String getBar(int completed, int total) {
    double percent = ((double) completed) / total;
    int progress = (int) (barWidth * percent);

    Assert.isTrue(progress >= 0, "not negative");
    Assert.isTrue(progress <= barWidth, "not overhanging");

    String bars = repetition('=', progress);
    String spaces = repetition(' ', barWidth - progress);
    return bars + spaces;
  }

  private String repetition(char string, int count) {
    if (string == ' ') {
      return "                    ".substring(0, count);
    } else if (string == '=') {
      return "====================".substring(0, count);
    } else {
      throw new IllegalArgumentException();
    }
  }
}
