package org.opennms.forge.thresholdreplay;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import org.jrobin.core.FetchData;
import org.jrobin.core.RrdDb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RrdOverlay {

    private static Logger logger = LoggerFactory.getLogger(ThresholdReplayer.class);
    private RrdDb rrdDb;
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

    public String getTime() {
        return new Date(timestamps[stepIndex]*1000).toString();
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

    public Long getTimeStamp() {
        return timestamps[stepIndex];
    }

    public Long getStepSize() {
        return stepSize;
    }
}
