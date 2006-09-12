package com.baulsupp.kolja.log.viewer.search;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baulsupp.curses.list.ItemModel;
import com.baulsupp.kolja.log.line.Line;

public class BasicSearch implements Search {
  private Pattern pattern;
  
  private transient Matcher matcher;

  private ItemModel model;

  public BasicSearch(Pattern pattern) {
    this.pattern = pattern;
  }

  public void setModel(ItemModel model) {
    this.model = model;
  }

  public int previous(int i) {
    model.moveTo(i);

    while (model.hasPrevious()) {
      Line l = (Line) model.previous();
      if (matches(l)) {
        return l.getOffset();
      }
    }
    return -1;
  }

  public int next(int i) {
    model.moveTo(i + 1);

    while (model.hasNext()) {
      Line l = (Line) model.next();
      if (matches(l)) {
        return l.getOffset();
      }
    }
    return -1;
  }

  private boolean matches(Line l) {
    if (matcher == null) {
      matcher = pattern.matcher(l);
    } else {
      matcher.reset(l);
    }
    
    return matcher.find();
  }
}
