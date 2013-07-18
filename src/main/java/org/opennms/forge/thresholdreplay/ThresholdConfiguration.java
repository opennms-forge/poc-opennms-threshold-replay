package org.opennms.forge.thresholdreplay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tak
 */
class ThresholdConfiguration {

    private static Logger logger = LoggerFactory.getLogger(ThresholdConfiguration.class);
    private final String thresholdCategory;
    
    private final String dataSourceName;
    private final String dataSourceType = "node";
    
    private final String thresholdType;
    private final double thresholdValue;
    private final double thresholdRearm;
    private final int thresholdTrigger;

    public ThresholdConfiguration(String thresholdCategory, String rrdName, String thresholdType, double thresholdValue, double thresholdRearm, int thresholdTrigger) {
        this.thresholdCategory = thresholdCategory;
        this.dataSourceName = rrdName;
        this.thresholdType = thresholdType;
        this.thresholdValue = thresholdValue;
        this.thresholdRearm = thresholdRearm;
        this.thresholdTrigger = thresholdTrigger;
    }

    public String getThresholdCategory() {
        return thresholdCategory;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public String getDataSourceType() {
        return dataSourceType;
    }

    public String getThresholdType() {
        return thresholdType;
    }

    public double getThresholdValue() {
        return thresholdValue;
    }

    public double getThresholdRearm() {
        return thresholdRearm;
    }

    public int getThresholdTrigger() {
        return thresholdTrigger;
    }

    public String toFormatedString() {
        return thresholdCategory + " :: " + dataSourceName + " :: " + dataSourceType + "\n" + thresholdType + ", value " + thresholdValue + ", rearm " + thresholdRearm + ", trigger " + thresholdTrigger;
    }
}
