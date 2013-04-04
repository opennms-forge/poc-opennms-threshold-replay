/**
 * *****************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2012 The OpenNMS Group, Inc. OpenNMS(R) is Copyright (C) 1999-2012 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with OpenNMS(R). If not, see:
 * http://www.gnu.org/licenses/
 *
 * For more information contact: OpenNMS(R) Licensing <license@opennms.org> http://www.opennms.org/ http://www.opennms.com/
 * *****************************************************************************
 */
package org.opennms.forge.thresholdreplay;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import org.opennms.netmgt.rrd.RrdException;


/**
 * This starter just provides the command line parameter handling.
 * 
 * @author Markus@OpenNMS.org
 */
public class Starter {

    private static Logger logger = LoggerFactory.getLogger(Starter.class);
    
    @Option(name = "--startDate", aliases = {"-sd"}, required = true, usage = "YYYY-MM-dd")
    private static String startDate = "";
    
    @Option(name = "--endDate", aliases = {"-ed"}, required = true, usage = "YYYY-MM-dd")
    private static String endDate = "";
    
    @Option(name = "--resolution", aliases = { "-res"}, required = false, usage = "300 (desired step size)")
    private static Integer desiredResolution = 300;
    
    @Option(name = "--rrdName", aliases = {"-rrd"}, required = true, usage = "SysRawInterrupts")
    private static String rrdName = "";

    @Option(name = "--nodeId", aliases = {"-id"}, required = true, usage = "1")
    private static String nodeId = "";

    @Option(name = "--thresholdType", aliases = {"-ttype"}, required = false, usage = "high or low")
    private static String thresholdType = "high";
   
    @Option(name = "--thresholdValue", aliases = {"-tv"}, required = true, usage = "1200.0")
    private static Double thresholdValue = 1200.0;
    
    @Option(name = "--thresholdRearm", aliases = {"-tr"}, required = true, usage = "1000.0")
    private static Double thresholdRearm = 1000.0;
    
    @Option(name = "--thresholdTrigger", aliases = {"-tt"}, required = false, usage = "1")
    private static Integer thresholdTrigger = 1;
       
    @Option(name = "--rrdBasePath", aliases = {"-in"}, required = false, usage = "/tmp/threshold-replay/input/")
    private static String rrdBasePath = "/tmp/threshold-replay/input/";
    
    @Option(name = "--outPath", aliases = {"-out"}, required = false, usage = "/tmp/threshold-replay/out/")
    private static String outPath = "/tmp/out/threshold-replay/";
    
    /**
     * Set maximal terminal width for line breaks
     */
    private final int TERMINAL_WIDTH = 120;

    public static void main(String[] args) throws IOException {
        new Starter().doMain(args);
    }

    public void doMain(String[] args) {

        CmdLineParser parser = new CmdLineParser(this);
        parser.setUsageWidth(TERMINAL_WIDTH);

        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            parser.printUsage(System.err);
            System.exit(1);
        }

        logger.info("OpenNMS Threshold Replay");

        ThresholdReplayer tr = new ThresholdReplayer(startDate, endDate, desiredResolution, rrdName, nodeId, thresholdType, thresholdValue, thresholdRearm, thresholdTrigger, rrdBasePath, outPath);
        tr.generateThresholdOverlayPNG();
        
        logger.info("Thanks for computing with OpenNMS!");
    }
}