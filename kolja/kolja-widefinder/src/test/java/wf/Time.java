package wf;

import com.baulsupp.kolja.ansi.reports.ReportRunnerMain;

public class Time {
  public static void main(String[] args) {
    ReportRunnerMain.main("-x", "src/main/config/wf.xml", "-r", "time?fields=dH", "src/test/logs/o10k.ap");
  }
}
