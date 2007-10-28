package bank;

import com.baulsupp.kolja.ansi.reports.ReportRunnerMain;

public class Requests {
  public static void main(String[] args) {
    ReportRunnerMain
        .main(new String[] { "-x", "src/main/resources/bank.xml", "-r", "requests", "..\\test-files\\dmsserver-1.log" });
  }
}
