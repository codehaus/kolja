package com.baulsupp.kolja.log.viewer.io;

import java.util.Comparator;

import org.apache.commons.lang.builder.CompareToBuilder;

import com.baulsupp.kolja.log.line.Line;

public class LineComparator implements Comparator<Line> {
  private String[] fieldNames;
  
  public LineComparator(String... fieldNames) {
    this.fieldNames = fieldNames;
  }

  public int compare(Line l1, Line l2) {
    if (l1 == l2) {
      return 0;
    }
    
    CompareToBuilder compareToBuilder = new CompareToBuilder();
    
    for (String fieldName : fieldNames) {
      compareToBuilder.append(l1.getValue(fieldName), l2.getValue(fieldName));
    }
    
    compareToBuilder.append(-1, 0);
    
    return compareToBuilder.toComparison();
  }
}
