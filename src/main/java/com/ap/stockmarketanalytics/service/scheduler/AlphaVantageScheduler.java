package com.ap.stockmarketanalytics.service.scheduler;


import com.ap.stockmarketanalytics.config.kafka.KafkaConfig;
import com.ap.stockmarketanalytics.restclient.alphavantage.AlphaVantageWebClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AlphaVantageScheduler {

    private final KafkaConfig kafkaConfig;
    private final AlphaVantageWebClient alphaVantageWebClient;


    public AlphaVantageScheduler(KafkaConfig kafkaConfig,
                                 AlphaVantageWebClient alphaVantageWebClient) {
        this.kafkaConfig = kafkaConfig;
        this.alphaVantageWebClient = alphaVantageWebClient;
    }

    @Scheduled(fixedRate = 3000)
    public void publishStockPrice() {

        alphaVantageWebClient.getStockPrice("IBM").log();

    }

}
