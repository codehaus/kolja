package com.baulsupp.curses.list;

import jcurses.system.InputChar;
import jcurses.system.Toolkit;
import jcurses.widgets.Window;

public class BasicWindow extends Window {
  private InputHandler handler;

  public BasicWindow() {
    super(Toolkit.getScreenWidth(), Toolkit.getScreenHeight(), false, "");
    setShadow(false);
    setClosingChar(new InputChar(0));
    getRootPanel().setColors(ColorList.blackOnWhite);
  }

  protected void handleInput(InputChar inp) {
    boolean handled = false;

    if (handler != null)
      handled = handler.handleInput(inp);

    if (!handled)
      super.handleInput(inp);
  }

  public InputHandler getHandler() {
    return handler;
  }

  public void setHandler(InputHandler handler) {
    this.handler = handler;
  }
}