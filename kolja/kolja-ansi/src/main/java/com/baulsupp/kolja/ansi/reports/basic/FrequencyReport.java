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
package com.baulsupp.kolja.ansi.reports.basic;

import com.baulsupp.kolja.ansi.reports.ReportEngine;
import com.baulsupp.kolja.ansi.reports.ReportPrinter;
import com.baulsupp.kolja.log.line.Line;

/**
 * Most Frequently Accessed Pages.
 * 
 * @author Yuri Schimke
 */
public class FrequencyReport<T> extends AbstractFrequencyReport<T> {
  private String field;

  public FrequencyReport() {
  }

  public FrequencyReport(String url) {
    this.field = url;
  }

  public void setQ(String field) {
    this.field = field;
  }

  @SuppressWarnings("unchecked")
  @Override
  protected T getValue(Line line) {
    return (T) line.getValue(field);
  }

  @Override
  public void initialise(ReportPrinter reportRunner, ReportEngine reportEngine) {
    super.initialise(reportRunner, reportEngine);

    if (field == null) {
      throw new IllegalStateException("q not set");
    }
  }

  public String describe() {
    return "Frequency: " + field;
  }
}
