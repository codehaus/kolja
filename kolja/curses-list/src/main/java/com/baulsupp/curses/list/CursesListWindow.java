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

import java.beans.PropertyChangeListener;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.Lock;

import jcurses.system.InputChar;
import jcurses.util.Message;
import jcurses.widgets.BorderLayoutManager;
import jcurses.widgets.WidgetsConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baulsupp.curses.application.Command;
import com.baulsupp.curses.application.CommandList;
import com.baulsupp.curses.application.HelpCommand;
import com.baulsupp.curses.application.ListMovementCommand;
import com.baulsupp.curses.application.QuitCommand;
import com.baulsupp.curses.application.util.ResponsiveLock;
import com.baulsupp.kolja.util.event.PropertyChangeListenerList;

public class CursesListWindow<T, S extends CursesListWindow<T, S>> implements InputHandler {
  private static final Logger log = LoggerFactory.getLogger("com.baulsupp.list.CursesListWindow");

  // TODO reduce visibility
  public CursesList<T> list = new CursesList<T>();

  protected LineNumberIndex lineNumbers = null;

  protected BorderLayoutManager manager;

  protected BasicWindow window;

  protected CommandList commands = new CommandList();

  protected PropertyChangeListenerList listeners = new PropertyChangeListenerList();

  // TODO make private
  public ResponsiveLock uiLock = new ResponsiveLock();
  private Lock backgroundLock = uiLock.createBackgroundLock(1500);

  public void setLineNumbers(LineNumberIndex lineNumbers) {
    this.lineNumbers = lineNumbers;
  }

  public CommandList getCommands() {
    return commands;
  }

  public void createDefaultCommands() {
    addCommand(new HelpCommand());
    addCommand(new QuitCommand());
    addCommand(new ListMovementCommand());
  }

  public void addCommand(Command command) {
    commands.add(command);

    if (command instanceof PropertyChangeListener) {
      addPropertyChangeListener((PropertyChangeListener) command);
    }
  }

  public void addPropertyChangeListener(PropertyChangeListener listener) {
    listeners.add(listener);
  }

  public boolean handleInput(InputChar inp) {
    try {
      uiLock.lock();

      boolean handled = commands.run(inp, this);

      if (handled) {
        return true;
      }

      // if (list.handleInput(inp)) {
      // return true;
      // } else {
      log.debug("unknown keystroke");
      log.debug("special " + inp.isSpecialCode());
      log.debug("code " + inp.getCode());
      if (!inp.isSpecialCode())
        log.debug("char " + inp.getCharacter());
      return false;
      // }
    } catch (Exception e) {
      log.error("error", e);
      showError(e);
      return true;
    } finally {
      uiLock.unlock();
    }
  }

  protected void showError(Exception e) {
    new Message("Error", String.valueOf(e.getMessage()), "ok").show();
  }

  public <R> R performWithLock(Callable<R> runnable) throws Exception {
    backgroundLock.lock();

    try {
      return runnable.call();
    } finally {
      backgroundLock.unlock();
    }
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
    manager.addWidget(list, BorderLayoutManager.CENTER, WidgetsConstants.ALIGNMENT_CENTER, WidgetsConstants.ALIGNMENT_CENTER);
  }

  public void setRenderer(CursesListRenderer<T> renderer) {
    CursesListRenderer oldRenderer = list.getRenderer();

    list.setRenderer(renderer);

    listeners.firePropertyChangeEvent(this, "renderer", oldRenderer, renderer);
  }

  public void setModel(ItemModel<T> model) {
    ItemModel oldModel = list.getModel();

    list.setModel(model);

    listeners.firePropertyChangeEvent(this, "model", oldModel, model);
  }
}
