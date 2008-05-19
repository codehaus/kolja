package jez;

import com.baulsupp.kolja.ansi.reports.ReportRunnerMain;

public class Events {
  public static void main(String[] args) {
    ReportRunnerMain.main("-x", "src/main/config/jez.xml", "-r", "events", "../test-files/log4j-nyssa.log");
  }
}
