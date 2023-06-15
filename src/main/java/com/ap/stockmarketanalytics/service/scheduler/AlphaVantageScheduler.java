package com.ap.stockmarketanalytics.service.scheduler;


import com.ap.stockmarketanalytics.config.kafka.KafkaConfig;
import com.ap.stockmarketanalytics.model.CompanyOverview;
import com.ap.stockmarketanalytics.model.StockQuote;
import com.ap.stockmarketanalytics.model.TimeSeriesDailyQuote;
import com.ap.stockmarketanalytics.model.mapper.QuoteObjectMapper;
import com.ap.stockmarketanalytics.model.mapper.TimeSeriesMapper;
import com.ap.stockmarketanalytics.restclient.alphavantage.AlphaVantageWebClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AlphaVantageScheduler {

    private final KafkaConfig kafkaConfig;
    private final AlphaVantageWebClient alphaVantageWebClient;
    private final QuoteObjectMapper quoteObjectMapper;
    private final TimeSeriesMapper timeSeriesMapper;

    private final String QUOTE_TOPIC = "TICKER_QUOTE";
    private final String TIME_SERIES_DAILY_TOPIC = "TIME_SERIES_DAILY";
    private final String COMPANY_OVERVIEW_TOPIC = "COMPANY_OVERVIEW";


    public AlphaVantageScheduler(KafkaConfig kafkaConfig,
                                 AlphaVantageWebClient alphaVantageWebClient,
                                 QuoteObjectMapper quoteObjectMapper,
                                 TimeSeriesMapper timeSeriesMapper) {
        this.kafkaConfig = kafkaConfig;
        this.alphaVantageWebClient = alphaVantageWebClient;
        this.quoteObjectMapper = quoteObjectMapper;
        this.timeSeriesMapper = timeSeriesMapper;
    }

    //@Scheduled(fixedRate = 10000)
    public void publishStockPrice() {

        alphaVantageWebClient.getStockPrice("AMZN").subscribe(q -> {

            final KafkaProducer<String, StockQuote> producer =
                    new KafkaProducer<>(kafkaConfig.getKafkaConnectionConfig());

            //data model with avro schema.
            StockQuote sq = quoteObjectMapper.quoteToStockQuote(q);

            ProducerRecord<String, StockQuote> quoteRecord =
                    new ProducerRecord<>(QUOTE_TOPIC, sq.getSymbol(),sq);

            producer.send(quoteRecord);

            //log.info("Stock quote: {}", sq);

        });

    }

    @Scheduled(fixedRate = 10000)
    public void publishTimeSeriesStockData() {

        log.info("Calculating time series data for IBM");
        String quote = "IBM";

        alphaVantageWebClient.getTimeSeriesDailyAdjusted(quote).subscribe(tsda -> {

            tsda.getTimeSeriesDaily().values().stream().limit(5).forEach(tsd -> {

                log.info("Logging time series data: {}", tsd);

                final KafkaProducer<String, TimeSeriesDailyQuote> producer =
                        new KafkaProducer<>(kafkaConfig.getKafkaConnectionConfig());

                //data model with avro schema.
                TimeSeriesDailyQuote tsdq =
                        timeSeriesMapper.timeSeriesDailyToTimeSeriesDailyQuote(tsd);

                ProducerRecord<String, TimeSeriesDailyQuote> quoteRecord =
                        new ProducerRecord<>(TIME_SERIES_DAILY_TOPIC, quote,tsdq);

                producer.send(quoteRecord);

            });

        });

    }

    //@Scheduled(fixedRate = 10000)
    public void publishCompanyOverview() {

        alphaVantageWebClient.getCompanyOverview("AMZN").subscribe(co-> {

            final KafkaProducer<String, CompanyOverview> producer =
                    new KafkaProducer<>(kafkaConfig.getKafkaConnectionConfig());

            ProducerRecord<String, CompanyOverview> companyOverview =
                    new ProducerRecord<>(QUOTE_TOPIC, co);

            //producer.send(companyOverview);
            //log.info("Company overview: {}", co.toString());

        });

    }

}
