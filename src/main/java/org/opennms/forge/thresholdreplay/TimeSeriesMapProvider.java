package org.opennms.forge.thresholdreplay;

import java.util.Map;
import org.joda.time.Instant;

/**
 *
 * @author tak
 */
interface TimeSeriesMapProvider {
    public Map<Instant, Double> getTimeSeriesMap();
}
