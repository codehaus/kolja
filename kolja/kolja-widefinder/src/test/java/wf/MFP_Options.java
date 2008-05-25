package wf;

import com.baulsupp.kolja.ansi.reports.ReportRunnerMain;

public class MFP_Options {
  public static void main(String[] args) {
    ReportRunnerMain.main("-x", "src/main/config/wf.xml", "-p", "console?ansi=false&fixedWidth=true&interactive=true", "-r",
        "freq?q=url&count=3", "src/test/logs/o10k.ap");
  }
}
