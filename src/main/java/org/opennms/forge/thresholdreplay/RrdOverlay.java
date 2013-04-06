package org.opennms.forge.thresholdreplay;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import org.jrobin.core.FetchData;
import org.jrobin.core.RrdDb;
import org.joda.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RrdOverlay extends LinkedHashMap<Instant, Double> {

    private static Logger logger = LoggerFactory.getLogger(ThresholdReplayer.class);
    private RrdDb rrdDb;
    private Map<Instant, Double> rrdMap = new LinkedHashMap<Instant, Double>();
    
    private long[] timestamps;
    private double[] values;
    
    private Integer stepIndex = 0;
    private Long stepSize;
            
    public boolean initRrdMeasurmentOverlay(File jrb, String dsName, long startTimestamp, long endTimestamp, Integer desiredResolution) {
        boolean hasWorked = false;
        try {
            if (jrb.exists() && jrb.canRead()) {
            rrdDb = new RrdDb(jrb.getAbsoluteFile(), true);
            FetchData fetchData = rrdDb.createFetchRequest("AVERAGE", startTimestamp, endTimestamp, desiredResolution).fetchData();
            timestamps = fetchData.getTimestamps();
            values = fetchData.getValues(dsName);            
            for (int i = 0; i < timestamps.length; i++) {
                //Rrd is using secondes instead of millisecondes so we multiply
                rrdMap.put(new Instant(timestamps[i]*1000), values[i]);
            }
            hasWorked = true;
            stepSize = desiredResolution.longValue();
            } else {
                hasWorked = false; 
            }
        } catch (IOException ex) {
            logger.error("Sorry", ex);
        } catch (org.jrobin.core.RrdException ex) {
            logger.error("Sorry", ex);
        }
        return hasWorked;
    }
    
    public double getValue() {
        return values[stepIndex];
    }

    public Integer nextStep() {
        stepIndex++;
        if (stepIndex > timestamps.length) {
            return -1;
        }
        return stepIndex;
    }

    public boolean hasNextStep() {
        if (stepIndex == timestamps.length) {
            return false;
        }
        return true;
    }

    public Instant getTimeStamp() {
        return new Instant(timestamps[stepIndex]);
    }
    
    public Map<Instant, Double> getRrdMap() {
        return rrdMap;
    }

    public Long getStepSize() {
        return stepSize;
    }
}
