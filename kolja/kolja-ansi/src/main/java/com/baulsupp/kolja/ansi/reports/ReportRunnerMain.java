package com.baulsupp.kolja.ansi.reports;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
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
import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;

import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.viewer.importing.LogFormat;
import com.baulsupp.kolja.log.viewer.importing.SavedLogFormatLoader;
import com.baulsupp.kolja.log.viewer.importing.SpringBeanLogFormatLoader;
import com.baulsupp.kolja.log.viewer.io.IoUtil;
import com.baulsupp.kolja.log.viewer.renderer.PrintfRenderer;
import com.baulsupp.kolja.log.viewer.renderer.Renderer;
import com.baulsupp.kolja.util.LogConfig;

public class ReportRunnerMain {
  private static final Logger log = Logger.getLogger(ReportRunnerMain.class);

  public static void main(String[] args) {
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
    BeanFactory appCtxt = SavedLogFormatLoader.loadAppContext(configName);

    LogFormat format = SpringBeanLogFormatLoader.getLogFormat(appCtxt);

    reportRunner.setAnsi(!cmd.hasOption("a"));

    reportRunner.setReports(createReports(appCtxt, cmd.getOptionValue("r")));

    Iterator<Line> bli = loadLineIterator(cmd, format);
    reportRunner.setI(bli);

    if (cmd.hasOption("o")) {
      reportRunner.setRenderer(PrintfRenderer.parse(cmd.getOptionValue("o")));
    } else {
      Renderer<Line> renderer = format.getRenderer();
      reportRunner.setGrid(renderer);
    }

    if (cmd.hasOption("f")) {
      reportRunner.setFixedWidth(true);
    }

    reportRunner.run();
  }

  private static List<TextReport> createReports(BeanFactory appCtxt, String option) throws Exception {
    List<TextReport> reports = new ArrayList<TextReport>();

    String[] strings = option.split(",");

    for (String string : strings) {
      reports.add((TextReport) appCtxt.getBean(string));
    }

    return reports;
  }

  public static Iterator<Line> loadLineIterator(CommandLine cmd, LogFormat format) throws IOException {
    Iterator<Line> bli;

    if (cmd.hasOption("i")) {
      bli = IoUtil.loadFromStdin(format);
    } else if (cmd.getArgs().length == 0) {
      throw new RuntimeException("at least one filename expected");
    } else {
      bli = IoUtil.loadFiles(format, commandFiles(cmd), false);
    }

    return bli;
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

    options.addOption(OptionBuilder.hasArg(false).withDescription("Read from STDIN").withLongOpt("stdin").create('i'));

    options.addOption(OptionBuilder.withArgName("formatConfig").hasArg().withDescription("Log format definition").withLongOpt(
        "formatConfig").create('x'));

    options.addOption(OptionBuilder.withArgName("outputFormat").hasArg().withDescription("printf output format").withLongOpt(
        "printf").create('o'));

    options.addOption(OptionBuilder.withArgName("report").hasArg().withDescription("report").withLongOpt("report").create('r'));

    options.addOption(OptionBuilder.hasArg(false).withDescription("Fixed Screen Width").withLongOpt("fixed-width").create('f'));

    return options;
  }
}
