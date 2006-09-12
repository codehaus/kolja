package com.baulsupp.less;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.regex.Pattern;

import jcurses.system.CharColor;
import jcurses.system.InputChar;
import jcurses.system.Toolkit;
import jcurses.util.Message;
import jcurses.util.Rectangle;
import jcurses.widgets.IScrollable;
import jcurses.widgets.ScrollbarPainter;

import org.apache.log4j.Logger;

import com.baulsupp.curses.list.ColorList;
import com.baulsupp.curses.list.CursesListRenderer;
import com.baulsupp.curses.list.CursesListWindow;
import com.baulsupp.curses.list.PaintListener;
import com.baulsupp.curses.list.TextDialog;
import com.baulsupp.curses.list.TextRenderer;
import com.baulsupp.curses.list.Util;
import com.baulsupp.kolja.log.filter.Filter;
import com.baulsupp.kolja.log.filter.FilteredLineIndex;
import com.baulsupp.kolja.log.filter.PriorityFilter;
import com.baulsupp.kolja.log.line.BasicLineIterator;
import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.line.LineIndex;
import com.baulsupp.kolja.log.util.IntRange;
import com.baulsupp.kolja.log.util.WrappedCharBuffer;
import com.baulsupp.kolja.log.viewer.event.Event;
import com.baulsupp.kolja.log.viewer.event.EventList;
import com.baulsupp.kolja.log.viewer.highlight.BasicSearchHighlight;
import com.baulsupp.kolja.log.viewer.importing.LogFormat;
import com.baulsupp.kolja.log.viewer.linenumbers.BasicLineNumberIndex;
import com.baulsupp.kolja.log.viewer.renderer.FieldRenderer;
import com.baulsupp.kolja.log.viewer.renderer.Renderer;
import com.baulsupp.kolja.log.viewer.renderer.Wrap;
import com.baulsupp.kolja.log.viewer.request.RequestHighlight;
import com.baulsupp.kolja.log.viewer.request.RequestIndex;
import com.baulsupp.kolja.log.viewer.request.RequestLine;
import com.baulsupp.kolja.log.viewer.search.BasicSearch;
import com.baulsupp.kolja.log.viewer.search.Search;
import com.baulsupp.kolja.util.ColourPair;

public class Less extends CursesListWindow<Line> implements IScrollable, PaintListener {
  private static final int BACKGROUND_READAHEAD = 25000;

  private static final Logger log = Logger.getLogger(Less.class);

  private java.util.List<LineIndex> models;

  private LineIndex currentIndex;

  private java.util.List<CursesListRenderer<Line>> renderers;

  private WrappedCharBuffer buffer;

  private Search search;

  private BasicSearchHighlight basicSearchHighlight;

  private ScrollbarPainter scrollbar;
  
  private boolean drawScrollbar = false;
  private EventList eventList;
  private RequestIndex requestIndex;

  private RequestHighlight requestHighlight;
  
  private ResponsiveLock uiLock = new ResponsiveLock();
  private Lock backgroundLock = uiLock.createBackgroundLock(1500);

  public Less() {
    this.scrollbar = new ScrollbarPainter(this);

    list.addPaintListener(this);
  }

  public void open(LogFormat format, File file) throws IOException {
    buffer = WrappedCharBuffer.fromFile(file);

    loadIndexes(format);

    loadRenderer(format);

    loadLineNumbers();

    rotateFilter();
    rotateRenderer();
  }

  private void loadLineNumbers() {
    setLineNumbers(BasicLineNumberIndex.create(buffer));
  }

  private void loadIndexes(LogFormat format) {
    models = new ArrayList<LineIndex>();
    
    LineIndex li = format.getLineIndex(buffer);
    models.add(li);

    if (format.supportsEvents()) {
      eventList = format.getEventList(li);
      li.addLineListener(eventList);
    }

    // TODO move filtering to config and allow choices
    Filter filter = new PriorityFilter();
    LineIndex filteredIndex = new FilteredLineIndex(li, filter);
    models.add(filteredIndex);

    if (format.supportsRequests()) {
      requestIndex = format.getRequestIndex(li);
    }
  }

