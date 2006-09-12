package com.baulsupp.kolja.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.springframework.core.io.Resource;

public class SerializationUtil {
  @SuppressWarnings("unchecked")
  public static <T> T deserialize(Class<T> clazz, Resource file) {
    try {
      InputStream fis = file.getInputStream();

      try {
        ObjectInputStream ois = new ObjectInputStream(fis);

        return (T) ois.readObject();
      } finally {
        fis.close();
      }
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static void serialize(File file, Object o) {
    try {
      FileOutputStream fos = new FileOutputStream(file);

      try {
        ObjectOutputStream oos = new ObjectOutputStream(fos);

        oos.writeObject(o);

        oos.flush();
      } finally {
        fos.close();
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
