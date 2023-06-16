package com.ap.stockmarketanalytics.consumer;

import com.ap.stockmarketanalytics.model.FiveDaysAverage;
import com.ap.stockmarketanalytics.model.StockCountSum;
import com.ap.stockmarketanalytics.model.TimeSeriesDailyQuote;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;
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

        quoteDailyEvents
                .print(Printed.<String, TimeSeriesDailyQuote>toSysOut()
                              .withLabel("Daily quotes for last 5 days"));

        //Define time window of 5 days.
        TimeWindows tumblingWindow = TimeWindows.ofSizeWithNoGrace(Duration.ofDays(5));

        //KTable<Windowed<String>, StockCountSum> fiveDayMovingAverage =
                quoteDailyEvents
                        .groupByKey()
                        .windowedBy(tumblingWindow)
                        .aggregate(() -> new StockCountSum(0L,0.0), (key, value, aggregate) -> {
                            aggregate.setCount(aggregate.getCount() + 1);
                            aggregate.setSum(aggregate.getSum() + value.getClose());
                            return aggregate;
                        }).toStream()
                        .map((Windowed<String> key, StockCountSum stockAverage) -> {
                            double aveNoFormat = stockAverage.getSum()/(double)stockAverage.getCount();
                            double formattedAve = Double.parseDouble(String.format("%.4f", aveNoFormat));

                            FiveDaysAverage fda = new FiveDaysAverage(formattedAve);

                            return new KeyValue<>(key.key(), fda) ;
                        })
                        .to("5_days_moving_average");

    }


}
