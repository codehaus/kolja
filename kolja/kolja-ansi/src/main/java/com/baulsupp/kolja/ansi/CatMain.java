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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.viewer.importing.LogFormat;
import com.baulsupp.kolja.log.viewer.importing.PlainTextLogFormat;
import com.baulsupp.kolja.log.viewer.importing.SavedLogFormatLoader;
import com.baulsupp.kolja.log.viewer.io.IoUtil;
import com.baulsupp.kolja.log.viewer.renderer.DebugRenderer;
import com.baulsupp.kolja.log.viewer.renderer.PrintfRenderer;
import com.baulsupp.kolja.log.viewer.renderer.Renderer;

public class CatMain {
  private static final Logger log = LoggerFactory.getLogger(CatMain.class);

  public static void main(String... args) {
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

  private static void run(CommandLine cmd) throws Exception, IOException, InterruptedException {
    final Cat cat = new Cat();

    LogFormat format = loadFormat(cmd);

    cat.setAnsi(!cmd.hasOption("a"));

    Iterator<Line> bli = loadLineIterator(cmd, format);
    cat.setI(bli);

    if (cmd.hasOption("o")) {
      cat.setRenderer(PrintfRenderer.parse(cmd.getOptionValue("o")));
    } else if (cmd.hasOption("d")) {
      cat.setRenderer(new DebugRenderer());
    } else {
      Renderer<Line> renderer = format.getLineRenderer();
      cat.setGrid(renderer);
    }

    String highlightTerm = null;
    if (cmd.hasOption("s")) {
      highlightTerm = cmd.getOptionValue("s");
      cat.addHighlightTerm(highlightTerm);
    }

    if (cmd.hasOption("f")) {
      cat.setFixedWidth(true);
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

  public static LogFormat loadFormat(CommandLine cmd) throws Exception {
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

  @SuppressWarnings("static-access")
  private static synchronized Options buildOptions() {
    Options options = new Options();

    options.addOption(OptionBuilder.hasArg(false).withDescription("usage information").withLongOpt("help").create('h'));

    options.addOption(OptionBuilder.hasArg(false).withDescription("B&W Coloring").create('a'));

    options.addOption(OptionBuilder.hasArg(false).withDescription("Read from STDIN").withLongOpt("stdin").create('i'));

    options.addOption(OptionBuilder.withArgName("highlightTerm").hasArg().withDescription("An expression to highlight")
        .withLongOpt("highlightTerm").create('s'));

    options.addOption(OptionBuilder.withArgName("formatClass").hasArg().withDescription("An implementation of LogFormat to use")
        .withLongOpt("formatClass").create('c'));

    options.addOption(OptionBuilder.withArgName("formatConfig").hasArg().withDescription("Log format definition").withLongOpt(
        "formatConfig").create('x'));

    options.addOption(OptionBuilder.withArgName("outputFormat").hasArg().withDescription("printf output format").withLongOpt(
        "printf").create('o'));

    options.addOption(OptionBuilder.hasArg(false).withDescription("Debug Output").withLongOpt("debug").create('d'));

    options.addOption(OptionBuilder.hasArg(false).withDescription("Fixed Screen Width").withLongOpt("fixed-width").create('f'));

    return options;
  }
}
