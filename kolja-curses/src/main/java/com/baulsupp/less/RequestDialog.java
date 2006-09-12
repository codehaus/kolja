package com.baulsupp.less;

import java.util.ArrayList;

import jcurses.event.ActionEvent;
import jcurses.event.ActionListener;
import jcurses.system.InputChar;
import jcurses.util.Rectangle;
import jcurses.widgets.BorderLayoutManager;
import jcurses.widgets.Button;
import jcurses.widgets.Dialog;
import jcurses.widgets.List;
import jcurses.widgets.WidgetsConstants;

import org.apache.log4j.Logger;

import com.baulsupp.curses.list.Util;
import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.viewer.request.RequestIndex;
import com.baulsupp.kolja.log.viewer.request.RequestLine;

public class RequestDialog extends Dialog {
  private static final long serialVersionUID = -2438075837387252360L;

  @SuppressWarnings("unused")
  private static final Logger logger = Logger.getLogger(Less.class);

  private List list;
  private RequestIndex eventList;
  private java.util.List<RequestLine> items = new ArrayList<RequestLine>();

  private RequestLine result = null;

  public RequestDialog(RequestIndex eventList) {
    super(80, 40, false, "Event List");

    this.eventList = eventList;

    BorderLayoutManager manager = new BorderLayoutManager();
    getRootPanel().setLayoutManager(manager);

    list = new List(35, false) {
      {
        setSize(new Rectangle(10, 10));
      }

      protected boolean handleInput(InputChar inputChar) {
        boolean result = super.handleInput(inputChar);

        if (Util.wasReturn(inputChar)) {
          select(getTrackedItem());
          RequestDialog.this.saveAndClose();
        }

        return result;
      }
    };
    list.setSelectable(false);

    loadEvents(false);

    manager.addWidget(list, BorderLayoutManager.CENTER, WidgetsConstants.ALIGNMENT_CENTER,
        WidgetsConstants.ALIGNMENT_CENTER);

      Button ok = new Button("Load Fully");
      ok.addListener(new ActionListener() {
        public void actionPerformed(ActionEvent arg0) {
          loadEvents(true);
          RequestDialog.this.repaint();
        }
      });

      manager.addWidget(ok, BorderLayoutManager.SOUTH, WidgetsConstants.ALIGNMENT_CENTER,
          WidgetsConstants.ALIGNMENT_CENTER);
      
      CompletionPanel completionPanel = new CompletionPanel(this.eventList);
      manager.addWidget(completionPanel, BorderLayoutManager.EAST, WidgetsConstants.ALIGNMENT_CENTER,
          WidgetsConstants.ALIGNMENT_CENTER);
  }

  private void loadEvents(boolean loadAll) {
    list.clear();
    items.clear();

    if (loadAll) {
      eventList.ensureAllKnown();
    }

    for (Line e : eventList.getKnown()) {
      RequestLine l = (RequestLine) e;
      list.add(l.getStatus());
      items.add(l);
    }
  }

  protected void onChar(InputChar arg0) {
    if (Util.wasReturn(arg0)) {
      saveAndClose();
    } else {
      super.onChar(arg0);
    }
  }

  public static RequestLine getValue(RequestIndex items, int offset) {
    RequestDialog d = new RequestDialog(items);
    
    // TODO move to correct position
    d.setPosition(offset);
    
    d.show();

    return d.result;
  }

  private void setPosition(int offset) {
    int pos = 0;
    
    for (RequestLine r : items) {
      if (r.isRelevantForOffset(offset)) {
        list.setTrackedItem(pos);
        
        break;
      }
      
      pos++;
    }
  }

  private void saveAndClose() {
    int selection = list.getSelectedIndex();

    if (selection != -1) {
      result = items.get(selection);
    }
    close();
  }
}
