package org.opennms.forge.thresholdreplay;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author tak
 */
public class ThresholdReplayerTest {

    private JohnDo thresholdReplayer;
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
    public void testThCorrentTcpConnectionAc100() {
        List<String> nodeIds = new ArrayList<String>();
//        nodeIds.add("440");
        nodeIds.add("441");
        startDate = "2013-04-02";
        endDate = "2013-04-05";
        desiredResolution = 120;
        rrdName = "tcpCurrEstab";
        thresholdType = "absoluteChange";
        thresholdValue = 100.0;
        thresholdRearm = 0.0;
        thresholdTrigger = 1;
        rrdBasePath = "/home/tak/Desktop/BD/rrds/";
        outPath = "/tmp/tr/out/";

        for (String nodeId : nodeIds) {
            thresholdReplayer = new JohnDo(startDate, endDate, 300, rrdName,
                    nodeId, thresholdType, thresholdValue, thresholdRearm,
                    thresholdTrigger, rrdBasePath, outPath);
            thresholdReplayer.generateThresholdOverlayPNG();
        }
    }

    @Test
    public void testThCorrentTcpConnectionAc400() {
        List<String> nodeIds = new ArrayList<String>();
//        nodeIds.add("440");
        nodeIds.add("441");
        startDate = "2013-04-02";
        endDate = "2013-04-05";
        desiredResolution = 120;
        rrdName = "tcpCurrEstab";
        thresholdType = "absoluteChange";
        thresholdValue = 400.0;
        thresholdRearm = 0.0;
        thresholdTrigger = 1;
        rrdBasePath = "/home/tak/Desktop/BD/rrds/";
        outPath = "/tmp/tr/out/";

        for (String nodeId : nodeIds) {
            thresholdReplayer = new JohnDo(startDate, endDate, 300, rrdName,
                    nodeId, thresholdType, thresholdValue, thresholdRearm,
                    thresholdTrigger, rrdBasePath, outPath);
            thresholdReplayer.generateThresholdOverlayPNG();
        }
    }
    
    @Test
    public void testThCorrentTcpConnectionH1000() {
        List<String> nodeIds = new ArrayList<String>();
        nodeIds.add("441");
        startDate = "2013-04-02";
        endDate = "2013-04-05";
        desiredResolution = 120;
        rrdName = "tcpCurrEstab";
        thresholdType = "high";
        thresholdValue = 1000.0;
        thresholdRearm = 700.0;
        thresholdTrigger = 3;
        rrdBasePath = "/home/tak/Desktop/BD/rrds/";
        outPath = "/tmp/tr/out/";

        for (String nodeId : nodeIds) {
            thresholdReplayer = new JohnDo(startDate, endDate, 300, rrdName,
                    nodeId, thresholdType, thresholdValue, thresholdRearm,
                    thresholdTrigger, rrdBasePath, outPath);
            thresholdReplayer.generateThresholdOverlayPNG();
        }
    }
    
    @Test
    public void testThCorrentTcpConnectionH400() {
        List<String> nodeIds = new ArrayList<String>();
        nodeIds.add("441");
        startDate = "2013-04-02";
        endDate = "2013-04-05";
        desiredResolution = 120;
        rrdName = "tcpCurrEstab";
        thresholdType = "high";
        thresholdValue = 400.0;
        thresholdRearm = 350.0;
        thresholdTrigger = 3;
        rrdBasePath = "/home/tak/Desktop/BD/rrds/";
        outPath = "/tmp/tr/out/";

        for (String nodeId : nodeIds) {
            thresholdReplayer = new JohnDo(startDate, endDate, 300, rrdName,
                    nodeId, thresholdType, thresholdValue, thresholdRearm,
                    thresholdTrigger, rrdBasePath, outPath);
            thresholdReplayer.generateThresholdOverlayPNG();
        }
    }

    @Test
    public void testThCorrentTcpConnectionH250() {
        List<String> nodeIds = new ArrayList<String>();
        nodeIds.add("441");
        startDate = "2013-04-02";
        endDate = "2013-04-05";
        desiredResolution = 300;
        rrdName = "tcpCurrEstab";
        thresholdType = "high";
        thresholdValue = 250.0;
        thresholdRearm = 235.0;
        thresholdTrigger = 3;
        rrdBasePath = "/home/tak/Desktop/BD/rrds/";
        outPath = "/tmp/tr/out/";

        for (String nodeId : nodeIds) {
            thresholdReplayer = new JohnDo(startDate, endDate, 300, rrdName,
                    nodeId, thresholdType, thresholdValue, thresholdRearm,
                    thresholdTrigger, rrdBasePath, outPath);
            thresholdReplayer.generateThresholdOverlayPNG();
        }
    }
    
    
    @Test
    public void testGenerateThresholdOverlayPNG() {
        System.out.println("generateThresholdOverlayPNG");
        List<String> nodeIds = new ArrayList<String>();
        nodeIds.add("441");
        startDate = "2013-04-01";
        endDate = "2013-04-02";
        desiredResolution = 300;
        rrdName = "tcpCurrEstab";
        thresholdType = "high";
//        thresholdType = "absoluteChange";
//        thresholdType = "relativeChange";
        thresholdValue = 1000.0;
        thresholdRearm = 800.0;
        thresholdTrigger = 1;
        rrdBasePath = "/home/tak/Desktop/BD/rrds/";
//        rrdBasePath = "/opt/opennms/share/rrd/snmp/";
        outPath = "/tmp/tr/out/";

//        File[] nodeIdFolders = new File(rrdBasePath).listFiles();
//        for (File nodeIdFolder : nodeIdFolders) {
//            if (nodeIdFolder.isDirectory()) {
//                nodeIds.add(nodeIdFolder.getName());
//            }
//        }

        for (String nodeId : nodeIds) {

            thresholdReplayer = new JohnDo(startDate, endDate, 300, rrdName,
                    nodeId, thresholdType, thresholdValue, thresholdRearm,
                    thresholdTrigger, rrdBasePath, outPath);
            thresholdReplayer.generateThresholdOverlayPNG();
        }
    }
}