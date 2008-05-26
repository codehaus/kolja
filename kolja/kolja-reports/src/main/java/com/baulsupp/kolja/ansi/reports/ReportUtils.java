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
package com.baulsupp.kolja.ansi.reports;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yuri Schimke
 * 
 */
public class ReportUtils {

  public static List<TextReport<?>> getReportsCopy(List<TextReport<?>> reports) {
    ArrayList<TextReport<?>> copy = new ArrayList<TextReport<?>>(reports.size());

    for (TextReport<?> textReport : reports) {
      copy.add(textReport.newInstance());
    }

    return copy;
  }

  @SuppressWarnings("unchecked")
  public static void mergeReports(List<TextReport<?>> finalReports, List<TextReport<?>> partReports) {
    for (int i = 0; i < finalReports.size(); i++) {
      TextReport finalReport = finalReports.get(i);
      TextReport partReport = partReports.get(i);

      finalReport.merge(partReport);
    }
  }

}
