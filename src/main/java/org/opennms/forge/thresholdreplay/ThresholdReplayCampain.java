package org.opennms.forge.thresholdreplay;

import java.util.LinkedList;
import java.util.List;
import org.joda.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tak
 */
public class ThresholdReplayCampain {
    
    private static Logger logger = LoggerFactory.getLogger(ThresholdReplayCampain.class);
    private final String nodeId;
    private final Instant start;
    private final Instant end;
    private List<ThresholdReplay> thresholdReplays = new LinkedList<ThresholdReplay>();

    public ThresholdReplayCampain(String nodeId, Instant start, Instant end) {
        this.nodeId = nodeId;
        this.start = start;
        this.end = end;
    }

    public List<ThresholdReplay> getThresholdReplays() {
        return thresholdReplays;
    }

    public void setThresholdReplays(List<ThresholdReplay> thresholdReplays) {
        this.thresholdReplays = thresholdReplays;
    }

    public static Logger getLogger() {
        return logger;
    }

    public String getNodeId() {
        return nodeId;
    }

    public Instant getStart() {
        return start;
    }

    public Instant getEnd() {
        return end;
    }
}
