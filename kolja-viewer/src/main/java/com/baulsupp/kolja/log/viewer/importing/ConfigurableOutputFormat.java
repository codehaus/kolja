package com.baulsupp.kolja.log.viewer.importing;

import java.io.Serializable;
import java.util.List;

import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.viewer.columns.ColumnWidths;
import com.baulsupp.kolja.log.viewer.format.OutputFormat;
import com.baulsupp.kolja.log.viewer.highlight.HighlightList;
import com.baulsupp.kolja.log.viewer.renderer.FieldRenderer;
import com.baulsupp.kolja.log.viewer.renderer.Renderer;

public class ConfigurableOutputFormat implements Serializable {
  private static final long serialVersionUID = -4225346124170712619L;

  private ColumnWidths widths;
  private List<OutputFormat> formats;
  private String additional;

  private HighlightList<Line> highlights;

  private List<String> names;

  public List<OutputFormat> getFormats() {
    return formats;
  }

  public void setFormats(List<OutputFormat> formats) {
    this.formats = formats;
  }

  public ColumnWidths getWidths() {
    return widths;
  }

  public void setWidths(ColumnWidths widths) {
    this.widths = widths;
  }

  public String getAdditional() {
    return additional;
  }

  public void setAdditional(String additional) {
    this.additional = additional;
  }

  public void setHighlights(HighlightList<Line> highlights) {
    this.highlights = highlights;
  }

  public HighlightList<Line> getHighlights() {
    return highlights;
  }

  public List<String> getNames() {
    return names;
  }

  public void setNames(List<String> names) {
    this.names = names;
  }

  public Renderer<Line> getRenderer() {
    FieldRenderer grid = new FieldRenderer(widths, names, formats, getHighlights());
    grid.showAdditional(additional);
    return grid;
  }
}
