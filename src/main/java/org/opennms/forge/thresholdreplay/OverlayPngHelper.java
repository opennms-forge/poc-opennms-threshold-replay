package org.opennms.forge.thresholdreplay;

import org.apache.commons.io.IOUtils;
import org.joda.time.Instant;
import org.opennms.netmgt.rrd.RrdException;
import org.opennms.netmgt.rrd.RrdUtils;

import java.io.*;
import java.util.List;

public class OverlayPngHelper {

    public static File createPng(Instant start, Instant end, File initialJrbFile, String nodeId, ThresholdReplay thresholdReplay) {
        File graphPng = new File("/tmp/");

        try {
            IOUtils.copy(new FileInputStream(initialJrbFile), new FileOutputStream("/tmp/tcpCurrEstab.jrb"));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        File overlayJrb = new File("/tmp/tcpCurrEstab.jrb");

        editOverlayRrdFile(thresholdReplay.getThresholdOccurs(), overlayJrb);

        String graphCommand = generateOverlayGraphCommand(start.getMillis() / 1000 + "", end.getMillis() / 1000 + "", thresholdReplay.getThresholdConfiguration().getDataSourceName(), nodeId, thresholdReplay.getThresholdConfiguration(), initialJrbFile, overlayJrb);
        try {
            InputStream graph = RrdUtils.createGraph(graphCommand, graphPng);
            IOUtils.copy(graph, new FileOutputStream("/tmp/graph.png"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RrdException e) {
            e.printStackTrace();
        }

        return new File("/tmp/graph.png");
    }

    private static void editOverlayRrdFile(List<ThresholdOccur> thresholdOccurs, File overlayJrb) {
        for(ThresholdOccur thresholdOccur : thresholdOccurs) {
            try {
                RrdUtils.updateRRD("Owner", overlayJrb.getParent(), overlayJrb.getName().replace(".jrb", ""), thresholdOccur.getExceeded().getMillis(), "NaN");
            } catch (RrdException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    private static String generateOverlayGraphCommand(String startTimestamp, String endTimestamp, String rrdName, String nodeId, ThresholdConfiguration thConf, File jrb, File jrbOverlay) {
        String overlayGraphCommand = "--start=" + startTimestamp + " --end=" + endTimestamp
                + " --title=\"Metric/Overlay=" + rrdName + " NodeId=" + nodeId
                + " Th-Type=" + thConf.getThresholdType() + " Th-Value=" + thConf.getThresholdValue() + " Th-Rearm=" + thConf.getThresholdRearm() + " Th-Trigger=" + thConf.getThresholdTrigger() + "\" "
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
        return overlayGraphCommand;
    }

    private static String generateSimpleGraphCommand(String startTimestamp, String endTimestamp, String rrdName, String nodeId, ThresholdConfiguration thConf, File jrb) {
        String simpleGraphCommand = "--start=" + startTimestamp + " --end=" + endTimestamp
                + " --title=\"Metric/Overlay=" + rrdName + " NodeId=" + nodeId
                + " Th-Type=" + thConf.getThresholdType() + " Th-Value=" + thConf.getThresholdValue() + " Th-Rearm=" + thConf.getThresholdRearm() + " Th-Trigger=" + thConf.getThresholdTrigger() + "\" "
                + "--width=1200 --height=600 "
                + "--vertical-label=\"Metric\" "
                + "DEF:metric=" + jrb.getAbsolutePath() + ":" + rrdName + ":AVERAGE "
                + "AREA:metric#729fcf: "
                + "LINE2:metric#3465a4:\"(Metric)\" "
                + "GPRINT:metric:AVERAGE:\"Avg Metric  \\: %8.2lf %s\" "
                + "GPRINT:metric:MIN:\"Min Metric  \\: %8.2lf %s\" "
                + "GPRINT:metric:MAX:\"Max Metric  \\: %8.2lf %s\" \n ";
        return simpleGraphCommand;
    }

}
