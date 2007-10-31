package bank;

import com.baulsupp.kolja.ansi.reports.ReportRunnerMain;

public class AllRequests {
  public static void main(String[] args) {
    ReportRunnerMain.main(new String[] { "-x", "src/main/resources/bank.xml", "-r", "allrequests",
        "..\\test-files\\dmsserver-1.log" });
  }
}
