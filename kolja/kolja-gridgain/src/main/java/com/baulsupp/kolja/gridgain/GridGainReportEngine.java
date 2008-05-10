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
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import org.gridgain.grid.Grid;
import org.gridgain.grid.GridConfigurationAdapter;
import org.gridgain.grid.GridFactory;
import org.gridgain.grid.logger.GridLogger;
import org.gridgain.grid.logger.java.GridJavaLogger;
import org.gridgain.grid.spi.checkpoint.sharedfs.GridSharedFsCheckpointSpi;

import com.baulsupp.kolja.ansi.reports.ReportEngine;
import com.baulsupp.kolja.ansi.reports.ReportPrinter;
import com.baulsupp.kolja.ansi.reports.TextReport;
import com.baulsupp.kolja.log.viewer.importing.LogFormat;

/**
 * @author Yuri Schimke
 */
public class GridGainReportEngine implements ReportEngine {
  protected List<TextReport<?>> reports;

  protected ReportPrinter reportPrinter;

  private LogFormat format;

  public GridGainReportEngine() {
  }

  public void setReportPrinter(ReportPrinter reportPrinter) {
    this.reportPrinter = reportPrinter;
  }

  public void setReports(java.util.List<TextReport<?>> reports) {
    this.reports = reports;
  }

  public void initialise() throws IOException {
    reportPrinter.initialise();
  }

  public void completed() throws IOException {
    boolean first = true;

    for (TextReport<?> r : reports) {
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
    GridConfigurationAdapter conf = getConfiguration();

    GridFactory.start(conf);

    try {
      Grid grid = GridFactory.getGrid();

      ReportBatch batch = new ReportBatch(format, commandFiles, reports);

      reports = grid.execute(new GridReportSplitter(), batch).get();
    } finally {
      GridFactory.stop(true);
    }

    completed();
  }

  private GridConfigurationAdapter getConfiguration() {
    GridConfigurationAdapter conf = new GridConfigurationAdapter();
    conf.setGridGainHome("C:\\java\\gridgain-2.0.2");
    Logger logger = Logger.getLogger("grid");
    GridLogger gridLogger = new GridJavaLogger(logger);
    conf.setGridLogger(gridLogger);
    GridSharedFsCheckpointSpi cpSpi = new GridSharedFsCheckpointSpi();
    cpSpi.setDirectoryPath("C:\\java\\gridgain-work");
    conf.setCheckpointSpi(cpSpi);
    return conf;
  }

  public void setLogFormat(LogFormat format) {
    this.format = format;
  }
}
