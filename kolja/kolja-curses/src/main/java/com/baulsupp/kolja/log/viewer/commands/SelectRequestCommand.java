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
package com.baulsupp.kolja.log.viewer.commands;

import java.beans.PropertyChangeEvent;
import java.util.Collection;
import java.util.Collections;

import jcurses.system.InputChar;

import com.baulsupp.curses.application.Command;
import com.baulsupp.curses.application.KeyBinding;
import com.baulsupp.curses.list.Util;
import com.baulsupp.kolja.log.viewer.importing.LogFormat;
import com.baulsupp.kolja.log.viewer.request.RequestHighlight;
import com.baulsupp.kolja.log.viewer.request.RequestLine;
import com.baulsupp.kolja.log.viewer.request.StandardRequestIndex;
import com.baulsupp.kolja.util.colours.ColourPair;
import com.baulsupp.less.Less;
import com.baulsupp.less.RequestDialog;

public class SelectRequestCommand implements Command<Less> {
  public RequestHighlight requestHighlight;

  private StandardRequestIndex requestIndex;

  private LogFormat format;

  private Less less;

  public SelectRequestCommand(LogFormat format, Less less) {
    this.format = format;
    this.less = less;

    registerNewListener();

    requestHighlight = new RequestHighlight(ColourPair.MAGENTA_ON_BLACK, requestIndex, null);
  }

  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals("lineIndex")) {
      registerNewListener();
    }
  }

  private void registerNewListener() {
    deregisterListener();

    requestIndex = format.getRequestIndex(less.getLineIndex());
    less.getLineIndex().addLineListener(requestIndex);
  }

  private void deregisterListener() {
    if (requestIndex != null) {
      requestIndex.deregister();
      requestIndex = null;
    }
  }

  public boolean handle(Less less, InputChar input) {
    if (!Util.wasLetter(input, 't')) {
      return false;
    }

    RequestLine l = RequestDialog.getValue(requestIndex, less.getOffset());

    if (l != null) {
      requestHighlight.setValue(l.getIdentifier());
      less.moveTo(l.getMinimumKnownOffset());
    }

    return true;
  }

  public Collection<KeyBinding> getDescription() {
    return Collections.singleton(new KeyBinding(new InputChar('t'), "Search", "Select Request"));
  }
}
