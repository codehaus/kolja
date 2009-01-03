package com.baulsupp.kolja.log.viewer.importing;

import java.io.Serializable;

import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.line.LineIndex;
import com.baulsupp.kolja.log.line.LineParser;
import com.baulsupp.kolja.log.line.matcher.EntryPattern;
import com.baulsupp.kolja.log.viewer.event.EventList;
import com.baulsupp.kolja.log.viewer.event.EventMatcher;
import com.baulsupp.kolja.log.viewer.renderer.Renderer;
import com.baulsupp.kolja.log.viewer.request.StandardRequestIndex;

public class ConfigurableLogFormat implements LogFormat, Serializable {
  private static final long serialVersionUID = 6264504331354771149L;

  private ConfigurableLineFormat lineFormat;
  private ConfigurableRequestFormat requestFormat;
  private ConfigurableOutputFormat outputFormat;
  private ConfigurableOutputFormat requestOutputFormat;
  private ConfigurableEventFormat eventFormat;

  public ConfigurableEventFormat getEventFormat() {
    return eventFormat;
  }

  public void setEventFormat(ConfigurableEventFormat eventFormat) {
    this.eventFormat = eventFormat;
  }

  public ConfigurableLineFormat getLineFormat() {
    return lineFormat;
  }

  public void setLineFormat(ConfigurableLineFormat lineFormat) {
    this.lineFormat = lineFormat;
  }

  public ConfigurableOutputFormat getOutputFormat() {
    return outputFormat;
  }

  public void setOutputFormat(ConfigurableOutputFormat outputFormat) {
    this.outputFormat = outputFormat;
  }

  public void setRequestOutputFormat(ConfigurableOutputFormat requestOutputFormat) {
    this.requestOutputFormat = requestOutputFormat;
  }

  public ConfigurableRequestFormat getRequestFormat() {
    return requestFormat;
  }

  public void setRequestFormat(ConfigurableRequestFormat requestFormat) {
    this.requestFormat = requestFormat;
  }

  public LineIndex getLineIndex(CharSequence buffer) {
    return lineFormat.buildLineIndex(buffer);
  }

  public Renderer<Line> getLineRenderer() {
    return outputFormat.getRenderer();
  }

  public Renderer<Line> getRequestRenderer() {
    if (requestOutputFormat == null) {
      return getLineRenderer();
    } else {
      return requestOutputFormat.getRenderer();
    }
  }

  public StandardRequestIndex getRequestIndex(LineIndex lineIndex) {
    return requestFormat.getRequestIndex(lineIndex, lineFormat);
  }

  public boolean supportsRequests() {
    return requestFormat != null;
  }

  public EventList getEventList(LineIndex li) {
    return supportsEvents() ? eventFormat.getEventList(li) : null;
  }

  public EventMatcher getEventDetector() {
    return supportsEvents() ? eventFormat.getEventDetector() : null;
  }

  public boolean supportsEvents() {
    return eventFormat != null && eventFormat.hasEvents();
  }

  public String getRequestField() {
    return requestFormat != null ? requestFormat.getRequestField() : null;
  }

  public EntryPattern getEntryPattern() {
    return lineFormat.getEntryPattern();
  }

  public LineParser getLineParser() {
    return lineFormat.getLineParser();
  }
}
