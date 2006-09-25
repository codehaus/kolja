package com.baulsupp.kolja.log.viewer.commands;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.regex.Pattern;

import jcurses.system.InputChar;

import com.baulsupp.curses.application.Command;
import com.baulsupp.curses.list.TextDialog;
import com.baulsupp.curses.list.Util;
import com.baulsupp.kolja.log.line.BasicLineIterator;
import com.baulsupp.kolja.log.viewer.highlight.BasicSearchHighlight;
import com.baulsupp.kolja.log.viewer.search.BasicSearch;
import com.baulsupp.kolja.log.viewer.search.Search;
import com.baulsupp.kolja.util.colours.ColourPair;
import com.baulsupp.less.Less;
import com.baulsupp.less.LineIndexItemModel;

public class SearchCommand implements Command<Less>, PropertyChangeListener {
  private Search search;

  private BasicSearchHighlight basicSearchHighlight;
  
  public SearchCommand(Less less) {
    basicSearchHighlight = new BasicSearchHighlight(ColourPair.RED_ON_BLACK);
    less.addFieldHighlighter(basicSearchHighlight);
  }
  
  public boolean handle(Less less, InputChar input) {
    if (!Util.wasLetter(input, '/', 'n', 'p')) {
      return false;
    }

    if (Util.wasLetter(input, '/'))
      newSearch(less);
    else if (Util.wasLetter(input, 'n'))
      nextSearchResult(less);
    else if (Util.wasLetter(input, 'p'))
      previousSearchResult(less);
    
    return true;
  }
  
  public void newSearch(Less less) {
    String a = TextDialog.getValue("Search");

    Pattern pattern = Pattern.compile(a);

    search = new BasicSearch(pattern);

    search.setModel(new LineIndexItemModel(new BasicLineIterator(less.getLineIndex())));

    basicSearchHighlight.setPattern(pattern);

    nextSearchResult(less);
  }

  private boolean hasCurrentSearch() {
    return search != null;
  }

  public void nextSearchResult(Less less) {
    if (hasCurrentSearch()) {
      int pos = search.next(less.getOffset());

      if (pos != -1) {
        less.moveTo(pos);
      }
    }
  }

  public void previousSearchResult(Less less) {
    if (hasCurrentSearch()) {
      int pos = search.previous(less.getOffset());

      if (pos != -1) {
        less.moveTo(pos);
      }
    }
  }


  public String getDescription() {
    return "s - Toggle Scrollbar";
  }

  public void propertyChange(PropertyChangeEvent evt) {
    throw new UnsupportedOperationException();
//    search.setModel(new LineIndexItemModel(new BasicLineIterator(currentIndex)));
  }
}
