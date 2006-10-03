package com.baulsupp.kolja.ansi;

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

import com.baulsupp.kolja.log.LogConstants;
import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.util.TruncationException;
import com.baulsupp.kolja.log.viewer.highlight.FilenameHighlight;
import com.baulsupp.kolja.log.viewer.importing.LogFormat;
import com.baulsupp.kolja.log.viewer.io.IoUtil;
import com.baulsupp.kolja.log.viewer.renderer.DebugRenderer;
import com.baulsupp.kolja.log.viewer.renderer.FieldRenderer;
import com.baulsupp.kolja.log.viewer.renderer.PrintfRenderer;
import com.baulsupp.kolja.log.viewer.renderer.Renderer;
import com.baulsupp.kolja.util.LogConfig;

public class TailMinusEffMain {
  private static final Logger log = Logger.getLogger(TailMinusEffMain.class);

  public static void main(String[] args) {
    LogConfig.config("tail");
    
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

  private static void run(CommandLine cmd) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException, InterruptedException {
    final TailMinusEff tail = new TailMinusEff();

    LogFormat format = CatMain.loadFormat(cmd);

    tail.setAnsi(!cmd.hasOption("a"));

    List<File> files = commandFiles(cmd);
    Iterator<Line> bli = IoUtil.loadFiles(format, files, true);
    
    tail.setI(bli);
    
    if (cmd.hasOption("o")) {
      tail.setRenderer(PrintfRenderer.parse(cmd.getOptionValue("o")));
    } else if (cmd.hasOption("d")) {
      tail.setRenderer(new DebugRenderer());
    } else {
      Renderer<Line> renderer = format.getRenderer();
      if (files.size() > 1 && renderer instanceof FieldRenderer) {
        FieldRenderer fieldRenderer = (FieldRenderer) renderer;
        fieldRenderer.prependColumn(LogConstants.FILE_NAME, IoUtil.getMaxFilenameWidth(files));
        fieldRenderer.addHighlight(new FilenameHighlight());
      }
      tail.setGrid(renderer);
    }
    
    if (cmd.hasOption("f")) {
      tail.setFixedWidth(true);
    }

    String highlightTerm = null;
    if (cmd.hasOption("s")) {
      highlightTerm = cmd.getOptionValue("s");
      tail.addHighlightTerm(highlightTerm);
    }

    if (!cmd.hasOption("i")) {
      Thread t = new Thread("InputProcessor") {
        public void run() {
          try {
            tail.processInput();
          } catch (IOException e) {
            log.error("error", e);
          }
        }
      };
      t.setDaemon(true);
      t.start();
    }

    try {
      tail.run();
    } catch (TruncationException te) {
      // TODO implement
      throw new UnsupportedOperationException();
    }
  }

  private static void handleError(Throwable pe, String type, String string) {
    System.err.println(type + ": " + string);
    log.error(type, pe);
  }

  private static List<File> commandFiles(CommandLine cmd) {
    List args = cmd.getArgList();
    List<File> files = new ArrayList<File>();
    
    for (Object a : args) {
      files.add(new File((String) a));
    }
    
    return files;
  }

  private static void printHelp(Options options) {
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp("tail", options);
  }

  private static synchronized Options buildOptions() {
    Options options = new Options();

    options.addOption(OptionBuilder.hasArg(false).withDescription("usage information").withLongOpt("help").create('h'));

    options.addOption(OptionBuilder.hasArg(false).withDescription("B&W Coloring").create('a'));

    options.addOption(OptionBuilder.hasArg(false).withDescription("Read from STDIN").withLongOpt("stdin").create('i'));

    options.addOption(OptionBuilder.withArgName("highlightTerm").hasArg().withDescription("An expression to highlight")
        .withLongOpt("highlightTerm").create('s'));

    options.addOption(OptionBuilder.withArgName("formatClass").hasArg().withDescription(
        "An implementation of LogFormat to use").withLongOpt("formatClass").create('c'));

    options.addOption(OptionBuilder.withArgName("formatConfig").hasArg().withDescription("Log format definition")
        .withLongOpt("formatConfig").create('x'));
    
    options.addOption(OptionBuilder.withArgName("outputFormat").hasArg().withDescription("printf output format")
        .withLongOpt("printf").create('o'));
    
    options.addOption(OptionBuilder.hasArg(false).withDescription("Debug Output").withLongOpt("debug").create(
        'd'));
    
    options.addOption(OptionBuilder.hasArg(false).withDescription("Fixed Screen Width").withLongOpt("fixed-width").create(
        'f'));

    return options;
  }
}
