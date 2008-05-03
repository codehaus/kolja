package com.baulsupp.kolja.widefinder.categories;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import com.baulsupp.kolja.ansi.reports.AbstractTextReport;
import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.widefinder.WideFinderConstants;

public class CategorisationReport extends AbstractTextReport {
  private FileTypeCategoriser categoriser = new WideFinderTypeCategoriser();

  private Map<String, Stats> stats = new HashMap<String, Stats>();
  private Stats total = new Stats("Total");

  @Override
  public void processLine(Line line) {
    Long bytes = (Long) line.getValue(WideFinderConstants.SIZE);
    if (bytes == null) {
      bytes = 0l;
    }

    Stats stats = getStats((String) line.getValue(WideFinderConstants.URL));
    stats.addBytes(bytes);

    if (bytes != null) {
      total.addBytes(bytes);
    }
  }

  private Stats getStats(String url) {
    String type = getType(url);

    Stats s = stats.get(type);

    if (s == null) {
      s = new Stats(type);

      stats.put(type, s);
    }

    return s;
  }

  private String getType(String url) {
    FileType fileType = categoriser.getFileType(url);

    String type = null;
    if (fileType.equals(StandardTypeCategoriser.UNKNOWN)) {
      type = categoriser.getExtension(url);

      if (type == null) {
        type = "Unknown";
      }
    } else {
      type = fileType.getCategory();
    }

    return type;
  }

  public String describe() {
    return "File Types";
  }

  @Override
  public void completed() {
    Collection<Stats> values = new TreeSet<Stats>(Stats.BY_SIZE);
    values.addAll(stats.values());

    for (Stats s : values) {
      println(s.toString());
    }

    println(total.toString());
  }
}
