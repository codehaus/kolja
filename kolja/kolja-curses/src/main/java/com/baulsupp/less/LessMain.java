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

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;

import com.baulsupp.kolja.log.util.WrappedCharBuffer;
import com.baulsupp.kolja.log.viewer.commands.BackgroundProcess;
import com.baulsupp.kolja.log.viewer.commands.ModelsCommand;
import com.baulsupp.kolja.log.viewer.commands.RendererCommand;
import com.baulsupp.kolja.log.viewer.commands.ScrollbarCommand;
import com.baulsupp.kolja.log.viewer.commands.SelectEventCommand;
import com.baulsupp.kolja.log.viewer.commands.SelectRequestCommand;
import com.baulsupp.kolja.log.viewer.importing.LogFormat;
import com.baulsupp.kolja.log.viewer.importing.PlainTextLogFormat;
import com.baulsupp.kolja.log.viewer.importing.SavedLogFormatLoader;
import com.baulsupp.kolja.log.viewer.linenumbers.BasicLineNumberIndex;
import com.baulsupp.kolja.util.LogConfig;

/**
 * Launcher for Less Tool
 */
public class LessMain {
  private static final Logger log = Logger.getLogger(LessMain.class);

  public static void main(String[] args) {
    LogConfig.config("less");

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

        WrappedCharBuffer buffer = WrappedCharBuffer.fromFile(f);

        Less tool = new Less();
        tool.createDefaultCommands();
        tool.addCommand(new RendererCommand(tool, format));
        tool.addCommand(new ModelsCommand(format, buffer, tool));
        if (format.supportsEvents()) {
          tool.addCommand(new SelectEventCommand(format, tool));
        }
        if (format.supportsRequests()) {
          tool.addCommand(new SelectRequestCommand(format, tool));
        }
        tool.addCommand(new ScrollbarCommand(tool, buffer));

        tool.setLineNumbers(BasicLineNumberIndex.create(buffer));

        if (cmd.hasOption("b")) {
          BackgroundProcess process = new BackgroundProcess(tool);
          process.startBackgroundThread();
        }

        tool.show();
      } catch (FileNotFoundException fnfe) {
        handleError(fnfe, "error", "file not found: " + f);
      } catch (Throwable e) {
        handleError(e, "error", e.getMessage());
      }
    }
  }

  private static void handleError(Throwable pe, String type, String string) {
    System.err.println(type + ": " + string);
    log.error(type, pe);
  }

  private static void printHelp(Options options) {
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp("less", options);
  }

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
