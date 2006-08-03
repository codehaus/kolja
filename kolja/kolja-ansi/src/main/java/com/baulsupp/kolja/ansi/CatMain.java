package com.baulsupp.kolja.ansi;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import jline.Terminal;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;

import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.viewer.importing.LogFormat;
import com.baulsupp.kolja.log.viewer.importing.PlainTextLogFormat;
import com.baulsupp.kolja.log.viewer.importing.SavedLogFormatLoader;
import com.baulsupp.kolja.log.viewer.renderer.DebugRenderer;
import com.baulsupp.kolja.log.viewer.renderer.PrintfRenderer;
import com.baulsupp.kolja.util.LogConfig;

public class CatMain {
  private static final Logger log = Logger.getLogger(CatMain.class);

  public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, InterruptedException {
    LogConfig.config("cat");
    
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
      final Cat cat = new Cat();

      LogFormat format = loadFormat(cmd);

      cat.setAnsi(!cmd.hasOption("a"));

      Iterator<Line> bli = loadLineIterator(cmd, format);
      
      cat.load(bli, format.getRenderer());
      
      if (cmd.hasOption("o")) {
        cat.setRenderer(PrintfRenderer.parse(cmd.getOptionValue("o")));
      }
      
      if (cmd.hasOption("d")) {
        cat.setRenderer(new DebugRenderer());
      }

      String highlightTerm = null;
      if (cmd.hasOption("s")) {
        highlightTerm = cmd.getOptionValue("s");
        cat.addHighlightTerm(highlightTerm);
      }

      if (!cmd.hasOption("i")) {
        Thread t = new Thread("InputProcessor") {
          public void run() {
            try {
              cat.processInput();
            } catch (IOException e) {
              log.error("error", e);
            }
          }
        };
        t.setDaemon(true);
        t.start();
      }

      cat.run();
    }
  }

  public static Iterator<Line> loadLineIterator(CommandLine cmd, LogFormat format) throws IOException {
    Iterator<Line> bli;
    boolean byRequest = cmd.hasOption("r");
    
    if (cmd.hasOption("i")) {
      bli = IoUtil.load(IoUtil.fromStdin(), format);
    } else if (cmd.getArgs().length == 0) {
      throw new RuntimeException("at least one filename expected");
    } else if (cmd.getArgs().length == 1) {
      File f = new File(cmd.getArgs()[0]);
      
      bli = IoUtil.loadLineIterator(format, byRequest, f);        
    } else {
      MultipleLineIterator mli = new MultipleLineIterator();
      
      for (String s : cmd.getArgs()) {
        File file = new File(s);
        mli.add(getShortName(file), IoUtil.loadLineIterator(format, byRequest, file));
      }
      
      bli = mli;
    }
    
    return bli;
  }

  private static String getShortName(File file) {
    return file.getName();
  }

  public static LogFormat loadFormat(CommandLine cmd) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException {
    LogFormat format;
    if (cmd.hasOption("c")) {
      format = (LogFormat) Class.forName(cmd.getOptionValue("c")).newInstance();
    } else if (cmd.hasOption("x")) {
      String configName = cmd.getOptionValue("x");
      format = SavedLogFormatLoader.load(configName);
    } else {
      format = new PlainTextLogFormat();
    }
    return format;
  }

  private static void printHelp(Options options) {
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp("cat", options);
  }

  private static synchronized Options buildOptions() {
    Options options = new Options();

    options.addOption(OptionBuilder.hasArg(false).withDescription("usage information").withLongOpt("help").create('h'));

    options.addOption(OptionBuilder.hasArg(false).withDescription("Group By Requests").withLongOpt("request").create(
        'r'));

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

    return options;
  }
}
