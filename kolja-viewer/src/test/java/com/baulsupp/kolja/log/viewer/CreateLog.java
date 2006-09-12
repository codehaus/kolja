package com.baulsupp.kolja.log.viewer;

import java.io.IOException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

public class CreateLog implements Runnable {
  private static final Logger logger = Logger.getLogger(CreateLog.class);

  public static Level[] levels = new Level[] { Level.DEBUG, Level.INFO, Level.WARN, Level.ERROR };

  public static void main(String[] args) throws IOException {
//    LogConfig.config("test");

    DOMConfigurator.configure("conf/log4j-kolja.xml");

    logger.info("starting");

    CreateLog c = new CreateLog();
    for (int i = 0; i < 5; i++) {
      runThread(c);
    }
  }

  private static void runThread(Runnable r) {
    Thread t = new Thread(r);
    t.start();
  }

  public void run() {
    while (true) {
      int lines = random(10);

      logger.info("creating " + lines);

      for (int i = 0; i < lines; i++) {
        Level level = randomLevel();

        logger.log(level, "Message");
      }

      try {
        Thread.sleep(1000 + random(15));
      } catch (InterruptedException e) {
      }
    }
  }

  private Level randomLevel() {
    return levels[random(levels.length)];
  }

  private int random(int i) {
    return (int) (Math.random() * i);
  }
}
