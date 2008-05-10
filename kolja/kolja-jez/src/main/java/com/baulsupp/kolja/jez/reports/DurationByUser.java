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
package com.baulsupp.kolja.jez.reports;

import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import com.baulsupp.kolja.ansi.reports.AbstractTextReport;
import com.baulsupp.kolja.jez.JezConstants;
import com.baulsupp.kolja.log.line.Line;

/**
 * @author Yuri Schimke
 * 
 */
public final class DurationByUser extends AbstractTextReport<DurationByUser> {
  private static final long serialVersionUID = 5136930933855032187L;

  private Map<String, Long> times = new TreeMap<String, Long>();

  public String describe() {
    return "Duration By User";
  }

  @Override
  public void processLine(Line line) {
    String user = (String) line.getValue(JezConstants.USER);
    Long time = (Long) line.getValue(JezConstants.DURATION);

    if (user != null && time != null) {
      incrementTime(user, time);
    }

  }

  private void incrementTime(String user, Long time) {
    Long existing = times.get(user);

    if (existing != null) {
      times.put(user, existing.longValue() + time.longValue());
    } else {
      times.put(user, time.longValue());
    }
  }

  @Override
  public void completed() {
    for (Entry<String, Long> e : times.entrySet()) {
      println(e.getKey() + "\t" + e.getValue());
    }
  }
}
