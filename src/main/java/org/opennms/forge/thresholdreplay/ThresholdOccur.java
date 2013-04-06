package org.opennms.forge.thresholdreplay;

import org.joda.time.Instant;
import org.joda.time.Period;

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

    public Period getPeriod() {
        Period period = new Period(0);
        if (rearmed != null) { 
            period = new Period(exeeded, rearmed);
        }
        return period;
    }

    public Object getThresholdDefinition() {
        return thresholdDefinition;
    }
}
