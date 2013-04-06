package org.opennms.forge.thresholdreplay;

import org.joda.time.DateTime;
import org.joda.time.Instant;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tak
 */
public class StarterTest {
    
    @Test
    public void testStart() {
        Starter starter = new Starter();
        
        //TODO doing it wrong
        Instant start = new Instant(new DateTime(2013, 4, 1, 0, 0, 0).getMillis());
        Instant end = new Instant(new DateTime(2013, 4, 3, 0, 0, 0).getMillis());
                
        starter.start(start, end, "/opt/opennms/share/rrd/snmp/", "1", "SysRawInterrupts", "high",  1000.0, 800.0, 1, 300);
    }
}