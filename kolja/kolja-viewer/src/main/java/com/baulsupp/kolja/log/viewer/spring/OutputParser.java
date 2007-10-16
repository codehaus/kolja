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
package com.baulsupp.kolja.log.viewer.spring;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.viewer.columns.ColumnWidths;
import com.baulsupp.kolja.log.viewer.format.CompressedPackageFormat;
import com.baulsupp.kolja.log.viewer.format.FormatFormat;
import com.baulsupp.kolja.log.viewer.format.OutputFormat;
import com.baulsupp.kolja.log.viewer.format.ToStringFormat;
import com.baulsupp.kolja.log.viewer.highlight.Highlight;
import com.baulsupp.kolja.log.viewer.highlight.HighlightList;
import com.baulsupp.kolja.log.viewer.highlight.PriorityHighlight;
import com.baulsupp.kolja.log.viewer.highlight.RegexHighlight;
import com.baulsupp.kolja.log.viewer.importing.ConfigurableOutputFormat;

/**
 * Output Parser
 * 
 * @author Yuri Schimke
 */
public class OutputParser {
  private Element element;

  public OutputParser(Element element) {
    this.element = element;
  }

  public ConfigurableOutputFormat parse() {
    ConfigurableOutputFormat outputFormat = new ConfigurableOutputFormat();

    outputFormat.setWidths(ColumnWidths.parse(XmlReaderUtil.getElementString(element, "widths")));
    outputFormat.setAdditional(XmlReaderUtil.getElementString(element, "additional"));

    outputFormat.setNames(parseNames());
    outputFormat.setFormats(parseFormats());

    outputFormat.setHighlights(parseHighlights());

    return outputFormat;
  }

  private HighlightList<Line> parseHighlights() {
    HighlightList<Line> results = new HighlightList<Line>();

    for (Element e : XmlReaderUtil.getChildElements(element, "highlights")) {
      results.addHighlight(parseHighlight(e));
    }

    return results;
  }

  private Highlight<Line> parseHighlight(Element e) {
    if (e.getNodeName().equals("priority-highlight")) {
      return parsePriorityHighlight(e);
    } else if (e.getNodeName().equals("regex-highlight")) {
      return parseRegexHighlight(e);
    }

    throw new IllegalArgumentException("unknown type '" + e.getNodeName() + "'");
  }

  private RegexHighlight parseRegexHighlight(Element e) {
    RegexHighlight regexHighlight = new RegexHighlight();

    regexHighlight.setContentField(e.getAttribute("field"));
    regexHighlight.setPattern(XmlReaderUtil.parsePattern(XmlReaderUtil.getSingleElement(element, "pattern"), 0));
    regexHighlight.setColours(XmlReaderUtil.parseColours(XmlReaderUtil.getSingleElement(element, "colours")));

    return regexHighlight;
  }

  private PriorityHighlight parsePriorityHighlight(Element e) {
    PriorityHighlight priorityHighlight = new PriorityHighlight();

    priorityHighlight.setPriorityField(e.getAttribute("field"));

    return priorityHighlight;
  }

  private List<OutputFormat> parseFormats() {
    List<OutputFormat> list = new ArrayList<OutputFormat>();

    for (Element e : XmlReaderUtil.getChildElements(element, "formats")) {
      list.add(parseFormat(e));
    }

    return list;
  }

  private List<String> parseNames() {
    List<String> list = new ArrayList<String>();

    for (Element e : XmlReaderUtil.getChildElements(element, "formats")) {
      list.add(e.getAttribute("field"));
    }

    return list;
  }

  private OutputFormat parseFormat(Element e) {
    if (e.getNodeName().equals("time-format")) {
      return parseTimeFormat(e);
    } else if (e.getNodeName().equals("string-format")) {
      return parseStringFormat(e);
    } else if (e.getNodeName().equals("package-format")) {
      return parsePackageFormat(e);
    }

    throw new IllegalArgumentException("unknown type '" + e.getNodeName() + "'");
  }

  private FormatFormat parseTimeFormat(Element e) {
    return new FormatFormat(new SimpleDateFormat(FormatFormat.SHORT_TIME));
  }

  private ToStringFormat parseStringFormat(Element e) {
    return new ToStringFormat();
  }

  private CompressedPackageFormat parsePackageFormat(Element e) {
    return new CompressedPackageFormat();
  }
}
