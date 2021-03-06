package wf;

import org.springframework.util.StopWatch;

import com.baulsupp.kolja.ansi.reports.ReportRunnerMain;

public class MFP20_GZ {
  public static void main(String[] args) {
    StopWatch sw = new StopWatch("Frequency - o1000k.ap");
    sw.start();

    ReportRunnerMain.main("-x", "src/main/config/wf.xml", "-r", "freq?q=url&count=10", "src/test/logs/O.100k.gz");

    sw.stop();

    System.out.println(sw.shortSummary());
  }
}
