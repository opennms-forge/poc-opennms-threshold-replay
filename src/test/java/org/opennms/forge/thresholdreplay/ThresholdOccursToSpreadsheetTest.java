package org.opennms.forge.thresholdreplay;

import java.io.File;
import java.util.LinkedList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tak
 */
public class ThresholdOccursToSpreadsheetTest {

    @Test
    public void testGo() {
        System.out.println("go");
        ThresholdOccursToSpreadsheet instance = new ThresholdOccursToSpreadsheet();
        instance.go(new File("/tmp/"), new LinkedList<ThresholdOccur>());
    }
}