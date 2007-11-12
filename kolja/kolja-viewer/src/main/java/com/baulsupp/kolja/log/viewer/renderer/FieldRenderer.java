package com.baulsupp.kolja.log.viewer.renderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.viewer.columns.Column;
import com.baulsupp.kolja.log.viewer.columns.ColumnWidths;
import com.baulsupp.kolja.log.viewer.format.OutputFormat;
import com.baulsupp.kolja.log.viewer.format.ToStringFormat;
import com.baulsupp.kolja.log.viewer.highlight.Highlight;
import com.baulsupp.kolja.log.viewer.highlight.HighlightList;
import com.baulsupp.kolja.log.viewer.highlight.HighlightResult;
import com.baulsupp.kolja.util.TextUtil;
import com.baulsupp.kolja.util.colours.ColourPair;
import com.baulsupp.kolja.util.colours.ColouredString;
import com.baulsupp.kolja.util.colours.MultiColourString;

public class FieldRenderer implements Renderer<Line> {
  public static final Logger logger = LoggerFactory.getLogger(FieldRenderer.class);

  public static ColouredString DEFAULT_SEPERATOR = new ColouredString(null, " ");

  protected ColouredString seperator = DEFAULT_SEPERATOR;

  private ColumnWidths widths;

  private List<String> columns;

  private List<OutputFormat> formats;

  private Highlight<Line> highlight;

  private String additional = null;

  private Wrap wrappingMode = Wrap.NONE;

  private int screenWidth;

  public FieldRenderer(ColumnWidths widths, List<String> columns, List<OutputFormat> formats, Highlight<Line> highlight) {
    this.widths = widths;
    this.columns = columns;
    this.formats = formats;
    this.highlight = highlight;
  }

  public FieldRenderer() {
    widths = new ColumnWidths();
    columns = new ArrayList<String>();
    formats = new ArrayList<OutputFormat>();
  }

  public TextDisplayRow getRow(Line viewRow) {
    OutputRow row = new OutputRow(viewRow);
    ColouredString seperator = null;

    if (viewRow.isFailed()) {
      row.append(viewRow.toString());
      return row;
    }
    
    int l = widths.getColumnCount();

    HighlightResult highlightResults = null;
    if (highlight != null) {
      highlightResults = highlight.getHighlights(viewRow);
    }

    ColourPair rowColour = highlightResults != null ? highlightResults.getRow() : null;
    if (rowColour != null) {
      seperator = this.seperator.changeColour(rowColour);
    } else {
      seperator = this.seperator;
    }

    MultiColourString gridRow = new MultiColourString();
    boolean firstColumn = true;
    for (int i = 0; i < l; i++) {
      String currentColumn = columns.get(i);

      ColourPair columnColour = rowColour;
      if (highlightResults != null) {
        ColourPair newPair = highlightResults.getColumnHighlight(currentColumn);
        if (newPair != null) {
          columnColour = newPair;
        }
      }

      int itemWidth = widths.get(i);

      if (itemWidth > 0) {
        if (!firstColumn) {
          gridRow.append(seperator);
        } else {
          firstColumn = false;
        }

        Object value = viewRow.getValue(currentColumn);
        String display = formats.get(i).format(value);

        MultiColourString msc = applyHighlights(display, columnColour, highlightResults);

        if (i == l - 1) {
          gridRow.append(msc);
        } else {
          gridRow.append(fixedWidth(msc, itemWidth, rowColour));
        }
      }
    }

    row.appendColouredLines(wrappingMode.wrap(gridRow, screenWidth));

    if (additional != null) {
      String value = (String) viewRow.getValue(additional);

      if (value != null && value.length() > 0) {
        row.newLine();
        MultiColourString colouredValue = new MultiColourString(rowColour, value);
        row.appendColouredLines(wrappingMode.wrap(colouredValue, screenWidth));
      }
    }

    return row;
  }

  // TODO word highlighting
  private MultiColourString applyHighlights(String display, ColourPair columnColour, HighlightResult highlightResults) {
    MultiColourString result = new MultiColourString(columnColour, display);

    if (highlightResults != null) {
      for (Entry<String, ColourPair> highlight : highlightResults.getWords().entrySet()) {
        highlightWord(result, highlight.getKey(), highlight.getValue());
      }
    }

    return result;
  }

  private void highlightWord(MultiColourString result, String key, ColourPair colours) {
    String plainText = result.toString();

    Matcher m = Pattern.compile(key, Pattern.LITERAL).matcher(plainText);

    while (m.find()) {
      result.setColour(colours, m.start(), m.end());
    }
  }

  public MultiColourString fixedWidth(MultiColourString string, int itemWidth, ColourPair rowColour) {
    if (string.length() < itemWidth) {
      int extra = itemWidth - string.length();

      string = new MultiColourString(string);
      string.append(new ColouredString(rowColour, TextUtil.blank(extra)));

      return string;
    } else if (string.length() > itemWidth) {
      return string.part(0, itemWidth);
    } else {
      return string;
    }
  }

  public List<String> getColumns() {
    return columns;
  }

  public void setColumns(List<String> columns) {
    this.columns = columns;
  }

  public ColumnWidths getWidths() {
    return widths;
  }

  public void setWidths(ColumnWidths widths) {
    this.widths = widths;
  }

  public void setWidth(int width) {
    this.screenWidth = width;
    widths.setScreenWidth(width);
    logger.info(width + " - " + widths);
  }

  public Wrap getWrappingMode() {
    return wrappingMode;
  }

  public void setWrappingMode(Wrap wrappingMode) {
    this.wrappingMode = wrappingMode;
  }

  public void addHighlight(Highlight<Line> highlight) {
    if (this.highlight == null) {
      this.highlight = highlight;
    } else {
      if (!(this.highlight instanceof HighlightList)) {
        this.highlight = new HighlightList<Line>(this.highlight);
      }
  
      ((HighlightList<Line>) this.highlight).addHighlight(highlight);
    }
  }

  public void showAdditional(String column) {
    this.additional = column;
  }

  public void prependColumn(String name, int width) {
    prependColumn(name, width, new ToStringFormat());
  }

  public void appendColumn(String name, int width) {
    appendColumn(name, width, new ToStringFormat());
  }
  
  public void prependColumn(String name, int width, OutputFormat of) {
    this.columns.add(0, name);
    this.formats.add(0, of);
    this.widths.addColumn(0, new Column(width));
  }

  public void appendColumn(String name, int width, OutputFormat of) {
    this.columns.add(name);
    this.formats.add(of);
    this.widths.addColumn(new Column(width));
  }
}
