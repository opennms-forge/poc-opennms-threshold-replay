package org.opennms.forge.thresholdreplay;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tak
 */
public class ThresholdReplay {
    private static Logger logger = LoggerFactory.getLogger(ThresholdReplay.class);
    private final ThresholdConfiguration thresholdConfiguration;
    private final List<ThresholdOccur> thresholdOccurs;

    public ThresholdReplay(ThresholdConfiguration thresholdConfiguration, List<ThresholdOccur> thresholdOccurs) {
        this.thresholdConfiguration = thresholdConfiguration;
        this.thresholdOccurs = thresholdOccurs;
    }

    public ThresholdConfiguration getThresholdConfiguration() {
        return thresholdConfiguration;
    }

    public List<ThresholdOccur> getThresholdOccurs() {
        return thresholdOccurs;
    }
    
}
