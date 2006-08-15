package jcurses.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Protocol {
  private static Log log = LogFactory.getLog(Protocol.class);
  
	public static void system(String message) {
		log.info(message);
	}
	
	public static void debug(String message) {
		log.debug(message);
	}
}
