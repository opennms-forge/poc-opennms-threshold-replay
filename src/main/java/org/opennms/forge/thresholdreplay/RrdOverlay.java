package org.opennms.forge.thresholdreplay;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jrobin.core.FetchData;
import org.jrobin.core.RrdDb;

public class RrdOverlay {

    private RrdDb rrdDb;
    private long[] timestamps;
    private double[] values;
    private Integer stepIndex = 0;
    private Long stepSize;
        
    public boolean initRrdMeasurmentOverlay(File jrb, String dsName, long startTimestamp, long endTimestamp) {
        boolean hasWorked = false;
        try {
            if (jrb.exists() && jrb.canRead()) {
            rrdDb = new RrdDb(jrb.getAbsoluteFile(), true);
            FetchData fetchData = rrdDb.createFetchRequest("AVERAGE", startTimestamp, endTimestamp).fetchData();
            timestamps = fetchData.getTimestamps();
            values = fetchData.getValues(dsName);
            hasWorked = true;
            
            stepSize = timestamps[1] - timestamps[0];
            } else {
                hasWorked = false; 
            }
        } catch (IOException ex) {
            Logger.getLogger(ThresholdReplayer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (org.jrobin.core.RrdException ex) {
            Logger.getLogger(ThresholdReplayer.class.getName()).log(Level.SEVERE, null, ex);
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
