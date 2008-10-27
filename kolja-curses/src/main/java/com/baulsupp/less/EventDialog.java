package com.baulsupp.less;

import java.util.ArrayList;

import jcurses.event.ActionEvent;
import jcurses.event.ActionListener;
import jcurses.system.InputChar;
import jcurses.widgets.BorderLayoutManager;
import jcurses.widgets.Button;
import jcurses.widgets.Dialog;
import jcurses.widgets.List;
import jcurses.widgets.WidgetsConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baulsupp.curses.list.Util;
import com.baulsupp.kolja.log.viewer.event.Event;
import com.baulsupp.kolja.log.viewer.event.EventList;

public class EventDialog extends Dialog {
  private static final long serialVersionUID = -2438075837387252360L;

  @SuppressWarnings("unused")
  private static final Logger logger = LoggerFactory.getLogger(Less.class);

  private List list;
  private EventList eventList;
  private java.util.List<Event> items = new ArrayList<Event>();

  private Event result = null;

  public EventDialog(EventList eventList) {
    super(80, 40, false, "Event List");
    
    this.setShadow(false);
    
    this.eventList = eventList;

    BorderLayoutManager manager = new BorderLayoutManager();
    getRootPanel().setLayoutManager(manager);

    list = new List(35, false) {

      protected boolean handleInput(InputChar inputChar) {
        boolean result = super.handleInput(inputChar);

        if (Util.wasReturn(inputChar)) {
          if (!items.isEmpty()) {
            int trackedItem = getTrackedItem();

            if (trackedItem >= 0) {
              select(trackedItem);
            }
          }

          EventDialog.this.saveAndClose();
        }

        return result;
      }
    };

    loadEvents(false);

    manager.addWidget(list, BorderLayoutManager.CENTER, WidgetsConstants.ALIGNMENT_CENTER,
        WidgetsConstants.ALIGNMENT_CENTER);

      Button ok = new Button("Load Fully");
      ok.addListener(new ActionListener() {
        public void actionPerformed(ActionEvent arg0) {
          loadEvents(true);
          EventDialog.this.repaint();
        }
      });

      manager.addWidget(ok, BorderLayoutManager.SOUTH, WidgetsConstants.ALIGNMENT_CENTER,
          WidgetsConstants.ALIGNMENT_CENTER);
      
      CompletionPanel completionPanel = new CompletionPanel(eventList);
      manager.addWidget(completionPanel, BorderLayoutManager.EAST, WidgetsConstants.ALIGNMENT_CENTER,
          WidgetsConstants.ALIGNMENT_CENTER);
      
  }

  private void loadEvents(boolean b) {
    list.clear();
    items.clear();

    if (b) {
      eventList.ensureAllKnown();
    }

    for (Event e : eventList.getEvents()) {
      list.add(e.toString());
      items.add(e);
    }
  }

  protected void onChar(InputChar arg0) {
    if (Util.wasReturn(arg0)) {
      saveAndClose();
    } else {
      super.onChar(arg0);
    }
  }

  public static Event getValue(EventList items) {
    EventDialog d = new EventDialog(items);

    d.show();

    return d.result;
  }

  private void saveAndClose() {
    int selection = list.getSelectedIndex();

    if (selection != -1) {
      result = items.get(selection);
    }
    close();
  }
}
