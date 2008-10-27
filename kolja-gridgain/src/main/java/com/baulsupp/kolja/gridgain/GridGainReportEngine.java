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
import java.util.List;

import org.gridgain.grid.Grid;
import org.gridgain.grid.GridConfigurationAdapter;
import org.gridgain.grid.GridFactory;

import com.baulsupp.kolja.ansi.reports.ReportPrinter;
import com.baulsupp.kolja.ansi.reports.TextReport;
import com.baulsupp.kolja.ansi.reports.engine.ReportEngine;
import com.baulsupp.kolja.ansi.reports.engine.file.NullReportContext;
import com.baulsupp.kolja.log.util.LongRange;
import com.baulsupp.kolja.log.viewer.importing.LogFormat;

/**
 * @author Yuri Schimke
 */
public class GridGainReportEngine implements ReportEngine {
  protected ReportPrinter reportPrinter;

  private LogFormat format;

  private Grid grid;

  private List<String> reportDescriptions;

  private List<TextReport<?>> reports;

  public GridGainReportEngine() {
  }

  public void setReportPrinter(ReportPrinter reportPrinter) {
    this.reportPrinter = reportPrinter;
  }

  public void setReportDescriptions(List<String> v) throws Exception {
    this.reportDescriptions = v;
    // setReports(ReportUtils.createReports(ReportUtils.createReportBuilder(),
    // v));
  }

  public void setReports(List<TextReport<?>> reports) {
    // this.reports = reports;
  }

  public void initialise() throws Exception {
    GridConfigurationAdapter conf = GridConfigFactory.getConfiguration();

    GridFactory.start(conf);

    grid = GridFactory.getGrid();
  }

  public void completed() throws Exception {
    GridFactory.stop(true);

    boolean first = true;

    for (TextReport<?> r : reports) {
      r.initialise(reportPrinter, new NullReportContext());

      if (!first) {
        reportPrinter.printLine();
      } else {
        first = false;
      }

      r.completed();
    }

    reportPrinter.completed();
  }

  public void process(List<File> commandFiles) throws Exception {
    ReportBatch batch = new ReportBatch(format, commandFiles, reportDescriptions);

    reports = grid.execute(new GridReportSplitter(), batch).get();
  }

  public void process(File file, LongRange intRange) {
    throw new UnsupportedOperationException();
  }

  public void setLogFormat(LogFormat format) {
    this.format = format;
  }
}
