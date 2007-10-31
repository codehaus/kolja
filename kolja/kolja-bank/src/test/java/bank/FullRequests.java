package bank;

import com.baulsupp.kolja.ansi.reports.ReportRunnerMain;

public class FullRequests {
  public static void main(String[] args) {
    ReportRunnerMain.main(new String[] { "-x", "src/main/resources/bank.xml", "-r", "requests?from=6:27&to=6:28&full=true",
        "..\\test-files\\dmsserver-1.log" });
  }
}
