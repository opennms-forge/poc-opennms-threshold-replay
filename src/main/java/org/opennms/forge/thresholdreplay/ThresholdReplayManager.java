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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.opennms.netmgt.config.ThresholdingConfigFactory;
import org.opennms.netmgt.config.threshd.Basethresholddef;
import org.opennms.netmgt.config.threshd.Expression;
import org.opennms.netmgt.config.threshd.Threshold;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tak
 */
public class ThresholdReplayManager {
    private static Logger logger = LoggerFactory.getLogger(ThresholdReplayManager.class);

    public static void run(Instant start, Instant end, String opennmsHome, String nodeId) throws MarshalException, ValidationException, FileNotFoundException {
        File thresholdsXml = new File(opennmsHome + "etc/thresholds.xml");
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
                    logger.info("Expression based thresholds are not supported for now " + basethresholddef.getDescription());
                } else {
                    logger.warn("Unsupported Basethresholddef for " + basethresholddef.getDescription());
                }
            }
        }

        List<String> nodes = new ArrayList<String>();
        nodes.add(nodeId);
        JrbTimeSeriesProvider timeSeriesProvider = new JrbTimeSeriesProvider();
        ThresholdReplayCampain thresholdReplayCampain = new ThresholdReplayCampain(nodes, start, end, thresholdConfigurations, timeSeriesProvider);
        thresholdReplayCampain = ThresholdReplayer.run(thresholdReplayCampain);
      
    }
}
