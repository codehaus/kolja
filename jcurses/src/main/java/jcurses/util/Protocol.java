package jcurses.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Protocol {
  private static Logger log = LoggerFactory.getLogger(Protocol.class);

  public static void system(String message) {
    log.info(message);
  }

  public static void debug(String message) {
    log.debug(message);
  }
}
