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
package com.baulsupp.less;

import com.baulsupp.kolja.log.util.WrappedCharBuffer;
import com.baulsupp.kolja.log.viewer.commands.*;
import com.baulsupp.kolja.log.viewer.event.EventList;
import com.baulsupp.kolja.log.viewer.importing.LogFormat;
import com.baulsupp.kolja.log.viewer.importing.PlainTextLogFormat;
import com.baulsupp.kolja.log.viewer.importing.SavedLogFormatLoader;
import com.baulsupp.kolja.log.viewer.linenumbers.BasicLineNumberIndex;
import com.baulsupp.kolja.log.viewer.request.StandardRequestIndex;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Launcher for Less Tool
 */
public class LessMain {
  private static final Logger log = LoggerFactory.getLogger(LessMain.class);

  public static void main(String... args) {
    CommandLineParser parser = new PosixParser();
    Options options = buildOptions();
    CommandLine cmd = null;
    try {
      cmd = parser.parse(options, args, true);
    } catch (ParseException pe) {
      handleError(pe, "config error", pe.getMessage());
      printHelp(options);
      System.exit(2);
    }

    if (cmd.hasOption("h") || cmd.getArgs().length == 0) {
      printHelp(options);
    } else {
      File f = new File(cmd.getArgs()[0]);

      try {
        runTool(cmd, f);
      } catch (FileNotFoundException fnfe) {
        handleError(fnfe, "error", "file not found: " + f);
      } catch (Throwable e) {
        handleError(e, "error", e.getMessage());
      }
    }
  }

  private static void runTool(CommandLine cmd, File f) throws Exception {
    LogFormat format;
    if (cmd.hasOption("c")) {
      format = (LogFormat) Class.forName(cmd.getOptionValue("c")).newInstance();
    } else if (cmd.hasOption("x")) {
      String configName = cmd.getOptionValue("x");
      format = SavedLogFormatLoader.load(configName);
    } else {
      format = new PlainTextLogFormat();
    }

    WrappedCharBuffer buffer = WrappedCharBuffer.fromFile(f);

    Less tool = new Less();
    tool.createDefaultCommands();
    tool.addCommand(new RendererCommand(tool, format));
    tool.addCommand(new ModelsCommand(format, buffer, tool));

    if (format.supportsEvents()) {
      EventList eventList = format.getEventList(tool.getLineIndex());
      tool.getLineIndex().addLineListener(eventList);

      tool.addCommand(new SelectEventCommand(eventList));

      if (cmd.hasOption("b")) {
        BackgroundProcess process = new BackgroundProcess(tool, eventList);
        process.startBackgroundThread();
      }
    }

    if (format.supportsRequests()) {
      StandardRequestIndex requestIndex = format.getRequestIndex(tool.getLineIndex());
      tool.getLineIndex().addLineListener(requestIndex);

      tool.addCommand(new SelectRequestCommand(requestIndex));

      if (cmd.hasOption("b")) {
        BackgroundProcess process = new BackgroundProcess(tool, requestIndex);
        process.startBackgroundThread();
      }
    }

    tool.addCommand(new ScrollbarCommand(tool, buffer));

    tool.setLineNumbers(BasicLineNumberIndex.create(buffer));

    tool.show();
  }

  private static void handleError(Throwable pe, String type, String string) {
    System.err.println(type + ": " + string);
    log.error(type, pe);
  }

  private static void printHelp(Options options) {
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp("less", options);
  }

  @SuppressWarnings("static-access")
  private static synchronized Options buildOptions() {
    Options options = new Options();

    options.addOption(OptionBuilder.hasArg(false).withDescription("usage information").withLongOpt("help").create('h'));

    options.addOption(OptionBuilder.withArgName("formatClass").hasArg().withDescription("An implementation of LogFormat to use")
        .withLongOpt("formatClass").create('c'));

    options.addOption(OptionBuilder.withArgName("formatConfig").hasArg().withDescription("Log format definition").withLongOpt(
        "formatConfig").create('x'));

    options.addOption(OptionBuilder.withDescription("Incremental Background Processing").withLongOpt("background").create('b'));

    return options;
  }
}
