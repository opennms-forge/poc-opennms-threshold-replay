/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opennms.forge.thresholdreplay;

import java.io.File;
import org.odftoolkit.odfdom.doc.OdfSpreadsheetDocument;
import org.odftoolkit.odfdom.doc.table.OdfTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tak
 */
public class ThresholdOccursToSpreadsheet {

    private static Logger logger = LoggerFactory.getLogger(ThresholdOccursToSpreadsheet.class);

    public void go() {
        try {
            File odsOutFile = new File("/tmp/threshold.ods");
            OdfSpreadsheetDocument spreadsheet = OdfSpreadsheetDocument.newSpreadsheetDocument();
            OdfTable thresholdOccurs = OdfTable.newTable(spreadsheet);
            thresholdOccurs.setTableName("Darlek are ....");
            spreadsheet.save(odsOutFile);
        } catch (Exception ex) {
            logger.error("Sorry", ex);
        }

    }
}
