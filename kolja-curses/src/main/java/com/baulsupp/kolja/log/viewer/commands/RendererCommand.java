package com.baulsupp.kolja.log.viewer.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import jcurses.system.InputChar;
import jcurses.system.Toolkit;

import com.baulsupp.curses.application.Command;
import com.baulsupp.curses.application.KeyBinding;
import com.baulsupp.curses.list.CursesListRenderer;
import com.baulsupp.curses.list.TextRenderer;
import com.baulsupp.curses.list.Util;
import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.viewer.importing.LogFormat;
import com.baulsupp.kolja.log.viewer.renderer.FieldRenderer;
import com.baulsupp.kolja.log.viewer.renderer.Renderer;
import com.baulsupp.kolja.log.viewer.renderer.Wrap;
import com.baulsupp.less.Less;
import com.baulsupp.less.ViewRowRenderer;

public class RendererCommand implements Command<Less> {
  private java.util.List<CursesListRenderer<Line>> renderers;

  public RendererCommand(Less less, LogFormat format) {

    renderers = new ArrayList<CursesListRenderer<Line>>();

    Renderer<Line> gridNonWrapped = format.getLineRenderer();
    gridNonWrapped.setWidth(Toolkit.getScreenWidth());

    ViewRowRenderer viewRenderer = new ViewRowRenderer(gridNonWrapped);
    renderers.add(viewRenderer);

    if (gridNonWrapped instanceof FieldRenderer) {
      ((FieldRenderer) gridNonWrapped).setWrappingMode(Wrap.NONE);

      Renderer<Line> gridWrapped = format.getLineRenderer();
      gridWrapped.setWidth(Toolkit.getScreenWidth());
      ((FieldRenderer) gridWrapped).setWrappingMode(Wrap.LAST_COLUMN);

      ((FieldRenderer) gridWrapped).addHighlight(less.getExtraHighlights());
      ((FieldRenderer) gridNonWrapped).addHighlight(less.getExtraHighlights());

      viewRenderer = new ViewRowRenderer(gridWrapped);
      renderers.add(viewRenderer);
    }

    TextRenderer<Line> plain = new TextRenderer<Line>();
    plain.setWrapping(Toolkit.getScreenWidth());
    renderers.add(plain);

    TextRenderer<Line> plainUnwrapped = new TextRenderer<Line>();
    plainUnwrapped.setWrapping(TextRenderer.NO_WRAP);
    renderers.add(plainUnwrapped);

    rotateRenderer(less);
  }

  public boolean handle(Less less, InputChar input) {
    if (!Util.wasLetter(input, 'r')) {
      return false;
    }

    rotateRenderer(less);

    return true;
  }

  public void rotateRenderer(Less less) {
    CursesListRenderer<Line> nextRenderer = getNextRenderer();

    if (nextRenderer instanceof ViewRowRenderer) {
      ViewRowRenderer viewRenderer = (ViewRowRenderer) nextRenderer;

      if (viewRenderer.getRenderer() instanceof FieldRenderer) {
        FieldRenderer fieldRenderer = (FieldRenderer) viewRenderer.getRenderer();
        less.list.setSidewaysSteps(fieldRenderer.getWidths().getSteps());
      }
    } else {
      less.list.setSidewaysSteps(null);
    }

    less.list.setSidewaysMove(10);

    less.setRenderer(nextRenderer);
  }

  CursesListRenderer<Line> getNextRenderer() {
    CursesListRenderer<Line> r = (CursesListRenderer<Line>) renderers.remove(0);
    renderers.add(r);

    return r;
  }

  public Collection<KeyBinding> getDescription() {
    return Collections.singleton(new KeyBinding(new InputChar('r'), "View", "Next Renderer"));
  }
}
