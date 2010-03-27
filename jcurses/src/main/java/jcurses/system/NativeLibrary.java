package jcurses.system;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class NativeLibrary {
  public static void load() {
    String library = getLibraryName();

    if (library != null) {
      try {
        File dir = getTempLibraryLocation();
        File f = extractLibrary(library, dir);

        if (f != null) {
          System.load(f.getAbsolutePath());
          return;
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    System.loadLibrary("jcurses");
  }

  private static File extractLibrary(String library, File dir) throws IOException {
    InputStream is = Toolkit.class.getResourceAsStream(library);

    if (is == null) {
      System.err.println("no native library for platform '" + library + "'");
      return null;
    }

    String libraryFileName = library.substring(library.lastIndexOf('/'));
    File f = new File(dir, libraryFileName);

    if (!f.exists()) {
      FileOutputStream fos = new FileOutputStream(f);

      try {
        byte[] buffy = new byte[1024];
        int read = 0;
        while ((read = is.read(buffy)) >= 0) {
          fos.write(buffy, 0, read);
        }
      } finally {
        fos.close();
      }
    }

    return f;
  }

  private static String getLibraryName() {
    String library = null;
    if (PlatformUtil.isWindowsX86()) {
      library = "/META-INF/windows-x86/libjcurses.dll";
    } else if (PlatformUtil.isMacOsx64()) {
        library = "/META-INF/osx-64/libjcurses.so";
    } else if (PlatformUtil.isMacOsxPpc()) {
      library = "/META-INF/osx-ppc/libjcurses.jnilib";
    } else if (PlatformUtil.isLinuxX86()) {
      library = "/META-INF/linux-x86/libjcurses.so";
    }
    return library;
  }

  private static File getTempLibraryLocation() {
    if (System.getProperty("jcurses.lib.dir") != null) {
      return new File(System.getProperty("jcurses.lib.dir"));
    } else {
      return new File(System.getProperty("java.io.tmpdir"));
    }
  }
}
