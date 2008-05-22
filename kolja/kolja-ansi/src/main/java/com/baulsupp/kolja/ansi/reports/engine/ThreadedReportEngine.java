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
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.baulsupp.kolja.ansi.reports.ReportPrinter;
import com.baulsupp.kolja.ansi.reports.ReportUtils;
import com.baulsupp.kolja.ansi.reports.TextReport;
import com.baulsupp.kolja.ansi.reports.engine.file.FileDivider;
import com.baulsupp.kolja.ansi.reports.engine.file.FileSection;
import com.baulsupp.kolja.ansi.reports.engine.file.NullReportContext;
import com.baulsupp.kolja.ansi.reports.engine.file.NullReportPrinter;
import com.baulsupp.kolja.log.util.IntRange;
import com.baulsupp.kolja.log.viewer.importing.LogFormat;

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

  public void setLogFormat(LogFormat format) {
    this.format = format;
  }

  public void setReportEngineFactory(ReportEngineFactory reportEngineFactory) {
    this.reportEngineFactory = reportEngineFactory;
  }

  public void setReportPrinter(ReportPrinter reportPrinter) {
    this.reportPrinter = reportPrinter;
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
    return Runtime.getRuntime().availableProcessors();
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

    executor.shutdown();
    executor.awaitTermination(10, TimeUnit.SECONDS);
  }

  public void process(List<File> files) throws Exception {
    List<FileSection> sections = split(files);

    processParts(sections);
  }

  public void process(File file, IntRange intRange) throws Exception {
    List<FileSection> sections = split(Collections.singletonList(file));

    processParts(sections);
  }

  private List<FileSection> split(List<File> files) {
    return this.fileDivider.split(files, getProcessorCount());
  }

  private void processParts(List<FileSection> sections) throws Exception {
    ExecutorCompletionService<List<TextReport<?>>> service = new ExecutorCompletionService<List<TextReport<?>>>(executor);

    for (FileSection fileSection : sections) {
      Callable<List<TextReport<?>>> task = createReportRun(fileSection);
      service.submit(task);
    }

    for (int i = 0; i < sections.size(); i++) {
      List<TextReport<?>> partReports = getNextResult(service);
      ReportUtils.mergeReports(reports, partReports);
    }
  }

  private List<TextReport<?>> getNextResult(ExecutorCompletionService<List<TextReport<?>>> service) throws Exception {
    try {
      return service.take().get();
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

  private Callable<List<TextReport<?>>> createReportRun(final FileSection fileSection) {
    return new Callable<List<TextReport<?>>>() {
      public List<TextReport<?>> call() throws Exception {
        return processFileSection(fileSection);
      }
    };
  }

  protected List<TextReport<?>> processFileSection(FileSection fileSection) throws Exception {
    List<TextReport<?>> reportsCopy = ReportUtils.getReportsCopy(reports);

    ReportEngine reportEngine = reportEngineFactory.createEngine();
    reportEngine.setLogFormat(format);

    ReportPrinter reportPrinter = new NullReportPrinter();
    reportPrinter.initialise();

    reportEngine.setReportPrinter(reportPrinter);

    reportEngine.setReports(reportsCopy);

    reportEngine.initialise();

    reportEngine.process(fileSection.getFile(), fileSection.getIntRange());

    reportEngine.completed();

    return reportsCopy;
  }
}
