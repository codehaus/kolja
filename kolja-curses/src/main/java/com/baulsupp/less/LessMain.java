package com.baulsupp.less;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;

import com.baulsupp.kolja.log.viewer.importing.LogFormat;
import com.baulsupp.kolja.log.viewer.importing.PlainTextLogFormat;
import com.baulsupp.kolja.log.viewer.importing.SavedLogFormatLoader;
import com.baulsupp.kolja.util.LogConfig;

public class LessMain {
  private static final Logger log = Logger.getLogger(LessMain.class);

  public static void main(String[] args) throws Exception {
    LogConfig.config("less");
    
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
      File f = new File(cmd.getArgs()[0]);

      LogFormat format;

      try {
        if (cmd.hasOption("c")) {
          format = (LogFormat) Class.forName(cmd.getOptionValue("c")).newInstance();
        } else if (cmd.hasOption("x")) {
          String configName = cmd.getOptionValue("x");
          format = SavedLogFormatLoader.load(configName);
        } else {
          format = new PlainTextLogFormat();
        }

        Less tool = new Less();
        tool.open(format, f);
        
        if (cmd.hasOption("b")) {
          tool.startBackgroundThread();
        }
        
        tool.show();
      } catch (Throwable e) {
        log.error("error", e);
      }
    }
  }

  private static void printHelp(Options options) {
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp("less", options);
  }

  private static synchronized Options buildOptions() {
    Options options = new Options();

    options.addOption(OptionBuilder.hasArg(false).withDescription("usage information").withLongOpt("help").create('h'));

    options.addOption(OptionBuilder.withArgName("formatClass").hasArg().withDescription(
        "An implementation of LogFormat to use").withLongOpt("formatClass").create('c'));

    options.addOption(OptionBuilder.withArgName("formatConfig").hasArg().withDescription("Log format definition")
        .withLongOpt("formatConfig").create('x'));

    options.addOption(OptionBuilder.withDescription("Incremental Background Processing")
        .withLongOpt("background").create('b'));

    return options;
  }
}
