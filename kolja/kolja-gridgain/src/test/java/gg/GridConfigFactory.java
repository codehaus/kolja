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
package gg;

import org.apache.commons.logging.LogFactory;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.gridgain.grid.GridConfigurationAdapter;
import org.gridgain.grid.logger.GridLogger;
import org.gridgain.grid.logger.jcl.GridJclLogger;
import org.gridgain.grid.spi.checkpoint.sharedfs.GridSharedFsCheckpointSpi;

/**
 * @author Yuri Schimke
 * 
 */
public class GridConfigFactory {

  public static GridConfigurationAdapter getConfiguration() {
    GridConfigurationAdapter conf = new GridConfigurationAdapter();
    conf.setGridGainHome("C:\\java\\gridgain-2.0.2");
    BasicConfigurator.configure();
    Logger.getRootLogger().setLevel(Level.WARN);
    GridLogger gridLogger = new GridJclLogger(LogFactory.getLog("grid"));
    conf.setGridLogger(gridLogger);
    GridSharedFsCheckpointSpi cpSpi = new GridSharedFsCheckpointSpi();
    cpSpi.setDirectoryPath("C:\\java\\gridgain-work");
    conf.setCheckpointSpi(cpSpi);
    return conf;
  }
}