  private void loadRenderer(LogFormat format) {
    renderers = new ArrayList<CursesListRenderer<Line>>();
    
    Renderer<Line> gridNonWrapped = format.getRenderer();
    gridNonWrapped.setWidth(Toolkit.getScreenWidth());
    
    ViewRowRenderer viewRenderer = new ViewRowRenderer(gridNonWrapped);
    renderers.add(viewRenderer);

    basicSearchHighlight = new BasicSearchHighlight(ColourPair.RED_ON_BLACK);
    
    if (gridNonWrapped instanceof FieldRenderer) {
      ((FieldRenderer) gridNonWrapped).addHighlight(basicSearchHighlight);
      ((FieldRenderer) gridNonWrapped).setWrappingMode(Wrap.NONE);
      
      Renderer<Line> gridWrapped = format.getRenderer();
      gridWrapped.setWidth(Toolkit.getScreenWidth());
      ((FieldRenderer) gridWrapped).setWrappingMode(Wrap.LAST_COLUMN);
      ((FieldRenderer) gridWrapped).addHighlight(basicSearchHighlight);

      if (format.supportsRequests()) {  
        requestHighlight = new RequestHighlight(ColourPair.MAGENTA_ON_BLACK, requestIndex, null);

        ((FieldRenderer) gridWrapped).addHighlight(requestHighlight);
        ((FieldRenderer) gridNonWrapped).addHighlight(requestHighlight);
      }
      
      viewRenderer = new ViewRowRenderer(gridWrapped);
      renderers.add(viewRenderer);
    }

    TextRenderer<Line> plain = new TextRenderer<Line>();
    plain.setWrapping(Toolkit.getScreenWidth());
    renderers.add(plain);

    TextRenderer<Line> plainUnwrapped = new TextRenderer<Line>();
    plainUnwrapped.setWrapping(TextRenderer.NO_WRAP);
    renderers.add(plainUnwrapped);
  }
  
  public boolean handleInput(InputChar inp) {
    try {
      uiLock.lock();
      
      return internalHandleInput(inp);
    } finally {
      uiLock.unlock();
    }
  }

  public boolean internalHandleInput(InputChar inp) {
    try {
      if (Util.wasLetter(inp, 'f'))
        rotateFilter();
      else if (Util.wasLetter(inp, '?'))
        showHelp();
      else if (Util.wasLetter(inp, 'r'))
        rotateRenderer();
      else if (supportsSearch() && Util.wasLetter(inp, '/'))
        newSearch();
      else if (Util.wasLetter(inp, 'n'))
        nextSearchResult();
      else if (Util.wasLetter(inp, 'p'))
        previousSearchResult();
      else if (Util.wasLetter(inp, 's'))
        toggleScrollbar();
      else if (Util.wasLetter(inp, 'e'))
        selectEvent();
      else if (Util.wasLetter(inp, 't'))
        selectRequest();
      else
        return super.handleInput(inp);
    } catch (Exception e) {
      log.error("error", e);
      shutdown();
    }

    return true;
  }

  private void selectEvent() {
    if (eventList != null) {
      Event e = EventDialog.getValue(eventList);

      if (e != null) {
        log.info("selected " + e + " moving to " + e.getOffset());
        list.moveTo(e.getOffset());
      } else {
        log.info("no request selected");
      }
    }
  }

  private void selectRequest() {
    if (requestIndex != null) {
      RequestLine l = RequestDialog.getValue(requestIndex, list.getOffset());

      if (l != null) {
        log.info("selected " + l + " moving to " + l.getOffset());
        requestHighlight.setValue(l.getIdentifier());
        list.moveTo(l.getMinimumKnownOffset());
      } else {
        log.info("no request selected");
      }
    }
  }

  private void toggleScrollbar() {
    if (drawScrollbar) {
      drawScrollbar = false;
    } else {
      drawScrollbar = true;
    }

    list.doPaint();
  }

  void showHelp() {
    new Message("Help", "Help\nUse arrow keys to move.  q to quit.", "ok").show();
  }

  void rotateFilter() {
    setModel(getNextLineModel());
  }

  void setModel(LineIndex li) {
    currentIndex = li;
    list.setModel(new LineIndexItemModel(new BasicLineIterator(li)));

    if (search != null) {
      search.setModel(new LineIndexItemModel(new BasicLineIterator(currentIndex)));
    }
  }

