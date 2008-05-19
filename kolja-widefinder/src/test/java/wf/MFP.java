package wf;

import com.baulsupp.kolja.ansi.reports.ReportRunnerMain;

public class MFP {
  public static void main(String[] args) {
    ReportRunnerMain.main("-x", "src/test/config/wf.xml", "-r", "freq?q=url&count=3", "src/test/logs/o10k.ap");
  }
}
