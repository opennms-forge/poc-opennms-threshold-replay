package org.opennms.forge.thresholdreplay;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.joda.time.Instant;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.opennms.netmgt.config.threshd.Threshold;
import org.opennms.netmgt.threshd.ThresholdConfigWrapper;
import org.opennms.netmgt.threshd.ThresholdEvaluatorAbsoluteChange;
import org.opennms.netmgt.threshd.ThresholdEvaluatorHighLow;
import org.opennms.netmgt.threshd.ThresholdEvaluatorRelativeChange;
import org.opennms.netmgt.threshd.ThresholdEvaluatorState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tak
 */
public class ThresholdReplayer {

    private static Logger logger = LoggerFactory.getLogger(ThresholdReplayer.class);

    public static ThresholdReplay replayThresholdAgainstTimeSeriesDataMap(ThresholdConfiguration thresholdConfiguration, Map<Instant, Double> timeSeriesDataMap) {

        ThresholdEvaluatorState thresholdEvaluatorState = createThresholdEvaluatorState(thresholdConfiguration);
        ThresholdEvaluatorState.Status status;

        List<ThresholdOccur> thresholdOccurs = new LinkedList<ThresholdOccur>();
        ThresholdOccur thresholdOccur = null;
        for (Map.Entry<Instant, Double> entry : timeSeriesDataMap.entrySet()) {
            status = thresholdEvaluatorState.evaluate(entry.getValue());
            if (status.equals(ThresholdEvaluatorState.Status.TRIGGERED)) {
                thresholdOccur = new ThresholdOccur(new Instant(entry.getKey()), null);
                logger.debug("Threshold TRIGGERED at :: " + entry.getKey() + "\t with :: " + entry.getValue());
            }
            if (status.equals(ThresholdEvaluatorState.Status.RE_ARMED)) {
                thresholdOccur.setRearmed(entry.getKey());
                thresholdOccurs.add(thresholdOccur);
                logger.debug("Threshold RE_ARMED  at :: " + entry.getKey() + "\t with :: " + entry.getValue() + " after " + formatPeriod(thresholdOccur.getPeriod()));
            }
        }
        ThresholdReplay thresholdReplay = new ThresholdReplay(thresholdConfiguration, thresholdOccurs);

        return thresholdReplay;
    }

    private static ThresholdEvaluatorState createThresholdEvaluatorState(ThresholdConfiguration thresholdConfiguration) {
        Threshold threshold = new Threshold();
        threshold.setType(thresholdConfiguration.getThresholdType());
        threshold.setDsName(thresholdConfiguration.getDataSourceName());
        threshold.setDsType(thresholdConfiguration.getDataSourceType());
        threshold.setValue(thresholdConfiguration.getThresholdValue());
        threshold.setRearm(thresholdConfiguration.getThresholdRearm());
        threshold.setTrigger(thresholdConfiguration.getThresholdTrigger());
        ThresholdConfigWrapper wrapper = new ThresholdConfigWrapper(threshold);
        ThresholdEvaluatorState thresholdEvaluatorState = null;
        if (threshold.getType().equals("high") || threshold.getType().equals("low")) {
            thresholdEvaluatorState = new ThresholdEvaluatorHighLow.ThresholdEvaluatorStateHighLow(wrapper);
        } else if (threshold.getDsType().equals("absoluteChange")) {
            thresholdEvaluatorState = new ThresholdEvaluatorAbsoluteChange.ThresholdEvaluatorStateAbsoluteChange(wrapper);
        }
        if (threshold.getDsType().equals("relativeChange")) {
            thresholdEvaluatorState = new ThresholdEvaluatorRelativeChange.ThresholdEvaluatorStateRelativeChange(wrapper);
        }
        return thresholdEvaluatorState;
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
