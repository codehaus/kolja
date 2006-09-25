package com.baulsupp.kolja.log.viewer.commands;

import java.beans.PropertyChangeEvent;

import jcurses.system.InputChar;

import com.baulsupp.curses.application.Command;
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

  public String getDescription() {
    return "t - Select Request";
  }
}
