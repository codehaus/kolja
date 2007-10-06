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
package com.baulsupp.curses.list;

import jcurses.system.InputChar;
import jcurses.system.Toolkit;
import jcurses.widgets.Window;

import com.baulsupp.kolja.util.colours.ColourPair;

public class BasicWindow extends Window {
  private InputHandler handler;

  public BasicWindow() {
    super(Toolkit.getScreenWidth(), Toolkit.getScreenHeight(), false, "");
    setShadow(false);
    setClosingChar(new InputChar(0));
    getRootPanel().setColors(ColorList.lookup(ColourPair.BLACK_ON_WHITE));
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
