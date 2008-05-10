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
package com.baulsupp.kolja.gridgain;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.baulsupp.kolja.ansi.reports.TextReport;
import com.baulsupp.kolja.log.viewer.importing.LogFormat;

/**
 * @author Yuri Schimke
 * 
 */
public class ReportBatch {

  private List<File> files;
  private LogFormat format;
  private List<TextReport<?>> reports;

  public ReportBatch(LogFormat format, List<File> files, List<TextReport<?>> report) {
    this.format = format;
    this.files = files;
    this.reports = report;
  }

  public List<File> getFiles() {
    return files;
  }

  public LogFormat getFormat() {
    return format;
  }

  public List<TextReport<?>> getReportsCopy() {
    ArrayList<TextReport<?>> copy = new ArrayList<TextReport<?>>(reports.size());

    for (TextReport<?> textReport : reports) {
      copy.add(textReport.newInstance());
    }

    return copy;
  }

  public List<TextReport<?>> getReports() {
    return reports;
  }
}
