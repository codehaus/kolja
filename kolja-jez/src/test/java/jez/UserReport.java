package jez;

import com.baulsupp.kolja.ansi.reports.ReportRunnerMain;

public class UserReport {
  public static void main(String[] args) {
    ReportRunnerMain.main("-x", "src/main/config/jez.xml", "-r", "freq?q=user&count=5", "../test-files/log4j-nyssa.log");
  }
}
