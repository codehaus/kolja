package com.baulsupp.kolja.ansi;

import java.io.IOException;

public class TailMinusEff extends Cat {
  public void run() throws InterruptedException, IOException {
    while (true) {
      while (isRunning() && i.hasNext()) {
        showNextLine();
      }

      if (isEnd()) {
        return;
      }

      if (isPaused || !i.hasNext()) {
        Thread.sleep(200);
      }
    }
  }

  protected boolean isEnd() {
    return isQuit();
  }
}
