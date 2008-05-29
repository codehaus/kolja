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
package com.baulsupp.kolja.ansi.reports;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import jline.Terminal;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import com.baulsupp.kolja.ansi.reports.engine.DefaultReportEngineFactory;
import com.baulsupp.kolja.ansi.reports.engine.ReportEngine;
import com.baulsupp.kolja.ansi.reports.engine.ReportEngineFactory;
import com.baulsupp.kolja.log.viewer.importing.KoljaPropertyEditorRegistrar;
import com.baulsupp.kolja.log.viewer.importing.LogFormat;
import com.baulsupp.kolja.log.viewer.importing.SavedLogFormatLoader;
import com.baulsupp.kolja.log.viewer.importing.SpringBeanLogFormatLoader;
import com.baulsupp.kolja.util.services.BeanBuilder;
import com.baulsupp.kolja.util.services.BeanFactory;
import com.baulsupp.kolja.util.services.NamedService;
import com.baulsupp.kolja.util.services.ServiceFactory;

public class ReportRunnerMain {
  private static final Logger log = LoggerFactory.getLogger(ReportRunnerMain.class);

  public static void main(String... args) {
    Terminal.setupTerminal();

    CommandLineParser parser = new PosixParser();
    Options options = buildOptions();
    CommandLine cmd = null;
    try {
      cmd = parser.parse(options, args, true);
    } catch (ParseException pe) {
      printHelp(options);
      System.exit(2);
    }

    if (cmd.hasOption("h")) {
      printHelp(options);
    } else {
      try {
        run(cmd);
      } catch (FileNotFoundException e) {
        handleError(e, "file not found", e.getMessage());
      } catch (Exception e) {
        handleError(e, "error", e.getMessage());
      }
    }
  }

  private static void handleError(Throwable pe, String type, String string) {
    System.err.println(type + ": " + string);
    pe.printStackTrace(System.err);
    log.error(type, pe);
  }

  private static void run(CommandLine cmd) throws Exception {
    String configName = cmd.getOptionValue("x");
    ConfigurableListableBeanFactory appCtxt = SavedLogFormatLoader.loadAppContext(configName);

    LogFormat format = SpringBeanLogFormatLoader.getLogFormat(appCtxt);

    final ReportPrinter reportPrinter = createReportPrinter(cmd, format);

    final ReportEngine reportEngine = createReportEngine(cmd, format);

    reportPrinter.setReportEngine(reportEngine);
    reportEngine.setReportPrinter(reportPrinter);

    String[] reportValues = cmd.getOptionValues("r");

    if (reportValues == null) {
      throw new RuntimeException("no report specified");
    }

    List<String> v = new ArrayList<String>(Arrays.asList(reportValues));

    List<String> filenames = unfutzArgs(v);

    log.info("reports " + v);
    log.info("files " + filenames);

    reportEngine.setReportDescriptions(v);

    List<File> commandFiles = commandFiles(filenames);

    reportEngine.initialise();

    reportEngine.process(commandFiles);

    reportEngine.completed();
  }

  private static ReportPrinter createReportPrinter(CommandLine cmd, LogFormat format) throws Exception {
    final ReportPrinter reportPrinter;

    if (cmd.hasOption('p')) {
      BeanFactory<ReportPrinter> builder = createServiceBuilder(ReportPrinter.class);

      reportPrinter = builder.create(cmd.getOptionValue('p'));
    } else {
      reportPrinter = new AnsiReportPrinter();
    }

    reportPrinter.initialise(format);

    return reportPrinter;
  }

  private static ReportEngine createReportEngine(CommandLine cmd, LogFormat format) throws Exception {
    ReportEngineFactory reportEngineFactory;

    if (cmd.hasOption('g')) {
      BeanFactory<ReportEngineFactory> builder = createServiceBuilder(ReportEngineFactory.class);

      reportEngineFactory = builder.create(cmd.getOptionValue('g'));
    } else {
      reportEngineFactory = new DefaultReportEngineFactory();
    }

    ReportEngine reportEngine = reportEngineFactory.createEngine();

    reportEngine.setLogFormat(format);

    return reportEngine;
  }

  private static <T extends NamedService> BeanFactory<T> createServiceBuilder(Class<T> type) {
    PropertyEditorRegistrar propertyEditorRegistrar = new KoljaPropertyEditorRegistrar();
    return new BeanBuilder<T>(propertyEditorRegistrar, new ServiceFactory<T>(type));
  }

  private static List<String> unfutzArgs(List<String> v) {
    boolean isFiles = false;
    List<String> files = new ArrayList<String>();

    for (Iterator<String> i = v.iterator(); i.hasNext();) {
      String string = i.next();

      if (isFiles) {
        i.remove();
        files.add(string);
      }

      if (string.equals("--")) {
        i.remove();
        isFiles = true;
      }

    }

    return files;
  }

  private static List<File> commandFiles(List<String> filenames) {
    List<File> files = new ArrayList<File>();

    for (String a : filenames) {
      files.add(new File(a));
    }

    return files;
  }

  private static void printHelp(Options options) {
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp("report", options);
  }

  @SuppressWarnings("static-access")
  private static synchronized Options buildOptions() {
    Options options = new Options();

    options.addOption(OptionBuilder.hasArg(false).withDescription("usage information").withLongOpt("help").create('h'));

    options.addOption(OptionBuilder.hasArg(false).withDescription("B&W Coloring").create('a'));

    options.addOption(OptionBuilder.withArgName("formatConfig").hasArg().withDescription("Log format definition").withLongOpt(
        "formatConfig").create('x'));

    options.addOption(OptionBuilder.withArgName("outputFormat").hasArg().withDescription("printf output format").withLongOpt(
        "printf").create('o'));

    options.addOption(OptionBuilder.withArgName("engine").hasArgs().withDescription("Report Engine").withLongOpt("engine").create(
        'g'));

    options.addOption(OptionBuilder.withArgName("report").hasArgs().withDescription("report").withLongOpt("report").create('r'));

    options.addOption(OptionBuilder.hasArg(false).withDescription("Fixed Screen Width").withLongOpt("fixed-width").create('f'));

    options.addOption(OptionBuilder.hasArg(false).withDescription("Interactive").withLongOpt("interactive").create('i'));

    options.addOption(OptionBuilder.withArgName("printer").hasArgs().withDescription("Report Printer").withLongOpt("printer")
        .create('p'));

    return options;
  }
}
