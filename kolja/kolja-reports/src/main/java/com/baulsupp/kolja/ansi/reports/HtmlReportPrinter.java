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
package com.baulsupp.kolja.ansi.reports;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.baulsupp.kolja.ansi.reports.engine.ReportEngine;
import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.viewer.importing.LogFormat;
import com.baulsupp.kolja.log.viewer.renderer.Renderer;
import com.baulsupp.kolja.log.viewer.request.RequestLine;
import com.baulsupp.kolja.util.colours.Colour;
import com.baulsupp.kolja.util.colours.ColouredString;
import com.baulsupp.kolja.util.colours.MultiColourString;

/**
 * @author Yuri Schimke
 */
public class HtmlReportPrinter implements ReportPrinter {
  private FileWriter fw;

  private Renderer<Line> renderer;

  private Renderer<Line> requestRenderer;

  public String getName() {
    return "html";
  }

  public void setRequestRenderer(Renderer<Line> requestRenderer) {
    this.requestRenderer = requestRenderer;
  }

  public void setRenderer(Renderer<Line> renderer) {
    this.renderer = renderer;
  }

  public void initialise(LogFormat format) throws IOException {
    setRequestRenderer(format.getRequestRenderer());
    setRenderer(format.getLineRenderer());

    fw = new FileWriter("./out.html");

    fw.write("<html><head><title>Kolja Report</title></head><body>");
  }

  public void completed() throws IOException {
    fw.write("</body></html>");

    fw.close();
  }

  public void printLine(Line line) {
    toHtml(renderer, line);
  }

  public void printLine() {
    write("<br/>\n");
  }

  public void printRequest(RequestLine request) {
    toHtml(requestRenderer, request);
  }

  public void printTitle(String title) {
    write("<h3>" + title + "</h3>");
  }

  public void println(MultiColourString string) {
    for (ColouredString s : string.getColouredStrings()) {
      write("<span style=\"color: " + getHtmlColor(s.getForegroundColor()) + "\">");
      write(s.toString());
      write("</span>");
    }

    printLine();
  }

  private String getHtmlColor(Colour color) {
    if (color == null) {
      color = Colour.BLACK ;
    }

    // TODO use real colours

    return color.toString();
  }

  private void write(String string) {
    try {
      fw.write(string);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void toHtml(Renderer<Line> renderer, Line line) {
    List<MultiColourString> lines = renderer.getRow(line).getLines();

    for (MultiColourString s : lines) {
      println(s);
    }
  }

  public void setReportEngine(ReportEngine reportEngine) {
  }
}
