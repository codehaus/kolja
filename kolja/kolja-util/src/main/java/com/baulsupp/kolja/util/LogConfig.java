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
package com.baulsupp.kolja.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.filter.ThresholdFilter;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.WriterAppender;

/**
 * Kolja internal log configurator
 * 
 * @author Yuri Schimke
 */
public class LogConfig {
  public static void config(String string) {
    LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();

    Logger rootLogger = lc.getLogger(LoggerContext.ROOT_NAME);

    rootLogger.detachAndStopAllAppenders();

    WriterAppender<LoggingEvent> writerAppender = createAppender(string, lc);

    rootLogger.addAppender(writerAppender);
  }

  private static WriterAppender<LoggingEvent> createAppender(String string, LoggerContext lc) {
    WriterAppender<LoggingEvent> writerAppender = new WriterAppender<LoggingEvent>();
    writerAppender.setContext(lc);
    PatternLayout pl = createLayout(lc);
    writerAppender.setLayout(pl);

    OutputStream os;
    try {
      os = new FileOutputStream(string + ".log");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
    ThresholdFilter levelFilter = new ThresholdFilter();
    levelFilter.setLevel(Level.INFO.toString());
    writerAppender.addFilter(levelFilter);
    writerAppender.setWriter(new OutputStreamWriter(os));
    writerAppender.setImmediateFlush(false);
    writerAppender.start();
    return writerAppender;
  }

  private static PatternLayout createLayout(LoggerContext lc) {
    PatternLayout pl = new PatternLayout();
    pl.setContext(lc);
    pl.setPattern("%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n");
    pl.start();
    return pl;
  }

  public static void shutdown() {
    LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();

    lc.stop();
  }
}
