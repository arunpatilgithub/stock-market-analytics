package com.ap.stockmarketanalytics.consumer;

import com.ap.stockmarketanalytics.model.TimeSeriesDailyQuote;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.processor.TimestampExtractor;

public class FiveDayTrailingTimeStampExtractor implements TimestampExtractor {

    @Override
    public long extract(ConsumerRecord<Object, Object> record,
                        long partitionTime) {
        TimeSeriesDailyQuote timeSeriesDailyQuote =  (TimeSeriesDailyQuote)record.value();

        if (timeSeriesDailyQuote != null && StringUtils.isNoneBlank(timeSeriesDailyQuote.getClosingDate())) {

            String closingDate = timeSeriesDailyQuote.getClosingDate();;
            return Long.valueOf(closingDate);

        }

        return partitionTime;
    }
}
