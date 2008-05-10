/**
 * Copyright (c) 2002-2007 Yuri Schimke. All Rights Reserved.
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package com.baulsupp.kolja.ansi.reports.basic;

import java.io.File;

import com.baulsupp.kolja.ansi.reports.AbstractTextReport;
import com.baulsupp.kolja.log.line.Line;

/**
 * @author Yuri Schimke
 * 
 */
public final class FailureReport extends AbstractTextReport<FailureReport> {
  private static final long serialVersionUID = 6089075160216434619L;

  private int count;

  public FailureReport() {
  }

  public String describe() {
    return "Failed Log Lines";
  }

  @Override
  public void beforeFile(File file) {
    super.beforeFile(file);

    count = 0;
  }

  @Override
  public void processLine(Line line) {
    if (line.isFailed()) {
      count++;

      printLine(line);
    }
  }

  @Override
  public void afterFile(File file) {
    super.afterFile(file);

    println("" + count + " failures");
  }
}