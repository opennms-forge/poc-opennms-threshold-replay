package org.opennms.forge.thresholdreplay;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Instant;

/**
 *
 * @author tak
 */
public class ThresholdOccur {

    private Instant exeeded;
    private Instant rearmed;
    private Object thresholdDefinition;

    public ThresholdOccur(Instant exeeded, Object thresholdDefinition) {
        this.exeeded = exeeded;
        this.thresholdDefinition = thresholdDefinition;
    }

    public void setRearmed(Instant rearmed) {
        this.rearmed = rearmed;
    }

    public Instant getExeeded() {
        return exeeded;
    }

    public Instant getRearmed() {
        return rearmed;
    }

    public Duration getDuration() {
        Duration duration = new Duration(0);
        if (rearmed != null) { 
            duration = new Duration(exeeded, rearmed);
        }
        return duration;
    }

    public Object getThresholdDefinition() {
        return thresholdDefinition;
    }
}
