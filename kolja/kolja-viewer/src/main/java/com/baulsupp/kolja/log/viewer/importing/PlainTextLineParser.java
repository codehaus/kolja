package com.baulsupp.kolja.log.viewer.importing;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.baulsupp.kolja.log.line.BasicLine;
import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.line.LineParser;

public class PlainTextLineParser implements LineParser {
  public Line parse(CharSequence c) {
    BasicLine l = new BasicLine();

    l.setContent(c);

    Map<String, Object> emptyMap = new HashMap<String, Object>();
    l.setValues(emptyMap);

    return l;
  }

  public Set<String> getNames() {
    return Collections.emptySet();
  }
}
