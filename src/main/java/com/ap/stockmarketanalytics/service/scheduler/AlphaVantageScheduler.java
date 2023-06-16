package com.ap.stockmarketanalytics.service.scheduler;


import com.ap.stockmarketanalytics.config.kafka.KafkaConfig;
import com.ap.stockmarketanalytics.model.TimeSeriesDailyQuote;
import com.ap.stockmarketanalytics.model.mapper.QuoteObjectMapper;
import com.ap.stockmarketanalytics.model.mapper.TimeSeriesMapper;
import com.ap.stockmarketanalytics.model.timeseries.TimeSeriesDaily;
import com.ap.stockmarketanalytics.restclient.alphavantage.AlphaVantageWebClient;
import lombok.extern.slf4j.Slf4j;
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
    private final QuoteObjectMapper quoteObjectMapper;
    private final TimeSeriesMapper timeSeriesMapper;

    private final String QUOTE_TOPIC = "TICKER_QUOTE";
    private final String TIME_SERIES_DAILY_TOPIC = "TIME_SERIES_DAILY";
    private final String COMPANY_OVERVIEW_TOPIC = "COMPANY_OVERVIEW";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public AlphaVantageScheduler(KafkaConfig kafkaConfig,
                                 AlphaVantageWebClient alphaVantageWebClient,
                                 QuoteObjectMapper quoteObjectMapper,
                                 TimeSeriesMapper timeSeriesMapper,
                                 KafkaTemplate<String, Object> kafkaTemplate) {
        this.alphaVantageWebClient = alphaVantageWebClient;
        this.quoteObjectMapper = quoteObjectMapper;
        this.timeSeriesMapper = timeSeriesMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    //@Scheduled(fixedRate = 10000)
    public void publishStockPrice() {

        alphaVantageWebClient.getStockPrice("AMZN").subscribe(q -> {

//            final KafkaProducer<String, StockQuote> producer =
//                    new KafkaProducer<>(kafkaConfig.getKafkaConnectionConfig());
//
//            //data model with avro schema.
//            StockQuote sq = quoteObjectMapper.quoteToStockQuote(q);
//
//            ProducerRecord<String, StockQuote> quoteRecord =
//                    new ProducerRecord<>(QUOTE_TOPIC, sq.getSymbol(),sq);
//
//            producer.send(quoteRecord);


            //log.info("Stock quote: {}", sq);

        });

    }

    @Scheduled(fixedRate = 15000)
    public void publishTimeSeriesStockData() {

        log.info("Calculating time series data for IBM");
        String quote = "IBM";


        alphaVantageWebClient.getTimeSeriesDailyAdjusted(quote).subscribe(tsda -> {

            tsda.getMetadataResponse();


            tsda.getTimeSeriesDaily().entrySet().stream().limit(5).forEach(e -> {

                String dayOfClosing = e.getKey().toString();
                ZonedDateTime zonedDateTime = LocalDate.parse(dayOfClosing).atTime(23, 59).atZone(ZoneId.of("America" +
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
                                                                  String.valueOf(zonedDateTime.toEpochSecond()));

                log.info("Logging time series data: {}", t);

                kafkaTemplate.send(TIME_SERIES_DAILY_TOPIC, t.getSymbol(),t);


            });
        });

    }

    //@Scheduled(fixedRate = 10000)
    public void publishCompanyOverview() {

//        alphaVantageWebClient.getCompanyOverview("AMZN").subscribe(co-> {
//
//            final KafkaProducer<String, CompanyOverview> producer =
//                    new KafkaProducer<>(kafkaConfig.getKafkaConnectionConfig());
//
//            ProducerRecord<String, CompanyOverview> companyOverview =
//                    new ProducerRecord<>(QUOTE_TOPIC, co);
//
//            //producer.send(companyOverview);
//            //log.info("Company overview: {}", co.toString());
//
//        });

    }

}
