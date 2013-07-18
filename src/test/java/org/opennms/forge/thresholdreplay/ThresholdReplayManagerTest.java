package org.opennms.forge.thresholdreplay;

import org.joda.time.DateTime;
import org.joda.time.Instant;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tak
 */
public class ThresholdReplayManagerTest {

    private ThresholdReplayManager thresholdReplayManager;
    
    
    @Test
    public void testRun() {
        //TODO doing it wrong
        Instant start = new Instant(new DateTime(2013, 4, 1, 0, 0, 0).getMillis());
        Instant end = new Instant(new DateTime(2013, 4, 3, 0, 0, 0).getMillis());
        
//        ThresholdReplayManager.run(start, end, "/opt/opennms/", "1");

    }
}