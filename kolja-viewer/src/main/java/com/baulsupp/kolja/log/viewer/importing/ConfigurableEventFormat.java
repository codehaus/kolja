package com.baulsupp.kolja.log.viewer.importing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.baulsupp.kolja.log.line.LineIndex;
import com.baulsupp.kolja.log.viewer.event.BasicEventDetector;
import com.baulsupp.kolja.log.viewer.event.EventDetector;
import com.baulsupp.kolja.log.viewer.event.EventList;
import com.baulsupp.kolja.log.viewer.event.EventMatcher;

public class ConfigurableEventFormat implements Serializable {
  private static final long serialVersionUID = -1122460693160139708L;

  private List<EventMatcher> eventMatchers = new ArrayList<EventMatcher>();

  public ConfigurableEventFormat() {
  }

  public List<EventMatcher> getEventMatchers() {
    return eventMatchers;
  }

  public void setEventMatchers(List<EventMatcher> eventMatchers) {
    this.eventMatchers = eventMatchers;
  }

  public boolean hasEvents() {
    return !eventMatchers.isEmpty();
  }

  public void addEventMatcher(EventMatcher eventMatcher) {
    eventMatchers.add(eventMatcher);
  }

  public EventList getEventList(LineIndex li) {
    return new EventList(li, new BasicEventDetector(eventMatchers));
  }

  public EventDetector getEventDetector() {
    return new BasicEventDetector(eventMatchers);
  }
}
