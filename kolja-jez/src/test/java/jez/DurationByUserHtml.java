package jez;

import com.baulsupp.kolja.ansi.reports.ReportRunnerMain;

public class DurationByUserHtml {
  public static void main(String[] args) {
    ReportRunnerMain.main("-x", "src/main/config/jez.xml", "-w", "-r", "usage", "../test-files/log4j-nyssa.log");
  }
}
