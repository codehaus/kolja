package com.baulsupp.less;

import java.util.ArrayList;
import java.util.List;

import jcurses.system.CharColor;
import jcurses.system.Toolkit;
import jcurses.util.Rectangle;
import jcurses.widgets.Widget;

import com.baulsupp.kolja.log.line.CompletionStatus;
import com.baulsupp.kolja.log.util.IntRange;

public class CompletionPanel extends Widget {
  private CompletionStatus completionStatus;

  private static final CharColor FULL = new CharColor(CharColor.BLACK, CharColor.BLACK);
  private static final CharColor TWO_THIRD = new CharColor(CharColor.BLUE, CharColor.BLACK);
  private static final CharColor THIRD = new CharColor(CharColor.CYAN, CharColor.BLACK);
  private static final CharColor NONE = new CharColor(CharColor.WHITE, CharColor.BLACK);

  public CompletionPanel(CompletionStatus completionStatus) {
    this.completionStatus = completionStatus;
  }
  
  @Override
  protected Rectangle getPreferredSize() {
    return new Rectangle(1, 100);
  }

  @Override
  protected void doPaint() {
    List<IntRange> intRanges = createIntRanges();
    
    int pos = 0;
    for (IntRange range : intRanges) {
      CharColor color = lookupColor(range);
      Toolkit.drawHorizontalLine(getAbsoluteX(), getAbsoluteY() + pos, getAbsoluteX() + getSize().getWidth(), color);
      pos++;
    }
  }

  private CharColor lookupColor(IntRange range) {
    double percent = completionStatus.getCompletionPercentage(range);
    
    if (percent < 0.25) {
      return NONE; 
    } else if (percent < 0.5) {
      return THIRD;
    } else if (percent < 0.75) {
      return TWO_THIRD;
    } else {
      return FULL;
    }
  }

  private List<IntRange> createIntRanges() {
    int height = getSize().getHeight();
    int length = completionStatus.getLength();
    
    List<IntRange> result = new ArrayList<IntRange>();
    
    int each = length / height;
    int leftover = length % height;
    
    int pos = 0;
    
    for (int i = 0; i < height; i++) {
      int newPos = pos + each + (i < leftover ? 1 : 0);
      result.add(new IntRange(pos, newPos));
      pos = newPos;
    }
    
    return result;
  }

  @Override
  protected void doRepaint() {
    doPaint();
  }
}
