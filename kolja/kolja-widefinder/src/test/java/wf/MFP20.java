package wf;

import com.baulsupp.kolja.ansi.reports.ReportRunnerMain;

public class MFP20 {
  public static void main(String[] args) {
    ReportRunnerMain.main("-x", "src/main/config/wf.xml", "-r", "freq?q=url&count=10", "src/test/resources/o100k.ap");
  }
}
