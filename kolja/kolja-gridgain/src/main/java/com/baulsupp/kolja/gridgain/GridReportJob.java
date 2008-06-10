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
import java.util.ArrayList;
import java.util.List;

import org.gridgain.grid.GridException;
import org.gridgain.grid.GridJob;

import com.baulsupp.kolja.ansi.reports.MementoReport;
import com.baulsupp.kolja.ansi.reports.ReportPrinter;
import com.baulsupp.kolja.ansi.reports.ReportUtils;
import com.baulsupp.kolja.ansi.reports.TextReport;
import com.baulsupp.kolja.ansi.reports.engine.DefaultReportEngineFactory;
import com.baulsupp.kolja.ansi.reports.engine.ReportEngine;
import com.baulsupp.kolja.ansi.reports.engine.ReportEngineFactory;
import com.baulsupp.kolja.ansi.reports.engine.file.NullReportPrinter;
import com.baulsupp.kolja.log.util.LongRange;
import com.baulsupp.kolja.log.viewer.importing.LogFormat;
import com.baulsupp.kolja.util.services.BeanFactory;

/**
 * @author Yuri Schimke
 * 
 */
public class GridReportJob implements GridJob {
  private static final long serialVersionUID = -1353677666898433825L;

  private LogFormat logFormat;

  private File file;

  private LongRange intRange;

  private ReportEngineFactory reportEngineFactory = new DefaultReportEngineFactory();

  private List<String> reportDescriptions;

  private BeanFactory<TextReport<?>> reportBuilder;

  public GridReportJob(LogFormat logFormat, File file, List<String> reportDescriptions, LongRange longRange) {
    this.logFormat = logFormat;
    this.file = file;
    this.reportDescriptions = reportDescriptions;
    this.intRange = longRange;
  }

  public void setReportEngineFactory(ReportEngineFactory reportEngineFactory) {
    this.reportEngineFactory = reportEngineFactory;
  }

  public void setReportBuilder(BeanFactory<TextReport<?>> reportBuilder) {
    this.reportBuilder = reportBuilder;
  }

  public void cancel() {
  }

  public Serializable execute() throws GridException {
    List<TextReport<?>> reports;
    try {
      reports = createReports();

      ReportEngine reportEngine = createLocalReportEngine();
      reportEngine.setLogFormat(logFormat);

      ReportPrinter reportPrinter = new NullReportPrinter();
      reportPrinter.initialise(logFormat);

      reportEngine.setReportPrinter(reportPrinter);

      reportEngine.setReports(reports);

      reportEngine.initialise();

      reportEngine.process(file, intRange);

      reportEngine.completed();

      for (TextReport<?> report : reports) {
        report.cleanup();
      }

      return (Serializable) getResult(reports);
    } catch (Exception e) {
      throw new GridException(e);
    }
  }

  private List<Object> getResult(List<TextReport<?>> reports) throws Exception {
    List<Object> result = new ArrayList<Object>();

    for (TextReport<?> report : reports) {
      if (report instanceof MementoReport) {
        result.add(((MementoReport<?>) report).getMemento());
      } else {
        result.add(report);
      }
    }

    return result;
  }

  private List<TextReport<?>> createReports() throws Exception {
    if (reportBuilder == null) {
      reportBuilder = ReportUtils.createReportBuilder();
    }

    return ReportUtils.createReports(reportBuilder, reportDescriptions);
  }

  private ReportEngine createLocalReportEngine() {
    return reportEngineFactory.createEngine();
  }

  public File getFile() {
    return file;
  }

  public LongRange getLongRange() {
    return intRange;
  }
}
