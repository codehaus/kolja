package wf;

import com.baulsupp.kolja.ansi.reports.ReportRunnerMain;

public class Multiple {
  public static void main(String[] args) {
    ReportRunnerMain.main("-x", "src/main/config/wf.xml", "-r", "freq?q=ipaddress&count=10", "-r", "freq?q=url&count=10", "-r",
        "time?fields=d&count=10", "-r", "time?fields=H&count=10", "src/test/resources/o10k.ap");
  }
}