  LineIndex getNextLineModel() {
    LineIndex li = (LineIndex) models.remove(0);
    models.add(li);

    return li;
  }

  void rotateRenderer() {
    CursesListRenderer<Line> nextRenderer = getNextRenderer();

    if (nextRenderer instanceof ViewRowRenderer) {
      ViewRowRenderer viewRenderer = (ViewRowRenderer) nextRenderer;
      
      if (viewRenderer.getRenderer() instanceof FieldRenderer) {
        FieldRenderer fieldRenderer = (FieldRenderer) viewRenderer.getRenderer();
        list.setSidewaysSteps(fieldRenderer.getWidths().getSteps());
      }
    } else {
      list.setSidewaysSteps(null);
    }
    
    list.setSidewaysMove(10);
    
    list.setRenderer(nextRenderer);
  }

  CursesListRenderer<Line> getNextRenderer() {
    CursesListRenderer<Line> r = (CursesListRenderer<Line>) renderers.remove(0);
    renderers.add(r);

    return r;
  }

  protected boolean supportsSearch() {
    return true;
  }

  protected void newSearch() {
    String a = TextDialog.getValue("Search");

    Pattern pattern = Pattern.compile(a);

    search = new BasicSearch(pattern);

    search.setModel(new LineIndexItemModel(new BasicLineIterator(currentIndex)));

    basicSearchHighlight.setPattern(pattern);

    nextSearchResult();
  }

  private boolean hasCurrentSearch() {
    return search != null;
  }

  private void nextSearchResult() {
    if (hasCurrentSearch()) {
      int pos = search.next(list.getOffset());

      if (pos != -1) {
        list.moveTo(pos);
      }
    }
  }

  private void previousSearchResult() {
    if (hasCurrentSearch()) {
      int pos = search.previous(list.getOffset());

      if (pos != -1) {
        list.moveTo(pos);
      }
    }
  }


  public boolean hasHorizontalScrollbar() {
    return false;
  }

  public boolean hasVerticalScrollbar() {
    return true;
  }

  public Rectangle getBorderRectangle() {
    Rectangle r = list.getRectangle();
    
    // account for normal (unwanted) margin
    r.setY(-1);
    r.setHeight(r.getHeight() + 2);
    
    r.setX(r.getX() + r.getWidth() - 1);
    r.setWidth(1);
    return r;
  }

  public CharColor getBorderColors() {
    return ColorList.blackOnWhite;
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
    float offset = list.getOffset();
    float total = buffer.length();
    
    return offset / total;
  }

  public float getVerticalScrollbarLength() {
    float firstOffset = list.getOffset();
    float lastOffset = list.getLastOffset();
    float total = buffer.length();

    return (lastOffset - firstOffset) / total;
  }

  public void painted() {
    uiLock.startBackgroundThreads();
    
    if (drawScrollbar) {
      if (log.isInfoEnabled()) {
        log.info("scrollbar " + getVerticalScrollbarOffset() + " " + getVerticalScrollbarLength());
      }
      scrollbar.paint();
    }
  }

  public void startBackgroundThread() {
    if (eventList != null) {
      Thread t = new Thread("Less-BackgroundThread") {
        @Override
        public void run() {
          try {
            processMore();
          } catch (Exception e) {
            log.error("exception", e);
          }
        }
      };
      t.start();
    }
  }

  protected void processMore() {
    while (true) {
      boolean more = false;
      
      backgroundLock.lock();
      
      try {
        more = processSlightlyMore();
      } finally {
        backgroundLock.unlock();
      }
      
      if (!more) {
        sleep(5000);
      }
    }
  }
  
  private void sleep(long ms) {
    try {
      Thread.sleep(ms);
    } catch (InterruptedException e) {
      // nothing
    }
  }

  private boolean processSlightlyMore() {
    IntRange[] unknown = eventList.listUnknown();
    
    if (unknown.length == 0) {
      log.info("nothing to process");

      return false;
    } else {
      IntRange first = new IntRange(unknown[0]);
      
      if (first.getLength() > BACKGROUND_READAHEAD) {
        first.setTo(first.getFrom() + BACKGROUND_READAHEAD);
      }
      
      log.info("ensuring known " + first);
      
      eventList.ensureKnown(first);
      
      return true;
    }
  }
}