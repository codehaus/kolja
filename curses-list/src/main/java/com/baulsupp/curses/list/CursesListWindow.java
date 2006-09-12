package com.baulsupp.curses.list;

import jcurses.system.InputChar;
import jcurses.system.Toolkit;
import jcurses.util.Message;
import jcurses.widgets.BorderLayoutManager;
import jcurses.widgets.WidgetsConstants;

import org.apache.log4j.Logger;

public class CursesListWindow<T> implements InputHandler {
  private static final Logger log = Logger.getLogger("com.baulsupp.list.CursesListWindow");

  protected CursesList<T> list = new CursesList<T>();

  protected LineNumberIndex lineNumbers = null;

  protected BorderLayoutManager manager;

  protected BasicWindow window;

  public void setLineNumbers(LineNumberIndex lineNumbers) {
    this.lineNumbers = lineNumbers;
  }

  public boolean handleInput(InputChar inp) {
    try {
      if (Util.was_q(inp)) {
        shutdown();
      } else if (usesLineNumbers() && Util.wasLetter(inp, 'g')) {
        gotoLine();
      } else if (list.handleInput(inp)) {
        return true;
      } else {
        log.debug("unknown keystroke");
        log.debug("special " + inp.isSpecialCode());
        log.debug("code " + inp.getCode());
        if (!inp.isSpecialCode())
          log.debug("char " + inp.getCharacter());
        return false;
      }
    } catch (Exception e) {
      log.error("error", e);
    }

    return true;
  }

  private boolean usesLineNumbers() {
    return lineNumbers != null;
  }

  private void gotoLine() {
    String a = TextDialog.getValue("Goto Line");

    if (a != null) {
      try {
        if (a.equals("")) {
          list.end();
        } else {
          int line = Integer.parseInt(a);
          list.moveTo(lineNumbers.offset(line - 1));
        }
      } catch (NumberFormatException nfe) {
        showMessage(nfe.toString());
      }
    }
  }

  protected void showMessage(String message) {
    new Message("", message, "ok").show();
  }

  protected void shutdown() {
    Toolkit.clearScreen(ColorList.blackOnWhite);
    Toolkit.shutdown();
    System.exit(-1);
  }

  public void show() {
    createWindow();

    window.show();
  }

  protected void createWindow() {
    window = new BasicWindow();
    manager = new BorderLayoutManager();
    window.getRootPanel().setLayoutManager(manager);
    list.setColors(ColorList.whiteOnBlack);
    window.getRootPanel().setPanelColors(ColorList.whiteOnBlack);

    window.setHandler(this);
    manager.addWidget(list, BorderLayoutManager.CENTER, WidgetsConstants.ALIGNMENT_CENTER,
        WidgetsConstants.ALIGNMENT_CENTER);
  }
}
