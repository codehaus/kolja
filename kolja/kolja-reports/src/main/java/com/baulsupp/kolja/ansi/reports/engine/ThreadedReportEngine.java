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
package com.baulsupp.kolja.ansi.reports.engine;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.baulsupp.kolja.ansi.reports.ReportPrinter;
import com.baulsupp.kolja.ansi.reports.ReportUtils;
import com.baulsupp.kolja.ansi.reports.TextReport;
import com.baulsupp.kolja.ansi.reports.engine.file.FileDivider;
import com.baulsupp.kolja.ansi.reports.engine.file.FileSection;
import com.baulsupp.kolja.ansi.reports.engine.file.NullReportContext;
import com.baulsupp.kolja.ansi.reports.engine.file.NullReportPrinter;
import com.baulsupp.kolja.log.util.LongRange;
import com.baulsupp.kolja.log.viewer.importing.LogFormat;
import com.baulsupp.kolja.util.services.BeanFactory;

/**
 * @author Yuri Schimke
 */
public class ThreadedReportEngine implements ReportEngine {
  private LogFormat format;
  private ReportPrinter reportPrinter;
  private List<TextReport<?>> reports;
  private ExecutorService executor;
  private FileDivider fileDivider;
  private ReportEngineFactory reportEngineFactory;
  private BeanFactory<TextReport<?>> reportBuilder;
  private Integer count;

  public void setLogFormat(LogFormat format) {
    this.format = format;
  }

  public void setCount(Integer count) {
    this.count = count;
  }

  public void setReportEngineFactory(ReportEngineFactory reportEngineFactory) {
    this.reportEngineFactory = reportEngineFactory;
  }

  public void setReportPrinter(ReportPrinter reportPrinter) {
    this.reportPrinter = reportPrinter;
  }

  public void setReportBuilder(BeanFactory<TextReport<?>> reportBuilder) {
    this.reportBuilder = reportBuilder;
  }

  public void setReportDescriptions(List<String> v) throws Exception {
    if (reportBuilder == null) {
      reportBuilder = ReportUtils.createReportBuilder();
    }

    setReports(ReportUtils.createReports(reportBuilder, v));
  }

  public void setReports(List<TextReport<?>> reports) {
    this.reports = reports;
  }

  public void setExecutor(ExecutorService executor) {
    this.executor = executor;
  }

  public void setFileDivider(FileDivider fileDivider) {
    this.fileDivider = fileDivider;
  }

  public void initialise() throws Exception {
    if (executor == null) {
      int threads = getProcessorCount();
      executor = Executors.newFixedThreadPool(threads);
    }
  }

  private int getProcessorCount() {
    if (count != null) {
      return count;
    } else {
      return Runtime.getRuntime().availableProcessors();
    }
  }

  public void completed() throws Exception {
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
  }

  public void process(List<File> files) throws Exception {
    List<FileSection> sections = split(files);

    processParts(sections);
  }

  public void process(File file, LongRange intRange) throws Exception {
    List<FileSection> sections = split(Collections.singletonList(file));

    processParts(sections);
  }

  private List<FileSection> split(List<File> files) {
    return this.fileDivider.split(files, getProcessorCount());
  }

  private void processParts(List<FileSection> sections) throws Exception {
    List<Future<Void>> futures = new ArrayList<Future<Void>>();

    try {
      for (FileSection fileSection : sections) {
        Callable<Void> task = createReportRun(fileSection);
        futures.add(executor.submit(task));
      }

      for (Future<?> future : futures) {
        getNextResult(future);
      }
    } finally {
      executor.shutdownNow();
    }
  }

  private void getNextResult(Future<?> future) throws Exception {
    try {
      future.get();
    } catch (InterruptedException e) {
      throw e;
    } catch (ExecutionException e) {
      Throwable cause = e.getCause();

      if (cause instanceof Exception) {
        throw (Exception) cause;
      } else if (cause instanceof Error) {
        throw (Error) cause;
      } else {
        throw new Exception(cause);
      }
    }
  }

  private Callable<Void> createReportRun(final FileSection fileSection) {
    return new Callable<Void>() {
      public Void call() throws Exception {
        processFileSection(fileSection);
        return null;
      }
    };
  }

  protected void processFileSection(FileSection fileSection) throws Exception {
    List<TextReport<?>> reportsCopy = ReportUtils.getReportsCopy(reports);

    ReportEngine reportEngine = reportEngineFactory.createEngine();
    reportEngine.setLogFormat(format);

    ReportPrinter reportPrinter = new NullReportPrinter();
    reportPrinter.initialise(format);

    reportEngine.setReportPrinter(reportPrinter);

    reportEngine.setReports(reportsCopy);

    reportEngine.initialise();

    reportEngine.process(fileSection.getFile(), fileSection.getLongRange());

    ReportUtils.mergeReports(reports, reportsCopy);
  }
}
