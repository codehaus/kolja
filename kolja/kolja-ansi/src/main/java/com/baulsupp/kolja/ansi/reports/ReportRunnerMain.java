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
import java.util.List;

import jline.Terminal;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import com.baulsupp.kolja.ansi.ConsoleRenderer;
import com.baulsupp.kolja.ansi.TailRenderer;
import com.baulsupp.kolja.log.line.BasicLineIterator;
import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.line.LineIndex;
import com.baulsupp.kolja.log.util.WrappedCharBuffer;
import com.baulsupp.kolja.log.viewer.event.EventList;
import com.baulsupp.kolja.log.viewer.importing.LogFormat;
import com.baulsupp.kolja.log.viewer.importing.SavedLogFormatLoader;
import com.baulsupp.kolja.log.viewer.importing.SpringBeanLogFormatLoader;
import com.baulsupp.kolja.log.viewer.renderer.Renderer;
import com.baulsupp.kolja.log.viewer.request.RequestIndex;
import com.baulsupp.kolja.util.LogConfig;

public class ReportRunnerMain {
  private static final Logger log = Logger.getLogger(ReportRunnerMain.class);

  public static void main(String... args) {
    LogConfig.config("report");

    Terminal.setupTerminal();

    CommandLineParser parser = new PosixParser();
    Options options = buildOptions();
    CommandLine cmd = null;
    try {
      cmd = parser.parse(options, args, true);
    } catch (ParseException pe) {
      System.out.println("error: " + pe.getMessage());
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
    log.error(type, pe);
  }

  private static void run(CommandLine cmd) throws Exception {
    final AnsiReportRunner reportRunner = new AnsiReportRunner();

    String configName = cmd.getOptionValue("x");
    ConfigurableListableBeanFactory appCtxt = SavedLogFormatLoader.loadAppContext(configName);

    LogFormat format = SpringBeanLogFormatLoader.getLogFormat(appCtxt);

    boolean ansi = !cmd.hasOption("a");

    String optionValue = cmd.getOptionValue("r");

    if (optionValue == null) {
      throw new RuntimeException("no report specified");
    }

    reportRunner.setReports(createReports(new ReportBuilder(appCtxt), optionValue));

    boolean fixedWidth = cmd.hasOption("f");

    Renderer<Line> renderer = format.getLineRenderer();
    ConsoleRenderer<Line> lineRenderer = createRenderer(renderer, ansi, fixedWidth);
    reportRunner.setLineRenderer(lineRenderer);

    renderer = format.getRequestRenderer();
    ConsoleRenderer<Line> requestRenderer = createRenderer(renderer, ansi, fixedWidth);
    reportRunner.setRequestRenderer(requestRenderer);

    List<File> commandFiles = commandFiles(cmd);

    reportRunner.initialise();

    for (File file : commandFiles) {
      if (commandFiles.size() == 1) {
        WrappedCharBuffer buffer = WrappedCharBuffer.fromFile(file);

        LineIndex li = format.getLineIndex(buffer);

        if (format.supportsRequests()) {
          RequestIndex requestIndex = format.getRequestIndex(li);
          reportRunner.setRequestIndex(requestIndex);
        }

        if (format.supportsEvents()) {
          EventList eventList = format.getEventList(li);
          reportRunner.setEventList(eventList);
        }

        reportRunner.run(file, new BasicLineIterator(li));
      }
    }

    reportRunner.completed();
  }

  private static ConsoleRenderer<Line> createRenderer(Renderer<Line> renderer, boolean ansi, boolean fixedWidth) {
    renderer.setWidth(Terminal.getTerminal().getTerminalWidth());
    TailRenderer tr = new TailRenderer(renderer, ansi);
    tr.setFixedWidth(fixedWidth);
    return tr;
  }

  private static List<TextReport> createReports(ReportBuilder builder, String option) throws Exception {
    List<TextReport> reports = new ArrayList<TextReport>();

    String[] strings = option.split(",");

    for (String string : strings) {
      reports.add(builder.buildReport(string));
    }

    return reports;
  }

  @SuppressWarnings("unchecked")
  private static List<File> commandFiles(CommandLine cmd) {
    List<String> args = cmd.getArgList();
    List<File> files = new ArrayList<File>();

    for (String a : args) {
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

    options.addOption(OptionBuilder.withArgName("report").hasArg().withDescription("report").withLongOpt("report").create('r'));

    options.addOption(OptionBuilder.hasArg(false).withDescription("Fixed Screen Width").withLongOpt("fixed-width").create('f'));

    return options;
  }
}
