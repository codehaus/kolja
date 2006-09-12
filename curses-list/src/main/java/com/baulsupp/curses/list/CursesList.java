package com.baulsupp.curses.list;

import java.util.ArrayList;
import java.util.List;

import jcurses.system.InputChar;
import jcurses.system.Toolkit;
import jcurses.util.Rectangle;
import jcurses.widgets.Widget;

import org.apache.log4j.Logger;

public class CursesList<T extends Object> extends Widget implements ItemModelListener {
  private final static Logger logger = Logger.getLogger(CursesList.class);

  private Rectangle preferredSize = new Rectangle(1000, 1000);

  private int hiddenLinesTop = 0;

  private int hiddenLinesBottom = 0;

  private OffsetList offsetList = new OffsetList();

  private CursesListRenderer<T> renderer = null;

  private SmartLineIterator<T> model = null;

  private boolean enoughFound = false;

  private List<ListMovementListener> listeners = new ArrayList<ListMovementListener>();

  private boolean tailMode;

  private int xPos = 0;
  
  private int[] sidewaysSteps = null;
  private int sidewaysMove = 1;

  private List<PaintListener> paintListeners = new ArrayList<PaintListener>();

  protected Rectangle getPreferredSize() {
    return preferredSize;
  }

  public synchronized void doPaint() {
    clearBox();

    TextPanel panel = buildPanel();

    if (offsetList.isEmpty()) {
      loadRowsDown(true);
      hiddenLinesBottom = getUsedRows() - panel.getHeight();
    }

    int y = 0;
    int currentRow = 0;
    for (DisplayRow row : offsetList) {
      int rowHeight = row.getHeight();

      int fromRow = 0;
      if (currentRow == 0) {
        fromRow = hiddenLinesTop;
      }

      int rowsShown = rowHeight - fromRow;
      if (currentRow == offsetList.rowCount() - 1 && hiddenLinesBottom > 0) {
        rowsShown -= hiddenLinesBottom;
      }

      TextPanel rowPanel = panel.row(y, rowsShown);
      row.paint(rowPanel, fromRow, rowsShown, xPos);
      y += rowsShown;

      currentRow++;
    }

    enoughFound = y == panel.getHeight();
    
    triggerPaintListener();
  }

  private void triggerPaintListener() {
    for (PaintListener paintListener : paintListeners) {
      paintListener.painted();
    }
  }

  private TextPanel buildPanel() {
    int xOffset = this.getX();
    int yOffset = this.getY();
    
    int width = getSize().getWidth();
    int height = getSize().getHeight();
    
    TextPanel panel = new TextPanel(xOffset, yOffset, width, height);
    
    return panel;
  }

  public int[] getSidewaysSteps() {
    return sidewaysSteps;
  }

  public void setSidewaysSteps(int[] sidewaysSteps) {
    this.sidewaysSteps = sidewaysSteps;
  }
  
  public int getSidewaysMove() {
    return sidewaysMove;
  }

  public void setSidewaysMove(int sidewaysMove) {
    this.sidewaysMove = sidewaysMove;
  }

  protected int getScreenHeight() {
    return this.getRectangle().getHeight();
  }

  protected int getScreenWidth() {
    return this.getRectangle().getWidth();
  }

  protected void doRepaint() {
    doPaint();
  }

  private void clearBox() {
    Toolkit.drawRectangle(getRectangle(), getColors());
  }

  protected void setSize(Rectangle size) {
    reset();
    super.setSize(size);
  }

  private DisplayRow getDisplayRow(T item) {
    return renderer.getRow(item);
  }

  public ItemModel getModel() {
    return model;
  }

  public int getOffset() {
    return offsetList.rowCount() != 0 ? offsetList.getMinRowOffset() : 0;
  }
  
  public int getLastOffset() {
    return offsetList.rowCount() != 0 ? offsetList.getMaxRowOffset() : 0;
  }

  public synchronized void setModel(ItemModel<T> model) {
    int offset = offsetList.rowCount() != 0 ? offsetList.getMinRowOffset() : 0;
    reset();
    if (this.model != null)
      this.model.removeItemModelListener(this);
    this.model = new SmartLineIterator<T>(model);
    this.model.moveTo(offset);
    if (renderer != null)
      paint();
    this.model.addItemModelListener(this);
  }

  public CursesListRenderer getRenderer() {
    return renderer;
  }

  public synchronized void setRenderer(CursesListRenderer<T> renderer) {
    int offset = offsetList.rowCount() != 0 ? offsetList.getMinRowOffset() : 0;
    reset();
    this.renderer = renderer;
    this.model.moveTo(offset);
    if (model != null)
      paint();
  }

  public synchronized void reset() {
    // TODO get back as close as possible
    // model may have changed

    hiddenLinesTop = 0;
    hiddenLinesBottom = 0;
    offsetList.clear();
  }

