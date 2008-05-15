/**
 * Copyright (c) 2002-2007 Yuri Schimke. All Rights Reserved.
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package com.baulsupp.kolja.log.viewer.io;

import java.io.File;
import java.util.regex.Pattern;

import org.springframework.util.ClassUtils;
import org.springframework.util.StopWatch;

import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.line.LineIterator;
import com.baulsupp.kolja.log.line.LineParser;
import com.baulsupp.kolja.log.viewer.importing.PlainTextLineParser;

/**
 * @author Yuri Schimke
 */
public abstract class BenchmarkIterator {
  private LineIterator i;

  protected Pattern pattern;
  protected LineParser lineParser;
  protected File file;

  private int lineCount;

  protected StopWatch sw;

  public void setPattern(Pattern pattern) {
    this.pattern = pattern;
  }

  public void setLineParser(LineParser lineParser) {
    this.lineParser = lineParser;
  }

  public void setFile(File file) {
    this.file = file;
  }

  public void run() throws Exception {
    setPattern(Pattern.compile("^", Pattern.MULTILINE));
    setLineParser(new PlainTextLineParser());

    setFile(new File("../kolja-widefinder/src/test/logs/o100k.ap"));

    runBenchmark();
  }

  protected void runBenchmark() throws Exception {
    sw = new StopWatch(file.getName());

    sw.start("create iterator");
    i = createIterator();
    sw.stop();

    sw.start(ClassUtils.getShortName(i.getClass()));
    processFile();
    sw.stop();

    System.out.println(sw.prettyPrint());
    System.out.println(lineCount + " lines");
  }

  private void processFile() {
    lineCount = 0;

    while (i.hasNext()) {
      Line l = i.next();

      l.toString().hashCode();

      lineCount++;
    }
  }

  protected abstract LineIterator createIterator() throws Exception;

}
