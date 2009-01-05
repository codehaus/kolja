package com.baulsupp.kolja.log.viewer.commands;

import com.baulsupp.curses.application.Command;
import com.baulsupp.curses.application.KeyBinding;
import com.baulsupp.curses.list.Util;
import com.baulsupp.kolja.log.viewer.event.Event;
import com.baulsupp.kolja.log.viewer.event.EventList;
import com.baulsupp.less.EventDialog;
import com.baulsupp.less.Less;
import jcurses.system.InputChar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;

public class SelectEventCommand implements Command<Less> {
  private static final Logger log = LoggerFactory.getLogger(SelectEventCommand.class);

  private EventList eventList;

  public SelectEventCommand(EventList eventList) {
    this.eventList = eventList;
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

  public Collection<KeyBinding> getDescription() {
    return Collections.singleton(new KeyBinding(new InputChar('e'), "Search", "Select Event"));
  }
}
