/**
 * Copyright (c) 2002-2007 Yuri Schimke. All Rights Reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
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
