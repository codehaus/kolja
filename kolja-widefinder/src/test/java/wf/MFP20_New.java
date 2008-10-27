package wf;

import org.springframework.util.StopWatch;

import com.baulsupp.kolja.ansi.reports.ReportRunnerMain;

public class MFP20_New {
  public static void main(String[] args) {
    StopWatch sw = new StopWatch("Frequency - o1000k.ap");
    sw.start();

    ReportRunnerMain.main("-x", "src/main/config/wf.xml", "-r", "freq?q=url&count=10", "src/test/logs/O.100k");

    sw.stop();

    System.out.println(sw.shortSummary());
  }
}
