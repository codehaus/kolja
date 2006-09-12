package com.baulsupp.less;

import com.baulsupp.curses.list.CursesListRenderer;
import com.baulsupp.curses.list.DisplayRow;
import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.viewer.renderer.Renderer;
import com.baulsupp.kolja.log.viewer.renderer.TextDisplayRow;

public class ViewRowRenderer implements CursesListRenderer<Line> {
  private Renderer<Line> renderer;

  public ViewRowRenderer(Renderer<Line> renderer) {
    this.renderer = renderer;
  }
  
  public DisplayRow getRow(Line o) {
    TextDisplayRow row = renderer.getRow(o);

    return new LessRow(row);
  }

  public Renderer<Line> getRenderer() {
    return renderer;
  }  
}
