package com.baulsupp.kolja.log.viewer.commands;

import java.util.ArrayList;
import java.util.List;

import jcurses.system.InputChar;

import com.baulsupp.curses.application.Command;
import com.baulsupp.curses.list.Util;
import com.baulsupp.kolja.log.filter.Filter;
import com.baulsupp.kolja.log.filter.FilteredLineIndex;
import com.baulsupp.kolja.log.filter.PriorityFilter;
import com.baulsupp.kolja.log.line.LineIndex;
import com.baulsupp.kolja.log.viewer.importing.LogFormat;
import com.baulsupp.less.Less;

public class ModelsCommand implements Command<Less> {
  private List<LineIndex> models = new ArrayList<LineIndex>();
  
  public ModelsCommand(LogFormat format, CharSequence buffer, Less less) {
    LineIndex li = format.getLineIndex(buffer);
    models.add(li);

    // TODO move filtering to config and allow choices
    Filter filter = new PriorityFilter();
    LineIndex filteredIndex = new FilteredLineIndex(li, filter);
    models.add(filteredIndex);

    less.setLineIndex(getNextLineModel());
  }

  LineIndex getNextLineModel() {
    LineIndex li = (LineIndex) models.remove(0);
    models.add(li);

    return li;
  }

  public boolean handle(Less less, InputChar input) {
    if (!Util.wasLetter(input, 'f')) {
      return false;
    }

    less.setLineIndex(getNextLineModel());
    
    return true;
  }

  public String getDescription() {
    return "f - change filter";
  }
}
