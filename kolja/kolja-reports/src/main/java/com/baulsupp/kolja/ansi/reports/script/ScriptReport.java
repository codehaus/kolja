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

import com.baulsupp.kolja.ansi.reports.BaseTextReport;
import com.baulsupp.kolja.util.Mergeable;

/**
 * @author Yuri Schimke
 * 
 */
public class ScriptReport extends BaseTextReport<ScriptReport> implements Cloneable {
  private static final long serialVersionUID = 1L;

  @SuppressWarnings("unchecked")
  @Override
  public void merge(ScriptReport partReport) {
    try {
      for (Class<?> c = this.getClass(); c != Object.class; c = c.getSuperclass()) {
        for (Field field : c.getDeclaredFields()) {
          if (Mergeable.class.isAssignableFrom(field.getType())) {
            Mergeable thisValue = (Mergeable) field.get(this);
            Mergeable partValue = (Mergeable) field.get(partReport);
            thisValue.merge(partValue);
          }
        }
      }
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public ScriptReport newInstance() {
    try {
      ScriptReport report = (ScriptReport) clone();

      for (Class<?> c = report.getClass(); c != Object.class; c = c.getSuperclass()) {
        for (Field field : c.getDeclaredFields()) {
          if (Mergeable.class.isAssignableFrom(field.getType())) {
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
}
