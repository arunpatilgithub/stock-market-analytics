package com.ap.stockmarketanalytics.consumer;

import com.ap.stockmarketanalytics.model.TimeSeriesDailyQuote;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Suppressed;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.Windowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Slf4j
@Component
public class TimeSeriesQuoteConsumer {

    private final String TIME_SERIES_DAILY_TOPIC = "TIME_SERIES_DAILY";

    @Autowired
    public void buildTopology(StreamsBuilder builder) {

        //Abstraction for consuming events from source topic.
        KStream<String, TimeSeriesDailyQuote> quoteDailyEvents =
                builder.stream(TIME_SERIES_DAILY_TOPIC, Consumed.with(new FiveDayTrailingTimeStampExtractor()));

        //Define time window of 5 days.
        TimeWindows tumblingWindow = TimeWindows.ofSizeWithNoGrace(Duration.ofDays(5));

        KTable<Windowed<String>, Long> fiveDayMovingAverage =
                quoteDailyEvents
                        // 2
                        .groupByKey()
                        // 3.1 - windowed aggregation
                        .windowedBy(tumblingWindow)
                        // 3.2 - windowed aggregation
                        .count(Materialized.as("five-day-moving-average-view"))
                        // 4
                        .suppress(
                                Suppressed.untilWindowCloses(
                                        Suppressed.BufferConfig.unbounded().shutDownWhenFull()));

        fiveDayMovingAverage.toStream().print(Printed.<Windowed<String>, Long>toSysOut().withLabel("Five day moving " +
                                                                                                           "average!"));
//
//        KStream<String, Long> metrics = builder.stream("five-day-moving-average");
//        metrics.foreach((k,v) -> {
//            log.info("Key: {}", k);
//            log.info("Value: {}", v);
//        });

    }


}
