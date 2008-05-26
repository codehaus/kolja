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

import com.baulsupp.kolja.ansi.reports.AbstractTextReport;
import com.baulsupp.kolja.ansi.reports.basic.Frequencies;
import com.baulsupp.kolja.ansi.reports.basic.Frequencies.Count;
import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.viewer.format.OutputFormat;
import com.baulsupp.kolja.widefinder.WideFinderConstants;
import com.baulsupp.kolja.widefinder.format.SimpleFormat;
import com.baulsupp.kolja.widefinder.format.TruncatedFormat;
import com.baulsupp.kolja.widefinder.format.WideFinderLine;

/**
 * @author Yuri Schimke
 * 
 */
public abstract class WFIIBase<T extends WFIIBase<T>> extends AbstractTextReport<T> {
  private static final long serialVersionUID = 1L;

  private transient Matcher matcher;

  protected Frequencies<String> counts = new Frequencies<String>();

  public static final SimpleFormat NUMBER = new SimpleFormat();
  public static final TruncatedFormat ITEM = new TruncatedFormat(60);

  @Override
  public void completed() {
    printSection(NUMBER, ITEM);
  }

  protected void printSection(OutputFormat format, OutputFormat itemFormat) {
    for (Count<String> count : counts.getMostFrequent(10)) {
      println(count, format, itemFormat);
    }
  }

  private void println(Count<String> count, OutputFormat format, OutputFormat itemFormat) {
    println(String.format(" %10s: %s", format.format(count.getCount()), itemFormat.format(count.getItem())));
  }

  protected boolean matchArticle(String url) {
    if (matcher == null) {
      matcher = Pattern.compile("/ongoing/When/\\d\\d\\dx/\\d\\d\\d\\d/\\d\\d/\\d\\d/[^ .]+").matcher(url);
    } else {
      matcher.reset(url);
    }

    return matcher.matches();
  }

  protected boolean isGet(Line line) {
    return "GET".equals(line.getValue(WideFinderConstants.ACTION));
  }

  @Override
  public void processLine(Line line) {
    if (isGet(line)) {
      processLine((WideFinderLine) line);
    }
  }

  protected abstract void processLine(WideFinderLine line);

}
