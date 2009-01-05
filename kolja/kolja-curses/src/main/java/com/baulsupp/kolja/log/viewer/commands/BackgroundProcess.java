package com.baulsupp.kolja.log.viewer.commands;

import com.baulsupp.kolja.log.line.ValueIndexer;
import com.baulsupp.kolja.log.util.IntRange;
import com.baulsupp.less.Less;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

public class BackgroundProcess {
  private static final Logger log = LoggerFactory.getLogger(BackgroundProcess.class);

  private static final int BACKGROUND_READAHEAD = 250000;

  private ValueIndexer eventList;

  public BackgroundProcess(final Less less, ValueIndexer eventList) {
    this.eventList = eventList;
      
    Thread t = new Thread("Less-BackgroundThread") {
      @Override
      public void run() {
        try {
          processMore(less);
        } catch (Exception e) {
          log.error("exception", e);
        }
      }
    };
    t.start();
  }

  protected void processMore(Less less) throws Exception {
    while (true) {
      boolean more = less.performWithLock(new Callable<Boolean>() {
        public Boolean call() throws Exception {
          return processSlightlyMore();
        }
      });

      if (!more) {
        sleep(5000);
      }
    }
  }

  private void sleep(long ms) {
    try {
      Thread.sleep(ms);
    } catch (InterruptedException e) {
      // nothing
    }
  }

  private boolean processSlightlyMore() {
    IntRange[] unknown = eventList.listUnknown();

    if (unknown.length == 0) {
      log.info("nothing to process");

      return false;
    } else {
      IntRange first = new IntRange(unknown[0]);

      if (first.getLength() > BACKGROUND_READAHEAD) {
        first.setTo(first.getFrom() + BACKGROUND_READAHEAD);
      }

      log.info("ensuring known " + first);

      eventList.ensureKnown(first);

      return true;
    }
  }

  public void startBackgroundThread() {
    // for now nothing
  }
}
