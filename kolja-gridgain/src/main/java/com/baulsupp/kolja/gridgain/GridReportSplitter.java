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
package com.baulsupp.kolja.gridgain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.gridgain.grid.GridException;
import org.gridgain.grid.GridJob;
import org.gridgain.grid.GridJobResult;
import org.gridgain.grid.GridTaskSplitAdapter;

import com.baulsupp.kolja.ansi.reports.TextReport;

/**
 * @author Yuri Schimke
 * 
 */
public class GridReportSplitter extends GridTaskSplitAdapter<ReportBatch, List<TextReport<?>>> {
  private static final long serialVersionUID = -9046190661906193862L;
  private List<TextReport<?>> reports;
  private FileDivider fileDivider = new DefaultFileDivider();

  public void setFileDivider(FileDivider fileDivider) {
    this.fileDivider = fileDivider;
  }

  public void setReports(List<TextReport<?>> reports) {
    this.reports = reports;
  }

  @Override
  protected Collection<? extends GridJob> split(int gridSize, ReportBatch job) throws GridException {
    List<GridReportJob> result = new ArrayList<GridReportJob>();

    reports = job.getReportsCopy();

    List<FileSection> sections = fileDivider.split(job.getFiles());

    for (FileSection fileSection : sections) {
      result.add(new GridReportJob(job.getFormat(), fileSection.getFile(), job.getReportsCopy(), fileSection.getIntRange()));
    }

    return result;
  }

  @SuppressWarnings("unchecked")
  public List<TextReport<?>> reduce(List<GridJobResult> parts) throws GridException {
    for (GridJobResult gridJobResult : parts) {
      List<TextReport<?>> partReports = gridJobResult.getData();

      for (int i = 0; i < reports.size(); i++) {
        TextReport finalReport = reports.get(i);
        TextReport partReport = partReports.get(i);

        finalReport.merge(partReport);
      }
    }

    return reports;
  }
}
