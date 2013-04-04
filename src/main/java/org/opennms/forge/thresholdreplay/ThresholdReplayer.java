/**
 * *****************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2007-2012 The OpenNMS Group, Inc. OpenNMS(R) is Copyright (C)
 * 1999-2012 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * OpenNMS(R). If not, see: http://www.gnu.org/licenses/
 *
 * For more information contact: OpenNMS(R) Licensing <license@opennms.org>
 * http://www.opennms.org/ http://www.opennms.com/
 * *****************************************************************************
 */
package org.opennms.forge.thresholdreplay;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.opennms.netmgt.config.threshd.Threshold;
import org.opennms.netmgt.rrd.RrdDataSource;
import org.opennms.netmgt.rrd.RrdException;
import org.opennms.netmgt.rrd.RrdUtils;
import org.opennms.netmgt.threshd.ThresholdConfigWrapper;
import org.opennms.netmgt.threshd.ThresholdEvaluatorAbsoluteChange.ThresholdEvaluatorStateAbsoluteChange;
import org.opennms.netmgt.threshd.ThresholdEvaluatorHighLow.ThresholdEvaluatorStateHighLow;
import org.opennms.netmgt.threshd.ThresholdEvaluatorRelativeChange.ThresholdEvaluatorStateRelativeChange;
import org.opennms.netmgt.threshd.ThresholdEvaluatorState;
import org.opennms.netmgt.threshd.ThresholdEvaluatorState.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThresholdReplayer {

    private static Logger logger = LoggerFactory.getLogger(ThresholdReplayer.class);
    private String startDate;
    private String endDate;
    private Integer desiredResolution;
    private String rrdName;
    private String nodeId;
    private final String thresholdType;
    private final double thresholdValue;
    private final double thresholdRearm;
    private final int thresholdTrigger;
    private String rrdFileEnding = ".jrb";
    private File jrb;
    private File jrbOverlay;
    private RrdOverlay rrdOverlay = new RrdOverlay();
    private long startTimestamp;
    private long endTimestamp;
    private Map<Long, Double> overlayMap = new LinkedHashMap<Long, Double>();
    private String overlayGraphCommand = "";
    private DateTimeFormatter fmt = DateTimeFormat.forPattern("YYYY-MM-dd");

    public ThresholdReplayer(String startDate, String endDate, Integer desiredResolution, String rrdName, String nodeId, String thresholdType, double thresholdValue, double thresholdRearm, int thresholdTrigger, String rrdBasePath, String outPath) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.desiredResolution = desiredResolution;
        this.rrdName = rrdName;
        this.nodeId = nodeId;
        this.thresholdType = thresholdType;
        this.thresholdValue = thresholdValue;
        this.thresholdRearm = thresholdRearm;
        this.thresholdTrigger = thresholdTrigger;
        this.jrb = new File(rrdBasePath + "/" + nodeId + "/" + rrdName + rrdFileEnding);
        this.jrbOverlay = new File(outPath + rrdName + "/" + nodeId + "_" + rrdName + "_Overlay" + rrdFileEnding);
    }

    private long generateTimestamp(String date) {
        DateTime parseDateTime = fmt.parseDateTime(date);
        return parseDateTime.getMillis() / 1000;
    }

    private void generateOverlayGraphCommand() {
        overlayGraphCommand = "--start=" + startTimestamp + " --end=" + endTimestamp
                + " --title=\"Metric/Overlay=" + rrdName + " NodeId=" + nodeId
                + " Th-Type=" + thresholdType + " Th-Value=" + thresholdValue + " Th-Rearm=" + thresholdRearm + " Th-Trigger=" + thresholdTrigger + "\" "
                + "--width=1200 --height=600 "
                + "--vertical-label=\"Metric\" "
                + "DEF:metric=" + jrb.getAbsolutePath() + ":" + rrdName + ":AVERAGE "
                + "DEF:overlay=" + jrbOverlay.getAbsolutePath() + ":" + rrdName + ":AVERAGE "
                + "AREA:metric#729fcf: "
                + "LINE2:metric#3465a4:\"(Metric)\" "
                + "AREA:overlay#ef2929: "
                + "LINE2:overlay#cc0000:\"(Overlay)\" "
                + "GPRINT:metric:AVERAGE:\"Avg Metric  \\: %8.2lf %s\" "
                + "GPRINT:metric:MIN:\"Min Metric  \\: %8.2lf %s\" "
                + "GPRINT:metric:MAX:\"Max Metric  \\: %8.2lf %s\" \n "
                + "GPRINT:overlay:AVERAGE:\"Avg Overlay \\: %8.2lf %s\" "
                + "GPRINT:overlay:MIN:\"Min Overlay \\: %8.2lf %s\" "
                + "GPRINT:overlay:MAX:\"Max Overlay  \\: %8.2lf %s\" ";
    }

    public void generateThresholdOverlayPNG() {
        logger.error("test");
        startTimestamp = generateTimestamp(startDate);
        endTimestamp = generateTimestamp(endDate);

        System.out.println("Next :: generateRrdOverlay");
        if (generateRrdOverlay()) {

            System.out.println("Next :: runRrdOverlayToThresholder");
            runRrdOverlayToThresholder();

            if (true) {
                if (jrbOverlay.canWrite()) {
                    jrbOverlay.delete();
                }
            }

            System.out.println("Next :: createMiniOverlayRrdFile");
            createMiniOverlayRrdFile();

            System.out.println("Next :: editOverlayRrdFile");
            editOverlayRrdFile();

            System.out.println("Next :: generateOverlayGraphCommand");
            generateOverlayGraphCommand();

            System.out.println("Next :: storeGraphPNG");
            File overlayGraphPNG = new File(jrbOverlay.getAbsolutePath().replace(rrdFileEnding, ".png"));
            storeGraphPNG(overlayGraphPNG, overlayGraphCommand, jrbOverlay);

//            if(false) {
//                jrbOverlay.delete();
//            }

            System.out.println("DONE :: Thanks for computing with OpenNMS!");
        } else {
            System.out.println("No rrdOverlay possible, ignorring :: " + jrb.getAbsolutePath());
        }
//        File initialGraphPNG = new File(outPath + nodeId + "_" + rrdName + ".png");
//        storeGraphPNG(initialGraphPNG, initialGraphCommand, jrb);
    }

    private void runRrdOverlayToThresholder() {
        Threshold threshold = new Threshold();
        threshold.setType(thresholdType);
        threshold.setDsName(rrdName);
        threshold.setDsType("node");
        threshold.setValue(thresholdValue);
        threshold.setRearm(thresholdRearm);
        threshold.setTrigger(thresholdTrigger);
        ThresholdConfigWrapper wrapper = new ThresholdConfigWrapper(threshold);
        ThresholdEvaluatorState item = null;
        
        if (thresholdType.equals("high") || thresholdType.equals("low")) {
            item = new ThresholdEvaluatorStateHighLow(wrapper);
        } else 

        if (thresholdType.equals("absoluteChange")) {
            item = new ThresholdEvaluatorStateAbsoluteChange(wrapper);
        }

        if (thresholdType.equals("relativeChange")) {
            item = new ThresholdEvaluatorStateRelativeChange(wrapper);
        }

        Status status;
        if (item != null) {
            Boolean isExeeded = false;
            while (rrdOverlay.hasNextStep()) {
                status = item.evaluate(rrdOverlay.getValue());
                if (status.equals(Status.TRIGGERED)) {
                    isExeeded = true;
                    System.out.println("Threshold TRIGGERED at :: " + rrdOverlay.getTime() + "\t with :: " + rrdOverlay.getValue());
                }
                if (status.equals(Status.RE_ARMED)) {
                    isExeeded = false;
                    System.out.println("Threshold RE_ARMED  at :: " + rrdOverlay.getTime() + "\t with :: " + rrdOverlay.getValue());
                }

                if (isExeeded) {
                    overlayMap.put(rrdOverlay.getTimeStamp(), rrdOverlay.getValue());
                } else {
                    overlayMap.put(rrdOverlay.getTimeStamp(), Double.NaN);
                }
                /**
                 * Fake re_arms for absoluteChange and relativeChange to make every exeeded visible.
                 */
                if (thresholdType.equals("absoluteChange") || thresholdType.equals("relativeChange")) {
                    isExeeded = false;
                }
                rrdOverlay.nextStep();
            }
        }
    }

    private boolean generateRrdOverlay() {
        return rrdOverlay.initRrdMeasurmentOverlay(jrb, rrdName, startTimestamp, endTimestamp, desiredResolution);
    }

    private void storeGraphPNG(File graphPNG, String graphCommand, File jrbFile) {
        System.out.println("graphPNG :: " + graphPNG.getAbsolutePath());
        System.out.println("jrbFile  :: " + jrbFile.getAbsolutePath());

        InputStream createGraph = null;
        try {
            createGraph = RrdUtils.createGraph(graphCommand, jrbFile.getParentFile());
            graphPNG.getParentFile().mkdirs();
            OutputStream out = new FileOutputStream(graphPNG);
            int read;
            byte[] bytes = new byte[1024];
            while ((read = createGraph.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }

            createGraph.close();
            out.flush();
            out.close();
        } catch (Exception ex) {
            System.out.println("storeGraphPNG :: " + ex.getMessage());
        } finally {
            try {
                createGraph.close();
            } catch (IOException ex) {
                System.out.println("storeGraphPNG finally :: " + ex.getMessage());
            }
        }
    }

    private void editOverlayRrdFile() {
        for (Long timestamp : overlayMap.keySet()) {
            try {
                RrdUtils.updateRRD("Threshold-Replay", jrbOverlay.getParent(), nodeId + "_" + rrdName + "_Overlay", timestamp * 1000, overlayMap.get(timestamp).toString());
            } catch (RrdException ex) {
                System.out.println("RrdUtils.updateRRD :: " + ex.getMessage());
            }
        }
    }

    private void createMiniOverlayRrdFile() {
        List<RrdDataSource> rrdDataSourcesList = new ArrayList<RrdDataSource>();
        //TODO Counter vs Gauge
        RrdDataSource rrdDataSource = new RrdDataSource(rrdName, "GAUGE", 600, null, null);
        rrdDataSourcesList.add(rrdDataSource);
        List<String> rraList = new ArrayList<String>();
        rraList.add("RRA:AVERAGE:0.5:1:" + overlayMap.size());
        try {
            jrbOverlay.getParentFile().mkdirs();
            RrdUtils.createRRD("Threshold-Replay", jrbOverlay.getParent(), nodeId + "_" + rrdName + "_Overlay", rrdOverlay.getStepSize().intValue(), rrdDataSourcesList, rraList);
        } catch (Exception ex) {
            System.out.println("createMiniOverlayRrdFile :: " + ex.getMessage());
        }
    }
}
