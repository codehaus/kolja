package jcurses.system;

import jcurses.util.Protocol;

public class PlatformUtil {
	static {
		Protocol.system("os.name " + System.getProperty("os.name"));
		Protocol.system("os.arch " + System.getProperty("os.arch"));
	}
	
  public static boolean isWindows() {
    return System.getProperty("os.name").toLowerCase().contains("windows");
  }

  public static boolean isMacOsxPpc() {
    return System.getProperty("os.name").equals("Mac OS X") && !System.getProperty("os.arch").equals("i386") && !System.getProperty("os.arch").equals("x86_64");
  }

  public static boolean isMacOsx32() {
    return System.getProperty("os.name").equals("Mac OS X")  && System.getProperty("os.arch").equals("i386");
  }

  public static boolean isMacOsx64() {
    return System.getProperty("os.name").equals("Mac OS X")  && System.getProperty("os.arch").equals("x86_64");
  }
  
  public static boolean isLinuxX86() {
    return false;
  }

  // TODO implement properly
  public static boolean isWindowsX86() {
    return isWindows();
  }
}
