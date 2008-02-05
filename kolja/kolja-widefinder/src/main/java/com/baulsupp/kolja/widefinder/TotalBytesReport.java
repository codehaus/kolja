package com.baulsupp.kolja.widefinder;

import java.util.concurrent.atomic.AtomicLong;

import com.baulsupp.kolja.ansi.reports.AbstractTextReport;
import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.viewer.format.BytesFormat;

public class TotalBytesReport extends AbstractTextReport {
  private AtomicLong total = new AtomicLong();
  private BytesFormat format = new BytesFormat();

  @Override
  public void processLine(Line line) {
    Long bytes = (Long) line.getValue(WideFinderConstants.SIZE);

    if (bytes != null) {
      total.addAndGet(bytes);
    }
  }

  public String describe() {
    return "Total Bytes";
  }

  @Override
  public void completed() {
    println("Total bytes: " + total.get() + " (" + format.format(total.get()) + ")");
  }
}
