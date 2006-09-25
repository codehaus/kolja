package com.baulsupp.kolja.log.viewer.commands;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import jcurses.system.InputChar;

import org.apache.log4j.Logger;

import com.baulsupp.curses.application.Command;
import com.baulsupp.curses.list.Util;
import com.baulsupp.kolja.log.viewer.event.Event;
import com.baulsupp.kolja.log.viewer.event.EventList;
import com.baulsupp.kolja.log.viewer.importing.LogFormat;
import com.baulsupp.less.EventDialog;
import com.baulsupp.less.Less;

public class SelectEventCommand implements Command<Less>, PropertyChangeListener {
  private static final Logger log = Logger.getLogger(SelectEventCommand.class);

  private EventList eventList;

  private LogFormat format;

  private Less less;

  public SelectEventCommand(LogFormat format, final Less less) {
    this.format = format;
    this.less = less;
    
    registerNewListener();
  }

  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals("lineIndex")) {
      registerNewListener();
    }
  }

  private void registerNewListener() {
    deregisterListener();
    
    eventList = format.getEventList(less.getLineIndex());
    less.getLineIndex().addLineListener(eventList);
  }

  private void deregisterListener() {
    if (eventList != null) {
      eventList.deregister();
      eventList = null;
    }
  }

  public boolean handle(Less less, InputChar input) {
    if (!Util.wasLetter(input, 'e')) {
      return false;
    }

    Event e = EventDialog.getValue(eventList);

    if (e != null) {
      log.info("selected " + e + " moving to " + e.getOffset());
      less.moveTo(e.getOffset());
    } else {
      log.info("no request selected");
    }

    return true;
  }

  public String getDescription() {
    return "e - Select Event";
  }
}
