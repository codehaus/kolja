package jez;

import com.baulsupp.kolja.ansi.reports.ReportRunnerMain;

public class FailureReport {
  public static void main(String[] args) {
    ReportRunnerMain.main("-x", "src/main/config/jez.xml", "-r", "failed", "../test-files/log4j-nyssa.log");
  }
}
