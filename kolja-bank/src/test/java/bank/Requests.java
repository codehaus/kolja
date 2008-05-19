package bank;

import com.baulsupp.kolja.ansi.reports.ReportRunnerMain;

public class Requests {
  public static void main(String[] args) {
    ReportRunnerMain.main("-x", "src/main/resources/bank.xml", "-r", "requests?from=6:55&to=7:10",
        "..\\test-files\\dmsserver-1.log");
  }
}