  private DisplayRow loadNextRow() {
    DisplayRow r = getDisplayRow(model.next());
    offsetList.addLast(r, model.lastPosition());
    return r;
  }

  private DisplayRow loadPreviousRow() {
    DisplayRow r = getDisplayRow(model.previous());

    offsetList.addFirst(r, model.lastPosition());
    return r;
  }

  private void loadRowsDown(boolean tryUp) {
    int screenHeight = getScreenHeight();

    int rowsUsed = getUsedRows();
    do {
      if (!model.hasNext())
        return;

      DisplayRow r = loadNextRow();
      rowsUsed += r.getHeight();
    } while (rowsUsed < screenHeight && model.hasNext());

    if (tryUp && rowsUsed < screenHeight) {
      model.moveTo(offsetList.getMinRowOffset());

      if (model.hasPrevious()) {
        loadRowsUp(false);
      }
    }
  }

  private void loadRowsUp(boolean tryDown) {
    int screenHeight = getScreenHeight();

    int rowsUsed = getUsedRows();
    do {
      if (!model.hasPrevious())
        return;

      DisplayRow r = loadPreviousRow();
      rowsUsed += r.getHeight();
    } while (rowsUsed < screenHeight && model.hasPrevious());

    if (tryDown && rowsUsed < screenHeight) {
      model.moveTo(offsetList.getMaxRowOffset() + 1);

      if (model.hasNext()) {
        loadRowsDown(false);
      }
    }
  }

  private int getUsedRows() {
    int total = 0;

    for (DisplayRow r : offsetList) {
      total += r.getHeight();
    }

    return total;
  }

  public synchronized void up() {
    boolean moreInternal = hiddenLinesTop > 0;

    if (moreInternal) {
      hiddenLinesTop--;

      stepLast();

      paint();
    } else {
      if (!model.atTop()) {
        model.moveTo(offsetList.getMinRowOffset());
      }

      if (!model.hasPrevious())
        return;

      DisplayRow newRow = loadPreviousRow();

      int newHeight = newRow.getHeight();
      hiddenLinesTop = newHeight - 1;

      stepLast();

      paint();
    }

    triggerMovementListener();
  }

  private void stepLast() {
    DisplayRow last = offsetList.getLastRow();
    int lastRowSize = last.getHeight();

    boolean keepLastRow = hiddenLinesBottom + 1 < lastRowSize;

    if (keepLastRow) {
      hiddenLinesBottom++;
    } else {
      hiddenLinesBottom = 0;
      offsetList.removeLast();
    }
  }

  public synchronized void down() {
    boolean moreInternal = hiddenLinesBottom > 0;

    if (moreInternal) {
      hiddenLinesBottom--;
      stepFirst();

      paint();
    } else {
      if (!model.atBottom()) {
        model.moveTo(offsetList.getMaxRowOffset() + 1);
      }

      if (!model.hasNext())
        return;

      DisplayRow row = loadNextRow();

      hiddenLinesBottom = row.getHeight() - 1;

      // added to see if it fixes
      if (enoughFound) {
        stepFirst();
      }

      paint();
    }

    triggerMovementListener();
  }

  private void stepFirst() {
    DisplayRow firstRow = offsetList.getFirstRow();
    int firstRowSize = firstRow.getHeight();

    boolean keepFirstRow = hiddenLinesTop + 1 < firstRowSize;

    if (keepFirstRow) {
      hiddenLinesTop++;
    } else {
      hiddenLinesTop = 0;
      offsetList.removeFirst();
    }
  }

  public synchronized void moveTo(int offset) {
    // log.debug("moveTo");

    reset();
    this.model.moveTo(offset);
    paint();

    triggerMovementListener();
  }

  public synchronized void pageDown() {
    // log.debug("pageUp");

    model.moveTo(offsetList.getMaxRowOffset());

    hiddenLinesTop = 0;

    offsetList.clear();

    loadRowsDown(true);

    hiddenLinesBottom = Math.max(0, getUsedRows() - getScreenHeight());

    paint();

    triggerMovementListener();
  }

  public synchronized void pageUp() {
    // log.debug("pageUp");

    model.moveTo(offsetList.getMinRowOffset() + 1);

    hiddenLinesBottom = 0;

    offsetList.clear();

    loadRowsUp(true);

    hiddenLinesTop = Math.max(0, getUsedRows() - getScreenHeight());

    paint();

    triggerMovementListener();
  }

  // TODO generalise this to a) move to model position B) reset b) load up/down
  public synchronized void end() {
    model.moveToEnd();

    hiddenLinesBottom = 0;
    offsetList.clear();

    loadRowsUp(true);

    hiddenLinesTop = Math.max(0, getUsedRows() - getScreenHeight());

    paint();

    triggerMovementListener();
  }

