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
    final AnsiReportRunner reportRunner = new AnsiReportRunner();

    boolean interactive = !cmd.hasOption("n");
    reportRunner.setInteractive(interactive);

    String configName = cmd.getOptionValue("x");
    ConfigurableListableBeanFactory appCtxt = SavedLogFormatLoader.loadAppContext(configName);

    LogFormat format = SpringBeanLogFormatLoader.getLogFormat(appCtxt);

    boolean ansi = !cmd.hasOption("a");

    String[] reportValues = cmd.getOptionValues("r");

    if (reportValues == null) {
      throw new RuntimeException("no report specified");
    }

    List<String> v = new ArrayList<String>(Arrays.asList(reportValues));

    List<String> filenames = unfutzArgs(v);

    log.info("reports " + v);
    log.info("files " + filenames);

    reportRunner.setReports(createReports(new ReportBuilder(appCtxt), v));

    boolean fixedWidth = cmd.hasOption("f");

    Renderer<Line> renderer = format.getLineRenderer();
    ConsoleRenderer<Line> lineRenderer = createRenderer(renderer, ansi, fixedWidth);
    reportRunner.setLineRenderer(lineRenderer);

    renderer = format.getRequestRenderer();
    ConsoleRenderer<Line> requestRenderer = createRenderer(renderer, ansi, fixedWidth);
    reportRunner.setRequestRenderer(requestRenderer);

    List<File> commandFiles = commandFiles(filenames);

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

  private static ConsoleRenderer<Line> createRenderer(Renderer<Line> renderer, boolean ansi, boolean fixedWidth) {
    renderer.setWidth(Terminal.getTerminal().getTerminalWidth());
    TailRenderer tr = new TailRenderer(renderer, ansi);
    tr.setFixedWidth(fixedWidth);
    return tr;
  }

  private static List<TextReport> createReports(ReportBuilder builder, List<String> v) throws Exception {
    List<TextReport> reports = new ArrayList<TextReport>();

    for (String string : v) {
      reports.add(builder.buildReport(string));
    }

    log.info("" + reports);

    return reports;
  }

  @SuppressWarnings("unchecked")
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

    options.addOption(OptionBuilder.withArgName("report").hasArgs().withDescription("report").withLongOpt("report").create('r'));

    options.addOption(OptionBuilder.hasArg(false).withDescription("Fixed Screen Width").withLongOpt("fixed-width").create('f'));

    options.addOption(OptionBuilder.hasArg(false).withDescription("Non Interactive").withLongOpt("non-interactive").create('n'));

    return options;
  }
}
