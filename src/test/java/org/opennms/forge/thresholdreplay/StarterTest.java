package org.opennms.forge.thresholdreplay;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.joda.time.DateTime;
import org.joda.time.Instant;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.opennms.netmgt.config.ThresholdingConfigFactory;
import org.opennms.netmgt.config.threshd.Basethresholddef;
import org.opennms.netmgt.config.threshd.Expression;
import org.opennms.netmgt.config.threshd.Threshold;

/**
 *
 * @author tak
 */
public class StarterTest {

    @Test
    public void testStart() {
        Starter starter = new Starter();

        //TODO doing it wrong
//        Instant start = new Instant(new DateTime(2013, 4, 6, 0, 0, 0).getMillis());
//        Instant end = new Instant(new DateTime(2013, 4, 8, 0, 0, 0).getMillis());
        Instant start = new Instant(new DateTime(2013, 7, 22, 11, 0, 0).getMillis());
        Instant end = new Instant(new DateTime(2013, 7, 23, 11, 0, 0).getMillis());

//        starter.start(start, end, "/opt/opennms/share/rrd/snmp/", "1", "SysRawInterrupts", "high", 1000.0, 800.0, 1, 300);
//        starter.start(start, end, "src/test/resources/sampleRRDs", "438", "tcpCurrEstab", "absoluteChange", 10.0, 0.0, 1, 120);
//        starter.start(start, end, "src/test/resources/sampleRRDs", "441", "tcpCurrEstab", "absoluteChange", 600.0, 0.0, 1, 120);
//        starter.start(start, end, "src/test/resources/sampleRRDs", "440", "tcpCurrEstab", "absoluteChange", 600.0, 0.0, 1, 120);

        List<String> nodeIds = new ArrayList<String>();
        nodeIds.add("438");
        nodeIds.add("439");
        nodeIds.add("440");
        nodeIds.add("441");

        for (String nodeId : nodeIds) {
//            starter.start(start, end, "src/test/resources/sampleRRDs", nodeId, "tcpCurrEstab", "high", 800.0, 750.0, 3, 120);
//            starter.start(start, end, "src/test/resources/sampleRRDs", nodeId, "tcpCurrEstab", "absoluteChange", 1000.0, 0.0, 2, 120);
//            starter.start(start, end, "src/test/resources/sampleRRDs", nodeId, "tcpCurrEstab", "relativeChange", 0.3, 0.0, 1, 120);
        }

        //TODO this case causes a null pointer
        //starter.start(start, end, "src/test/resources/sampleRRDs", "440666", "tcpCurrEstab", "high", 800.0, 750.0, 1, 120);

    }

    @Ignore
    @Test
    public void testReadingThresholdConfig() throws FileNotFoundException, MarshalException, ValidationException {
//        File thresholdsXml = new File("/opt/opennms/etc/thresholds.xml");
        File thresholdsXml = new File("/home/tak/dev/opennms-and-friends/threshold-config/thresholds.xml");
        assertTrue(thresholdsXml.exists());

        List<ThresholdConfiguration> thresholdConfigurations = new LinkedList<ThresholdConfiguration>();
        ThresholdConfiguration thresholdConfiguration = null;

        ThresholdingConfigFactory thCoFa = new ThresholdingConfigFactory(new FileInputStream(thresholdsXml));
        Collection<String> groupNames = thCoFa.getGroupNames();
        for (String groupName : groupNames) {
            System.out.println(groupName);
            Collection<Basethresholddef> thresholds = thCoFa.getThresholds(groupName);
            for (Basethresholddef basethresholddef : thresholds) {
                if (basethresholddef instanceof Threshold) {
                    Threshold threshold = (Threshold) basethresholddef;
                    thresholdConfiguration = new ThresholdConfiguration(groupName, threshold.getDsName(), threshold.getType(), threshold.getValue(), threshold.getRearm(), threshold.getTrigger());
                    thresholdConfigurations.add(thresholdConfiguration);
                    System.out.println(thresholdConfiguration.toFormatedString());
                } else if (basethresholddef instanceof Expression) {
                    System.out.println("Expression based thresholds are not supported for now " + basethresholddef.getDescription());
                } else {
                    System.out.println("Unsupported Basethresholddef for " + basethresholddef.getDescription());
                }
            }
        }
        assertEquals(99, thCoFa.getGroupNames().size());
        Instant start = new Instant(new DateTime(2013, 4, 1, 0, 0, 0).getMillis());
        Instant end = new Instant(new DateTime(2013, 4, 3, 0, 0, 0).getMillis());
        List<String> nodes = new ArrayList<String>();
        nodes.add("441");
        JrbTimeSeriesProvider timeSeriesProvider = new JrbTimeSeriesProvider();
        ThresholdReplayCampain thresholdReplayCampain = new ThresholdReplayCampain(nodes, start, end, thresholdConfigurations, timeSeriesProvider);
        thresholdReplayCampain = ThresholdReplayer.run(thresholdReplayCampain);
    }
}