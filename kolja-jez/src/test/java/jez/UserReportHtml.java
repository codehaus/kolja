package jez;

import com.baulsupp.kolja.ansi.reports.ReportRunnerMain;

public class UserReportHtml {
  public static void main(String[] args) {
    ReportRunnerMain.main("-x", "src/main/config/jez.xml", "-w", "-r", "freq?q=user", "../test-files/log4j-nyssa.log");
  }
}
