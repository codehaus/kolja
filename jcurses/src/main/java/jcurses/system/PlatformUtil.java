package jcurses.system;

public class PlatformUtil {
  public static boolean isWindows() {
    return System.getProperty("os.name").toLowerCase().contains("windows");
  }

  public static boolean isMacOsx() {
    return System.getProperty("os.name").equals("Mac OS X");
  }
  
  public static boolean isLinuxX86() {
    return false;
  }

  // TODO implement properly
  public static boolean isWindowsX86() {
    return isWindows();
  }
}
