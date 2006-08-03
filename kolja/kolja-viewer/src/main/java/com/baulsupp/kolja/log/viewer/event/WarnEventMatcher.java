package com.baulsupp.kolja.log.viewer.event;

import java.io.Serializable;

import org.apache.log4j.Logger;

import com.baulsupp.kolja.log.LogConstants;
import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.line.type.Priority;

public class WarnEventMatcher implements EventMatcher, Serializable {
  private static final long serialVersionUID = -7577509944590961680L;

  @SuppressWarnings("unused")
  private static final Logger logger = Logger.getLogger(WarnEventMatcher.class);

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

//    if (s == null) {
//      return "";
//    } else if (s.length() > 33) {
//      return (s.substring(0, 30) + "...");
//    } else {
//      return s;
//    }
  }

  protected boolean isInteresting(Line l) {
    Priority level = getLevel(l);

    return level != null && (level.atleast(Priority.WARN));
  }

  protected Priority getLevel(Line l) {
    return (Priority) l.getValue(priorityField);
  }
}
