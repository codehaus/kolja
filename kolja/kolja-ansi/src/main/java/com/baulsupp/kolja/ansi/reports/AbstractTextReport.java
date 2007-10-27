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
package com.baulsupp.kolja.ansi.reports;

import org.springframework.util.Assert;

import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.util.colours.MultiColourString;

public abstract class AbstractTextReport implements TextReport {
  private ReportRunner reportRunner;

  public void initialise(ReportRunner reportRunner) {
    Assert.notNull(reportRunner);

    this.reportRunner = reportRunner;
  }

  public void processLine(Line line) {
  }

  public void completed() {
  }

  public void display(boolean showHeader) {
  }

  public void println(MultiColourString string) {
    reportRunner.println(string);
  }

  public void println(Line line) {
    reportRunner.println(line);
  }

  public void println(String string) {
    reportRunner.println(new MultiColourString(string));
  }
}