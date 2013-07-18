package org.opennms.forge.thresholdreplay;

import java.util.List;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tak
 */
public class ThresholdReplay {
    private static Logger logger = LoggerFactory.getLogger(ThresholdReplay.class);
    private final ThresholdConfiguration thresholdConfiguration;
    private final List<ThresholdOccur> thresholdOccurs;

    public ThresholdReplay(ThresholdConfiguration thresholdConfiguration, List<ThresholdOccur> thresholdOccurs) {
        this.thresholdConfiguration = thresholdConfiguration;
        this.thresholdOccurs = thresholdOccurs;
    }

    public ThresholdConfiguration getThresholdConfiguration() {
        return thresholdConfiguration;
    }

    public List<ThresholdOccur> getThresholdOccurs() {
        return thresholdOccurs;
    }
    
    public String toFormatedString() {
        String result = "\n";
        result = result.concat(thresholdConfiguration.toFormatedString() + "\n");
        Period overallPeriod = new Period();
        for (ThresholdOccur thresholdOccur : thresholdOccurs) {
            overallPeriod = overallPeriod.plus(thresholdOccur.getPeriod());
        }
        result = result.concat("\t Threshold exeeded " + thresholdOccurs.size() + " times for overall " + formatPeriod(overallPeriod) + "\n");
        return result;
    }
    
    //TODO find a better place for this pice
    private static String formatPeriod(Period period) {
        PeriodFormatter daysHoursMinutes = new PeriodFormatterBuilder()
                .appendDays()
                .appendSuffix(" day", " days")
                .appendSeparator(" and ")
                .appendMinutes()
                .appendSuffix(" minute", " minutes")
                .appendSeparator(" and ")
                .appendSeconds()
                .appendSuffix(" second", " seconds")
                .toFormatter();
        return daysHoursMinutes.print(period);
    }
    
}
