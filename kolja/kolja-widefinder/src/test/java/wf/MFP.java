package wf;

import com.baulsupp.kolja.ansi.reports.ReportRunnerMain;

public class MFP {
  public static void main(String[] args) {
    ReportRunnerMain.main("-x", "src/main/config/wf.xml", "-r", "freq?q=url&count=3", "src/test/resources/o10k.ap");
  }
}
