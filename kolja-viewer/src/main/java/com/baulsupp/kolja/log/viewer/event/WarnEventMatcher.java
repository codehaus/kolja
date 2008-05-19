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
package com.baulsupp.kolja.log.viewer.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baulsupp.kolja.log.LogConstants;
import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.line.type.Priority;

public class WarnEventMatcher implements EventMatcher {
  private static final long serialVersionUID = -7577509944590961680L;

  @SuppressWarnings("unused")
  private static final Logger logger = LoggerFactory.getLogger(WarnEventMatcher.class);

  private String priorityField = LogConstants.PRIORITY;
  private String messageField = LogConstants.CONTENT;

  public WarnEventMatcher() {
  }

  public WarnEventMatcher(String priorityField, String messageField) {
    this.priorityField = priorityField;
    this.messageField = messageField;
  }

  public String getPriorityField() {
    return priorityField;
  }

  public void setPriorityField(String priorityField) {
    this.priorityField = priorityField;
  }

  public String getMessageField() {
    return messageField;
  }

  public void setMessageField(String messageField) {
    this.messageField = messageField;
  }

  public Event match(Line l) {
    if (isInteresting(l)) {
      return new Event(l, getDescription(l));
    }

    return null;
  }

  protected String getDescription(Line l) {
    return getLevel(l) + " " + getMessagePart(l);
  }

  protected String getMessagePart(Line l) {
    return (String) l.getValue(messageField);

    // if (s == null) {
    // return "";
    // } else if (s.length() > 33) {
    // return (s.substring(0, 30) + "...");
    // } else {
    // return s;
    // }
  }

  protected boolean isInteresting(Line l) {
    Priority level = getLevel(l);

    return level != null && (level.atleast(Priority.WARN));
  }

  protected Priority getLevel(Line l) {
    return (Priority) l.getValue(priorityField);
  }
}
