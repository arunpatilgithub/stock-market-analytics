package com.ap.stockmarketanalytics.service.scheduler;


import com.ap.stockmarketanalytics.model.TimeSeriesDailyQuote;
import com.ap.stockmarketanalytics.model.timeseries.TimeSeriesDaily;
import com.ap.stockmarketanalytics.restclient.alphavantage.AlphaVantageWebClient;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RTimeSeries;
import org.redisson.api.RedissonClient;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Slf4j
@Component
public class AlphaVantageScheduler {

    //private final KafkaConfig kafkaConfig;
    private final AlphaVantageWebClient alphaVantageWebClient;

    private final String QUOTE_TOPIC = "TICKER_QUOTE";
    private final String TIME_SERIES_DAILY_TOPIC = "TIME_SERIES_DAILY";
    private final String COMPANY_OVERVIEW_TOPIC = "COMPANY_OVERVIEW";

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final RedissonClient redissonClient;

    public AlphaVantageScheduler(AlphaVantageWebClient alphaVantageWebClient,
                                 KafkaTemplate<String, Object> kafkaTemplate,
                                 RedissonClient redissonClient) {
        this.alphaVantageWebClient = alphaVantageWebClient;

        this.kafkaTemplate = kafkaTemplate;
        this.redissonClient = redissonClient;
    }

    //@Scheduled(fixedRate = 10000)
    public void publishStockPrice() {

        alphaVantageWebClient.getStockPrice("AMZN").subscribe(q -> {
            kafkaTemplate.send(QUOTE_TOPIC, q);
            log.info("Stock quote: {}", q);
        });

    }

    @Scheduled(fixedRate = 15000)
    public void publishTimeSeriesStockData() {

        log.info("Calculating time series data for IBM");
        String quote = "IBM";

        //This adds ZSET records in redis that is sorted by timestamp field.
        RTimeSeries<Double,String> ts = redissonClient.getTimeSeries("stock-series");

        alphaVantageWebClient.getTimeSeriesDailyAdjusted(quote).subscribe(tsda -> {

            tsda.getMetadataResponse();

            tsda.getTimeSeriesDaily().entrySet().stream().limit(5).forEach(e -> {

                String dayOfClosing = e.getKey().toString();
                ZonedDateTime closingDayInZonedDayTime = LocalDate.parse(dayOfClosing).atTime(23, 59).atZone(ZoneId.of("America" +
                                                                                                                    "/St_Johns"));

                TimeSeriesDaily tsd = e.getValue();

                TimeSeriesDailyQuote t = new TimeSeriesDailyQuote(quote,
                                                                  tsd.getOpen(),
                                                                  tsd.getHigh(),
                                                                  tsd.getLow(),
                                                                  tsd.getClose(),
                                                                  tsd.getAdjustedClose(),
                                                                  tsd.getVolume(),
                                                                  tsd.getDividendAmount(),
                                                                  tsd.getSplitCoefficient(),
                                                                  String.valueOf(closingDayInZonedDayTime.toInstant().toEpochMilli()));

                log.info("Logging time series data: {}", t);


                ts.add(closingDayInZonedDayTime.toInstant().toEpochMilli(), t.getClose());

                kafkaTemplate.send(TIME_SERIES_DAILY_TOPIC, t.getSymbol(),t);

            });
        });

    }


    //@Scheduled(fixedRate = 10000)
    public void publishCompanyOverview() {

        alphaVantageWebClient.getCompanyOverview("AMZN").subscribe(co-> {

            kafkaTemplate.send(COMPANY_OVERVIEW_TOPIC, co);

            log.info("Company overview: {}", co.toString());

        });

    }

}
