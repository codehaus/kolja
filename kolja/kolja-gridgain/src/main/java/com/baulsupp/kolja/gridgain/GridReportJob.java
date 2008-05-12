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
import java.io.Serializable;
import java.util.List;

import org.gridgain.grid.GridException;
import org.gridgain.grid.GridJob;

import com.baulsupp.kolja.ansi.reports.DefaultReportEngineFactory;
import com.baulsupp.kolja.ansi.reports.ReportEngine;
import com.baulsupp.kolja.ansi.reports.ReportEngineFactory;
import com.baulsupp.kolja.ansi.reports.ReportPrinter;
import com.baulsupp.kolja.ansi.reports.TextReport;
import com.baulsupp.kolja.log.util.IntRange;
import com.baulsupp.kolja.log.viewer.importing.LogFormat;

/**
 * @author Yuri Schimke
 * 
 */
public class GridReportJob implements GridJob {
  private static final long serialVersionUID = -1353677666898433825L;

  private LogFormat logFormat;

  private File file;

  private List<TextReport<?>> reports;

  private IntRange intRange;

  private ReportEngineFactory reportEngineFactory = new DefaultReportEngineFactory();

  public GridReportJob(LogFormat logFormat, File file, List<TextReport<?>> reports, IntRange intRange) {
    this.logFormat = logFormat;
    this.file = file;
    this.reports = reports;
    this.intRange = intRange;
  }

  public void setReportEngineFactory(ReportEngineFactory reportEngineFactory) {
    this.reportEngineFactory = reportEngineFactory;
  }

  public void cancel() {
  }

  public Serializable execute() throws GridException {
    try {
      ReportEngine reportEngine = createLocalReportEngine();
      reportEngine.setLogFormat(logFormat);

      ReportPrinter reportPrinter = new NullReportPrinter();
      reportPrinter.initialise();

      reportEngine.setReportPrinter(reportPrinter);

      reportEngine.setReports(reports);

      reportEngine.initialise();

      reportEngine.process(file, intRange);

      reportEngine.completed();
    } catch (Exception e) {
      throw new GridException(e);
    }

    for (TextReport<?> report : reports) {
      report.cleanup();

    }

    return (Serializable) reports;
  }

  private ReportEngine createLocalReportEngine() {
    return reportEngineFactory.createEngine();
  }

  public File getFile() {
    return file;
  }

  public IntRange getIntRange() {
    return intRange;
  }
}
