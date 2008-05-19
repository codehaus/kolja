package com.baulsupp.kolja.log.viewer.columns;

import java.beans.PropertyEditorSupport;

public class ColumnWidthsPropertyEditor extends PropertyEditorSupport {
  public void setAsText(String text) throws IllegalArgumentException {
      setValue(ColumnWidths.parse(text));
  }
}