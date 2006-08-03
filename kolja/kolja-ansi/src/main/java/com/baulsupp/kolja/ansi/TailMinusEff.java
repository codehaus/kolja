package com.baulsupp.kolja.ansi;

import java.io.IOException;

import com.baulsupp.kolja.log.line.Reloadable;

public class TailMinusEff extends Cat {
  public void run() throws InterruptedException, IOException {
    while (true) {
      while (isRunning() && i.hasNext()) {
        showNextLine();
      }

      if (isEnd()) {
        return;
      }

      if (isPaused) {
        Thread.sleep(200);
      } else {
        waitForUpdate();
      }
    }
  }

  protected boolean isEnd() {
    return isQuit();
  }

  private void waitForUpdate() throws IOException, InterruptedException {
    if (!reloadIfNeeded()) {
      Thread.sleep(200);
    }
  }

  private boolean reloadIfNeeded() throws IOException {
    if (isPaused) {
      return false;
    }

    if (i instanceof Reloadable) {
      return ((Reloadable) i).reload();
    }

    return false;
  }
}
