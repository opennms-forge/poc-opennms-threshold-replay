package org.opennms.forge.thresholdreplay;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.joda.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tak
 */
public class ThresholdReplayCampain {
    
    private static Logger logger = LoggerFactory.getLogger(ThresholdReplayCampain.class);
    private final List<String> nodeIds;
    private final Instant start;
    private final Instant end;
    private Map<String, List<ThresholdReplay>> thresholdReplaysByNodeIds = new HashMap<String, List<ThresholdReplay>>();
    private final TimeSeriesMapProvider timeSeriesMapProvider;
    
    public ThresholdReplayCampain(List<String> nodeIds, Instant start, Instant end, List<ThresholdConfiguration> thresholdConfigurations, TimeSeriesMapProvider timeSeriesMapProvider) {
        this.nodeIds = nodeIds;
        this.start = start;
        this.end = end;
        this.timeSeriesMapProvider = timeSeriesMapProvider;
    }

//    public List<ThresholdReplay> getThresholdReplays() {
//        return thresholdReplays;
//    }
//
//    public void setThresholdReplays(List<ThresholdReplay> thresholdReplays) {
//        this.thresholdReplays = thresholdReplays;
//    }

    public Instant getStart() {
        return start;
    }

    public Instant getEnd() {
        return end;
    }
}
