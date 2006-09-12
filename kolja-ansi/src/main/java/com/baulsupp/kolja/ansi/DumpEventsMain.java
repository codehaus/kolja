package com.baulsupp.kolja.ansi;

import java.io.File;
import java.io.IOException;

import jline.Terminal;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;

import com.baulsupp.kolja.log.line.BasicLineIterator;
import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.line.LineIndex;
import com.baulsupp.kolja.log.util.ReloadableCharBuffer;
import com.baulsupp.kolja.log.viewer.event.Event;
import com.baulsupp.kolja.log.viewer.event.EventList;
import com.baulsupp.kolja.log.viewer.importing.LogFormat;
import com.baulsupp.kolja.log.viewer.importing.PlainTextLogFormat;
import com.baulsupp.kolja.log.viewer.importing.SavedLogFormatLoader;
import com.baulsupp.kolja.util.LogConfig;

public class DumpEventsMain {
  @SuppressWarnings("unused")
  private static final Logger logger = Logger.getLogger(DumpEventsMain.class);

  public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException  {
    LogConfig.config("dumpevents");
    
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
      LogFormat format;
      if (cmd.hasOption("c")) {
        format = (LogFormat) Class.forName(cmd.getOptionValue("c")).newInstance();
      } else if (cmd.hasOption("x")) {
        String configName = cmd.getOptionValue("x");
        format = SavedLogFormatLoader.load(configName);
      } else {
        format = new PlainTextLogFormat();
      }
      
      File f = new File(cmd.getArgs()[0]);
      ReloadableCharBuffer buffer = ReloadableCharBuffer.fromFileReloadable(f);
      
      LineIndex lineIndex = format.getLineIndex(buffer);

      BasicLineIterator i = new BasicLineIterator(lineIndex);
      
      EventList eventIndex = format.getEventList(lineIndex);
      
      while (i.hasNext()) {
        Line line = i.next();
        Event event = eventIndex.readEvent(line);
        
        if (event != null) {
          System.out.println(event.toString());
        }
      }
    }
  }

  private static void printHelp(Options options) {
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp("dumpevents", options);
  }

  private static synchronized Options buildOptions() {
    Options options = new Options();

    options.addOption(OptionBuilder.hasArg(false).withDescription("usage information").withLongOpt("help").create('h'));

    options.addOption(OptionBuilder.withArgName("formatClass").hasArg().withDescription(
        "An implementation of LogFormat to use").withLongOpt("formatClass").create('c'));

    options.addOption(OptionBuilder.withArgName("formatConfig").hasArg().withDescription("Log format definition")
        .withLongOpt("formatConfig").create('x'));

    return options;
  }
}
