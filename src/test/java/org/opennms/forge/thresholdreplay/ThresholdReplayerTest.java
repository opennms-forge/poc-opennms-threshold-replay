package org.opennms.forge.thresholdreplay;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tak
 */
public class ThresholdReplayerTest {

    private ThresholdReplayer thresholdReplayer;
    private String startDate;
    private String endDate;
    private Integer desiredResolution;
    private String rrdName;
    private String nodeId;
    private String rrdBasePath;
    private String outPath;
    private String thresholdType;
    private Double thresholdValue;
    private Double thresholdRearm;
    private Integer thresholdTrigger;

    public ThresholdReplayerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGenerateThresholdOverlayPNG() {
        System.out.println("generateThresholdOverlayPNG");
        List<String> nodeIds = new ArrayList<String>();
        nodeIds.add("1");
        startDate = "2013-04-01";
        endDate = "2013-04-02";
        desiredResolution = 300;
        rrdName = "SysInterrupts";
        thresholdType = "high";
//        thresholdType = "absoluteChange";
//        thresholdType = "relativeChange";
        thresholdValue = 1000.0;
        thresholdRearm = 000.0;
        thresholdTrigger = 1;
        rrdBasePath = "/opt/opennms/share/rrd/snmp/";
        outPath = "/tmp/tr/out/";

//        File[] nodeIdFolders = new File(rrdBasePath).listFiles();
//        for (File nodeIdFolder : nodeIdFolders) {
//            if (nodeIdFolder.isDirectory()) {
//                nodeIds.add(nodeIdFolder.getName());
//            }
//        }

        for (String nodeId : nodeIds) {

            thresholdReplayer = new ThresholdReplayer(startDate, endDate, 300, rrdName,
                    nodeId, thresholdType, thresholdValue, thresholdRearm,
                    thresholdTrigger, rrdBasePath, outPath);
            thresholdReplayer.generateThresholdOverlayPNG();
        }
    }
}