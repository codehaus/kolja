package com.baulsupp.kolja.log.viewer.request;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.collections.primitives.ArrayIntList;
import org.apache.commons.collections.primitives.IntIterator;
import org.apache.commons.collections.primitives.IntList;

import com.baulsupp.kolja.log.field.MemoryIntField;
import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.line.LineIndex;
import com.baulsupp.kolja.log.line.LineListener;
import com.baulsupp.kolja.log.line.ValueIndexer;
import com.baulsupp.kolja.log.util.IntRange;

public class RequestIndex extends ValueIndexer implements LineIndex {
  protected String requestField;

  protected String messageField;

  protected Map<Object, RequestLine> requestsById = new HashMap<Object, RequestLine>();

  protected Map<Integer, RequestLine> requests = new TreeMap<Integer, RequestLine>();

  protected List<String> fields;

  private Set<LineListener> listeners = new HashSet<LineListener>();

  protected List<FieldCopier> matchers = new ArrayList<FieldCopier>();

  public RequestIndex(String requestField, String messageField, List<String> fields, LineIndex li) {
    super(li);
    this.fields = fields;
    this.requestField = requestField;
    this.messageField = messageField;
  }

  public int length() {
    return li.length();
  }

  protected void processLines(IntRange region, List<Line> regionLines) {
    IntList values = new ArrayIntList();

    for (Line line : regionLines) {
      Object requestId = line.getValue(requestField);

      RequestLine requestLine = requestsById.get(requestId);

      if (requestLine == null) {
        requestLine = createInitialLine(line, values);
      }

      updateLine(line, requestLine, values);
    }

    MemoryIntField lineOffsets = new MemoryIntField(region, values);
    indexed.add(lineOffsets);
  }

  protected void updateLine(Line line, RequestLine requestLine, IntList values) {
    requestLine.addLine(line);

    updateMatchers(line, requestLine);
  }

  protected void updateMatchers(Line line, RequestLine requestLine) {
    for (FieldCopier copier: matchers) {
      copier.copy(line, requestLine);
    }
  }

  protected RequestLine createInitialLine(Line line, IntList values) {
    Object identifier = line.getValue(requestField);

    RequestLine requestLine = new RequestLine(identifier, null);

    requestLine.setOffset(line.getOffset());

    for (String field : fields) {
      requestLine.setValue(field, line.getValue(field));
    }

    requestsById.put(identifier, requestLine);
    requests.put(line.getOffset(), requestLine);
    values.add(line.getOffset());

    return requestLine;
  }

  public Collection<RequestLine> getKnown() {
    return requests.values();
  }

  public List<Line> getAll() {
    return get(new IntRange(0, length()));
  }

  public List<Line> get(IntRange region) {
    ensureKnown(region);

    IntList offsets = indexed.get(region);

    if (offsets == null) {
      throw new NullPointerException(region.toString() + " " + li.length() + " "
          + Arrays.asList(indexed.listUnknownRanges(region)));
    }

    List<Line> result = offsetToLineList(offsets);

    for (Line line : result) {
      ensureLineKnown(region, (RequestLine) line);
    }

    return result;
  }

  // TODO ensure doesn't go on forever
  private void ensureLineKnown(IntRange region, RequestLine line) {
    if (line.isComplete()) {
      return;
    }

    region = new IntRange(region);

    while (!line.isComplete()) {
      if (!line.isStartFound()) {
        region.setFrom(Math.max(0, region.getFrom() - region.getLength()));
      }
      if (!line.isEndFound()) {
        region.setTo(Math.min(li.length() - 1, region.getTo() + region.getLength()));
      }

      ensureKnown(region);
    }
  }

  private List<Line> offsetToLineList(IntList offsets) {
    List<Line> results = new ArrayList<Line>();

    IntIterator i = offsets.iterator();
    while (i.hasNext()) {
      int offset = i.next();
      RequestLine requestLine = requests.get(offset);

      if (requestLine == null) {
        throw new IllegalStateException("line not found");
      }

      results.add(requestLine);
    }

    return results;
  }

  public void addMatcher(FieldCopier fieldCopier) {
    matchers.add(fieldCopier);
  }

  public void fireListeners(IntRange region, List<Line> lines) {
    if (listeners.size() > 0) {
      for (LineListener listener : listeners) {
        listener.linesAvailable(region, lines);
      }
    }
  }

  public void addLineListener(LineListener listener) {
    if (true) {
      throw new UnsupportedOperationException("not tested");
    }

    listeners.add(listener);
  }

  public Object getRequestIdentifier(Line viewRow) {
    return viewRow.getValue(requestField);
  }
}
