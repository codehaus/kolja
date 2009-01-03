package com.baulsupp.kolja.log.viewer.spring;

import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.viewer.highlight.*;
import com.baulsupp.kolja.log.viewer.event.EventMatcher;
import com.baulsupp.kolja.util.colours.ColourPair;
import org.w3c.dom.Element;
import org.springframework.util.ClassUtils;

public class HighlightParser {
  private Element element;
  private EventMatcher eventMatcher;

  public HighlightParser(Element element, EventMatcher eventMatcher) {
    this.element = element;
    this.eventMatcher = eventMatcher;
  }

  public HighlightList<Line> parseHighlights() {
    HighlightList<Line> results = new HighlightList<Line>();

    for (Element e : XmlReaderUtil.getChildElements(element, "highlights")) {
      results.addHighlight(parseHighlight(e));
    }

    return results;
  }

  private Highlight<Line> parseHighlight(Element e) {
    if (e.getNodeName().equals("priority-highlight")) {
      return parsePriorityHighlight(e);
    } else if (e.getNodeName().equals("failed-highlight")) {
      return new FailedHighlight();
    } else if (e.getNodeName().equals("regex-highlight")) {
      return parseRegexHighlight(e);
    } else if (e.getNodeName().equals("custom-highlight")) {
      return parseCustomHighlight(e);
    } else if (e.getNodeName().equals("event-highlight")) {
      return parseEventsHighlight();
    }

    throw new IllegalArgumentException("unknown type '" + e.getNodeName() + "'");
  }

  private RegexHighlight parseRegexHighlight(Element e) {
    RegexHighlight regexHighlight = new RegexHighlight();

    regexHighlight.setContentField(e.getAttribute("field"));
    regexHighlight.setPattern(XmlReaderUtil.parsePattern(XmlReaderUtil.getSingleElement(e, "pattern"), 0));
    regexHighlight.setColours(XmlReaderUtil.parseColours(XmlReaderUtil.getSingleElement(e, "colours")));

    return regexHighlight;
  }

  private PriorityHighlight parsePriorityHighlight(Element e) {
    PriorityHighlight priorityHighlight = new PriorityHighlight();

    priorityHighlight.setPriorityField(e.getAttribute("field"));

    return priorityHighlight;
  }

  private EventsHighlighter parseEventsHighlight() {
    return new EventsHighlighter(eventMatcher, ColourPair.GREEN_ON_BLACK);
  }

  @SuppressWarnings("unchecked")
  private Highlight<Line> parseCustomHighlight(Element e) {
    String className = e.getAttribute("class");

    try {
      Class c = ClassUtils.forName(className);

      return (Highlight<Line>) c.newInstance();
    } catch (RuntimeException ex) {
      throw ex;
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }
}
