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
package com.baulsupp.kolja.ansi.reports.ruby;

import org.jruby.Ruby;

import com.baulsupp.kolja.ansi.reports.BaseTextReport;
import com.baulsupp.kolja.ansi.reports.MementoReport;

/**
 * @author Yuri Schimke
 * 
 */
public class RbReport extends BaseTextReport<RbReport> implements Cloneable, MementoReport<Object> {
  private static final long serialVersionUID = 1L;
  @SuppressWarnings("unused")
  private Ruby runtime;

  public void setRuntime(Ruby runtime) {
    this.runtime = runtime;
  }

  public Object getMemento() throws Exception {
    throw new UnsupportedOperationException();
  }

  public void setMemento(Object memento) throws Exception {
    throw new UnsupportedOperationException();
  }

  @Override
  public void merge(RbReport partReport) throws Exception {
    throw new UnsupportedOperationException();
  }
}
