/**
 * Copyright (c) 2002-2007 Yuri Schimke. All Rights Reserved.
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package com.baulsupp.kolja.ansi.reports.script;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.baulsupp.kolja.ansi.reports.BaseTextReport;
import com.baulsupp.kolja.ansi.reports.MementoReport;
import com.baulsupp.kolja.util.Mergeable;

/**
 * @author Yuri Schimke
 * 
 */
public class ScriptReport extends BaseTextReport<ScriptReport> implements Cloneable, MementoReport<List<Object>> {
  private static final long serialVersionUID = 1L;

  @SuppressWarnings("unchecked")
  @Override
  public void merge(ScriptReport partReport) throws Exception {
    for (Class<?> c = this.getClass(); c != Object.class; c = c.getSuperclass()) {
      for (Field field : c.getDeclaredFields()) {
        if (isPersisted(field)) {
          field.setAccessible(true);
          Object thisValue = field.get(this);
          Object partValue = field.get(partReport);

          if (thisValue instanceof Mergeable) {
            ((Mergeable) thisValue).merge((Mergeable) partValue);
          } else {
            synchronized (this) {
              Object mergedValue = merge(thisValue, partValue);
              if (mergedValue != thisValue) {
                field.set(this, mergedValue);
              }
            }
          }
        }
      }
    }
  }

  @SuppressWarnings("unchecked")
  private Object merge(Object thisValue, Object partValue) throws Exception {
    if (thisValue instanceof Integer) {
      return ((Integer) thisValue).intValue() + ((Integer) partValue).intValue();
    } else if (thisValue instanceof Long) {
      return ((Long) thisValue).longValue() + ((Long) partValue).longValue();
    } else if (thisValue instanceof Double) {
      return ((Double) thisValue).doubleValue() + ((Double) partValue).doubleValue();
    } else if (thisValue instanceof Float) {
      return ((Float) thisValue).floatValue() + ((Float) partValue).floatValue();
    }

    return thisValue;
  }

  @Override
  public ScriptReport newInstance() {
    try {
      ScriptReport report = (ScriptReport) clone();

      for (Class<?> c = report.getClass(); c != Object.class; c = c.getSuperclass()) {
        for (Field field : c.getDeclaredFields()) {
          if (isMergeable(field)) {
            field.setAccessible(true);
            Object newValue = ((Mergeable<?>) field.get(report)).newInstance();
            field.set(report, newValue);
          }
        }
      }

      return report;
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public List<Object> getMemento() throws Exception {
    List<Object> result = new ArrayList<Object>();
    for (Class<?> c = getClass(); c != Object.class; c = c.getSuperclass()) {
      for (Field field : c.getDeclaredFields()) {
        if (isPersisted(field)) {
          field.setAccessible(true);
          Object value = field.get(this);
          result.add(value);
        }
      }
    }

    return result;
  }

  public void setMemento(List<Object> memento) throws Exception {
    Iterator<Object> i = memento.iterator();

    for (Class<?> c = getClass(); c != ScriptReport.class; c = c.getSuperclass()) {
      for (Field field : c.getDeclaredFields()) {
        if (isPersisted(field)) {
          field.setAccessible(true);
          field.set(this, i.next());
        }
      }
    }
  }

  private boolean isMergeable(Field field) {
    return field.getType().isAssignableFrom(Mergeable.class) && isPersisted(field);
  }

  private boolean isPersisted(Field field) {
    return !is(field, Modifier.TRANSIENT) && !is(field, Modifier.STATIC);
  }

  private boolean is(Field field, int flag) {
    return (field.getModifiers() & flag) != 0;
  }
}
