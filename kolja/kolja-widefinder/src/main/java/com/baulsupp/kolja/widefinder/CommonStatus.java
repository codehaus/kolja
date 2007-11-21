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
package com.baulsupp.kolja.widefinder;

import java.util.TreeMap;

import com.baulsupp.kolja.ansi.reports.AbstractFrequencyReport;
import com.baulsupp.kolja.ansi.reports.Frequencies.Count;
import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.widefinder.format.HttpStatus;

/**
 * Most Frequently Accessed Pages.
 * 
 * @author Yuri Schimke
 */
public class CommonStatus extends AbstractFrequencyReport<HttpStatus> {
  public CommonStatus() {
    super(new TreeMap<HttpStatus, Count<HttpStatus>>());
  }

  @Override
  public void processLine(Line line) {
    HttpStatus status = (HttpStatus) line.getValue(WideFinderConstants.STATUS);

    increment(status);
  }

  @Override
  public void completed() {
    for (Count<HttpStatus> c : getFrequencies()) {
      println(c.getItem() + " " + c.getCount());
    }
  }
}
