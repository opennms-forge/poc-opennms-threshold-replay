package org.opennms.forge.thresholdreplay;

import org.joda.time.Instant;
import org.joda.time.Period;

/**
 *
 * @author tak
 */
public class ThresholdOccur {

    private Instant exceeded;
    private Instant rearmed;
    private Object thresholdDefinition;

    public ThresholdOccur(Instant exceeded, Object thresholdDefinition) {
        this.exceeded = exceeded;
        this.thresholdDefinition = thresholdDefinition;
    }

    public void setRearmed(Instant rearmed) {
        this.rearmed = rearmed;
    }

    public Instant getExceeded() {
        return exceeded;
    }

    public Instant getRearmed() {
        return rearmed;
    }

    public Period getPeriod() {
        Period period = new Period(0);
        if (rearmed != null) { 
            period = new Period(exceeded, rearmed);
        }
        return period;
    }

    public Object getThresholdDefinition() {
        return thresholdDefinition;
    }
}
