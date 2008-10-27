package com.baulsupp.less;

import com.baulsupp.curses.list.ItemModel;
import com.baulsupp.curses.list.ItemModelListener;
import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.line.LineIterator;

public class LineIndexItemModel implements ItemModel<Line> {
  private LineIterator li;

  private Line lastLine = null;

  public LineIndexItemModel(LineIterator li) {
    this.li = li;
  }

  public void moveTo(int position) {
    lastLine = null;
    li.moveTo(position);
  }

  public boolean hasNext() {
    return li.hasNext();
  }

  public Line next() {
    lastLine = li.next();
    return lastLine;
  }

  public boolean hasPrevious() {
    return li.hasPrevious();
  }

  public Line previous() {
    lastLine = li.previous();
    return lastLine;
  }

  public void moveToEnd() {
    lastLine = null;
    li.moveToEnd();
  }

  public void moveToStart() {
    lastLine = null;
    li.moveToStart();
  }

  // TODO needs to become position and remain persistant
  public int lastPosition() {
    return (lastLine != null) ? lastLine.getIntOffset() : 0;
  }

  public void addItemModelListener(ItemModelListener listener) {
  }

  public void removeItemModelListener(ItemModelListener listener) {
  }
}
