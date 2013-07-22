package org.opennms.forge.thresholdreplay;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.jrobin.core.FetchData;
import org.jrobin.core.RrdDb;
import org.joda.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//TODO Same as the JrbTimeSeriesProvider just other format for dates
public class RrdOverlay implements TimeSeriesMapProvider {

    private static Logger logger = LoggerFactory.getLogger(RrdOverlay.class);
    private RrdDb rrdDb;
    private Map<Instant, Double> timeSeriesMap = new LinkedHashMap<Instant, Double>();
    
    private long[] timestamps;
    private double[] values;

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
                    //Rrd is using seconds instead of milliseconds so we multiply
                    timeSeriesMap.put(new Instant(timestamps[i]*1000), values[i]);
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

    public Long getStepSize() {
        return stepSize;
    }

    @Override
    public Map<Instant, Double> getTimeSeriesMap() {
        return timeSeriesMap;
    }
}