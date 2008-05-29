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
import com.baulsupp.kolja.log.viewer.format.OutputFormat;
import com.baulsupp.kolja.widefinder.WideFinderConstants;
import com.baulsupp.kolja.widefinder.format.HttpStatus;
import com.baulsupp.kolja.widefinder.format.MegaBytesFormat;
import com.baulsupp.kolja.widefinder.format.SimpleFormat;
import com.baulsupp.kolja.widefinder.format.TruncatedFormat;

/**
 * @author Yuri Schimke
 * 
 */
public class WFII extends BaseTextReport<WFII> {
  private static final long serialVersionUID = 5637074836971432761L;

  public static final MegaBytesFormat MB = new MegaBytesFormat();
  public static final SimpleFormat NUMBER = new SimpleFormat();
  public static final TruncatedFormat ITEM = new TruncatedFormat(60);

  private Frequencies<String> articles = new Frequencies<String>();
  private Frequencies<String> byBytes = new Frequencies<String>();
  private Frequencies<String> _404 = new Frequencies<String>();
  private Frequencies<String> clients = new Frequencies<String>();
  private Frequencies<String> referrers = new Frequencies<String>();

  private transient Matcher matcher;

  public String describe() {
    return "WideFinder II";
  }

  @Override
  public String getName() {
    return "wfii";
  }

  @Override
  public void processLine(Line line) {
    if ("GET".equals(line.getValue(WideFinderConstants.ACTION))) {
      String url = (String) line.getValue(WideFinderConstants.URL);

      boolean isArticle = matchArticle(url);
      Long bytes = (Long) line.getValue(WideFinderConstants.SIZE);
      HttpStatus status = (HttpStatus) line.getValue(WideFinderConstants.STATUS);
      String client = (String) line.getValue(WideFinderConstants.IPADDRESS);
      String referrer = (String) line.getValue(WideFinderConstants.REFERRER);

      if (isArticle && status.isHit()) {
        articles.increment(url);
        clients.increment(client);

        if (isExternalReferrer(referrer)) {
          referrers.increment(referrer);
        }
      }

      if (bytes != null && status.equals(HttpStatus.SUCCESS_OK)) {
        byBytes.incrementBy(url, bytes);
      }

      if (status.equals(HttpStatus.CLIENT_ERROR_NOT_FOUND)) {
        _404.increment(url);
      }
    }
  }

  private boolean isExternalReferrer(String referrer) {
    return referrer != null && !referrer.equals("-") && !referrer.startsWith("http://www.tbray.org/ongoing/");
  }

  @Override
  public void completed() {
    printSection("Top URIs by hit", articles, NUMBER, ITEM);
    printSection("Top URIs by bytes", byBytes, MB, ITEM);
    printSection("Top 404s", _404, NUMBER, ITEM);
    printSection("Top client addresses", clients, NUMBER, ITEM);
    printSection("Top referrers", referrers, NUMBER, ITEM);
  }

  private void printSection(String title, Frequencies<String> frequencies, OutputFormat format, OutputFormat itemFormat) {
    println(title);
    for (Count<String> count : frequencies.getMostFrequent(10)) {
      println(count, format, itemFormat);
    }
    println("");
  }

  private void println(Count<String> count, OutputFormat format, OutputFormat itemFormat) {
    println(String.format(" %10s: %s", format.format(count.getCount()), itemFormat.format(count.getItem())));
  }

  private boolean matchArticle(String url) {
    if (matcher == null) {
      matcher = Pattern.compile("/ongoing/When/\\d\\d\\dx/\\d\\d\\d\\d/\\d\\d/\\d\\d/[^ .]+").matcher(url);
    } else {
      matcher.reset(url);
    }

    return matcher.matches();
  }
}
