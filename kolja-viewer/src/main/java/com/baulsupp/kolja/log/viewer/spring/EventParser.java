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

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.w3c.dom.Element;

import com.baulsupp.kolja.log.viewer.event.EventMatcher;
import com.baulsupp.kolja.log.viewer.event.WarnEventMatcher;
import com.baulsupp.kolja.log.viewer.importing.ConfigurableEventFormat;

/**
 * Event Parser
 * 
 * @author Yuri Schimke
 */
public class EventParser {
  private Element element;

  public EventParser(Element element) {
    Assert.notNull(element);

    this.element = element;
  }

  public ConfigurableEventFormat parse() {
    ConfigurableEventFormat eventFormat = new ConfigurableEventFormat();

    List<EventMatcher> matchers = new ArrayList<EventMatcher>();

    for (Element e : XmlReaderUtil.elements(element.getChildNodes())) {
      matchers.add(parseMatcher(e));
    }

    eventFormat.setEventMatchers(matchers);

    return eventFormat;
  }

  private EventMatcher parseMatcher(Element e) {
    if (e.getNodeName().equals("priority-events")) {
      return parsePriorityEvents(e);
    } else if (e.getNodeName().equals("custom-events")) {
      return parseCustomEvents(e);
    }

    throw new IllegalArgumentException("unknown type '" + e.getNodeName() + "'");
  }

  private WarnEventMatcher parsePriorityEvents(Element e) {
    return new WarnEventMatcher();
  }

  @SuppressWarnings("unchecked")
  private EventMatcher parseCustomEvents(Element e) {
    String className = e.getAttribute("class");

    try {
      Class c = ClassUtils.forName(className);

      return (EventMatcher) c.newInstance();
    } catch (RuntimeException ex) {
      throw ex;
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }
}
