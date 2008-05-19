package com.baulsupp.kolja.log.viewer.commands;

import java.util.Collection;
import java.util.Collections;

import jcurses.system.CharColor;
import jcurses.system.InputChar;
import jcurses.util.Rectangle;
import jcurses.widgets.IScrollable;
import jcurses.widgets.ScrollbarPainter;

import com.baulsupp.curses.application.Command;
import com.baulsupp.curses.application.KeyBinding;
import com.baulsupp.curses.list.ColorList;
import com.baulsupp.curses.list.PaintListener;
import com.baulsupp.curses.list.Util;
import com.baulsupp.less.Less;

public class ScrollbarCommand implements Command<Less>, IScrollable, PaintListener {
  private Less less = null;
  
  private ScrollbarPainter scrollbar;
  
  private boolean drawScrollbar = false;

  private CharSequence buffer;
  
  public ScrollbarCommand(Less less, CharSequence buffer) {
    this.less = less;
    this.buffer = buffer;
    
    this.scrollbar = new ScrollbarPainter(this);

    less.list.addPaintListener(this);
  }
  
  public boolean handle(Less less, InputChar input) {
    if (!Util.wasLetter(input, 's')) {
      return false;
    }
    
    toggleScrollbar(less);
    
    return true;
  }

  public Rectangle getBorderRectangle() {
    Rectangle r = less.list.getRectangle();
    
    // account for normal (unwanted) margin
    r.setY(-1);
    r.setHeight(r.getHeight() + 2);
    
    r.setX(r.getX() + r.getWidth() - 1);
    r.setWidth(1);
    return r;
  }

  public CharColor getScrollbarColors() {
    return ColorList.yellowOnBlack;
  }

  public float getHorizontalScrollbarOffset() {
    return 0;
  }

  public float getHorizontalScrollbarLength() {
    return 0;
  }

  public float getVerticalScrollbarOffset() {
    float offset = less.list.getOffset();
    float total = buffer.length();
    
    return offset / total;
  }

  public float getVerticalScrollbarLength() {
    float firstOffset = less.list.getOffset();
    float lastOffset = less.list.getLastOffset();
    float total = buffer.length();

    return (lastOffset - firstOffset) / total;
  }

  public CharColor getBorderColors() {
    return ColorList.blackOnWhite;
  }

  public void painted() {
    // TODO where should this be?
    less.uiLock.startBackgroundThreads();
    
    if (drawScrollbar) {
      scrollbar.paint();
    }
  }

  public void toggleScrollbar(Less less) {
    if (drawScrollbar) {
      drawScrollbar = false;
    } else {
      drawScrollbar = true;
    }

    less.list.doPaint();
  }

  public boolean hasHorizontalScrollbar() {
    return false;
  }

  public boolean hasVerticalScrollbar() {
    return true;
  }

  public Collection<KeyBinding> getDescription() {
    return Collections.singleton(new KeyBinding(new InputChar('s'), "View", "Toggle Scrollbar"));
  }
}