  public synchronized void home() {
    // log.debug("home");

    model.moveToStart();

    hiddenLinesBottom = 0;
    offsetList.clear();

    loadRowsDown(true);

    hiddenLinesBottom = Math.max(0, getUsedRows() - getScreenHeight());

    paint();

    triggerMovementListener();
  }
  
  private void left() {
    int normalLeft = xPos - sidewaysMove;
    
    if (xPos > 0) {
      if (sidewaysSteps != null) {
        xPos = nextLowest(xPos);
        xPos = Math.max(xPos, normalLeft);
      } else {
        xPos = Math.min(0, normalLeft);
      }
    }

    paint();
  }

  // TODO implement utility method
  private int nextLowest(int pos) {
    int maxLess = 0;
    
    for (int i : sidewaysSteps) {
      if (i < pos) {
        maxLess = Math.max(maxLess, i);
      }
    }
    
    return maxLess;
  }

  private void right() { 
    int normalRight = xPos + sidewaysMove;
    
    if (sidewaysSteps != null) {
      int stepRight = nextHighest(xPos);
      
      if (stepRight == xPos) {
        xPos = normalRight;
      } else {
        xPos = stepRight;
      }
    } else {
      xPos = normalRight;
    }
    
    paint();
  }

  // TODO implement utility method
  private int nextHighest(int pos) {
    int minHigher = Integer.MAX_VALUE;
    
    for (int i : sidewaysSteps) {
      if (i > pos) {
        minHigher = Math.min(minHigher, i);
      }
    }
    
    return minHigher == Integer.MAX_VALUE ? pos : minHigher;
  }

  public synchronized void refresh() {
    model.moveTo(offsetList.getMinRowOffset());

    hiddenLinesBottom = 0;
    offsetList.clear();

    loadRowsDown(true);

    hiddenLinesBottom = Math.max(0, getUsedRows() - getScreenHeight());

    paint();

    triggerMovementListener();
  }

  public synchronized void fileGrew() {
    logger.info("fileGrew");
    if (tailMode) {
      end();
    } else if (!enoughFound) {
      loadRowsDown(false);
      paint();
    }
  }

  public void addMovementListener(ListMovementListener listener) {
    listeners.add(listener);
  }

  public void removeMovementListener(ListMovementListener listener) {
    listeners.remove(listener);
  }

  private void triggerMovementListener() {
    int position = offsetList.getMinRowOffset();
    for (ListMovementListener listener : listeners) {
      listener.movedTo(position);
    }
  }

  public void setTailMode(boolean b) {
    this.tailMode = b;
  }

  public boolean handleInput(InputChar inp) {
    logger.info("list.handleInput " + inp.getCode() + " " + inp.isSpecialCode());

    if (inp.getCode() == InputChar.KEY_DOWN) {
      down();
    } else if (inp.getCode() == InputChar.KEY_UP) {
      up();
    } else if (inp.getCode() == InputChar.KEY_NPAGE) {
      pageDown();
    } else if (inp.getCode() == InputChar.KEY_PPAGE) {
      pageUp();
    } else if (Util.wasCtrlR(inp)) {
      refresh();
    } else if (inp.getCode() == InputChar.KEY_RIGHT) {
      right();
    } else if (inp.getCode() == InputChar.KEY_LEFT) {
      left();
    } else if (inp.getCode() == InputChar.KEY_END) {
      end();
    } else if (inp.getCode() == InputChar.KEY_HOME) {
      home();
    } else {
      return super.handleInput(inp);
    }

    return true;
  }
    
  public Rectangle getRectangle() {
    return super.getRectangle();
  }

  public void addPaintListener(PaintListener listener) {
    this.paintListeners.add(listener);
  }
}

class SmartLineIterator<T> implements ItemModel<T> {
  private ItemModel<T> li;

  private int position = 0;

  private static final int TOP = 1;

  private static final int BOTTOM = 2;

  private static final int NEITHER = 3;

  public SmartLineIterator(ItemModel<T> li) {
    this.li = li;
  }

  public void moveTo(int newPosition) {
    position = NEITHER;
    li.moveTo(newPosition);
  }

  public boolean hasNext() {
    return li.hasNext();
  }

  public T next() {
    position = BOTTOM;
    return li.next();
  }

  public boolean hasPrevious() {
    return li.hasPrevious();
  }

  public T previous() {
    position = TOP;
    return li.previous();
  }

  public void moveToEnd() {
    position = NEITHER;
    li.moveToEnd();
  }

  public void moveToStart() {
    position = NEITHER;
    li.moveToStart();
  }

  public int lastPosition() {
    return li.lastPosition();
  }

  public boolean atTop() {
    return position == TOP;
  }

  public boolean atBottom() {
    return position == BOTTOM;
  }

  public void addItemModelListener(ItemModelListener listener) {
    li.addItemModelListener(listener);
  }

  public void removeItemModelListener(ItemModelListener listener) {
    li.removeItemModelListener(listener);
  }
}
