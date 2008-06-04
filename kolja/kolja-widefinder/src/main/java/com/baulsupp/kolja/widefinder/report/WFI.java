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
package com.baulsupp.kolja.widefinder.report;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baulsupp.kolja.ansi.reports.BaseTextReport;
import com.baulsupp.kolja.ansi.reports.basic.Frequencies;
import com.baulsupp.kolja.ansi.reports.basic.Frequencies.Count;
import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.widefinder.WideFinderConstants;

/**
 * @author Yuri Schimke
 * 
 */
public class WFI extends BaseTextReport<WFI> {
  private static final long serialVersionUID = 5637074836971432761L;

  private Frequencies<String> counts = new Frequencies<String>();
  private transient Matcher matcher;

  public String describe() {
    return "WideFinder I";
  }

  @Override
  public String getName() {
    return "wfi";
  }

  @Override
  public void merge(WFI partReport) {
    counts.merge(counts);
  }

  @Override
  public void processLine(Line line) {
    String url = (String) line.getValue(WideFinderConstants.URL);
    String part = match(url);

    if (part != null) {
      counts.increment(part);
    }
  }

  @Override
  public void completed() {
    for (Count<String> count : counts.getMostFrequent(10)) {
      println(count.toString());
    }
  }

  private String match(String url) {
    if (matcher == null) {
      matcher = Pattern.compile("/ongoing/When/\\d\\d\\dx/(\\d\\d\\d\\d/\\d\\d/\\d\\d/[^ .]+)").matcher(url);
    } else {
      matcher.reset(url);
    }

    if (matcher.matches()) {
      return matcher.group(1);
    } else {
      return null;
    }
  }
}
