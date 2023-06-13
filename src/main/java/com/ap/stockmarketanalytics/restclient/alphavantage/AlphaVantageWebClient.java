package com.ap.stockmarketanalytics.restclient.alphavantage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class AlphaVantageWebClient {

    private final WebClient webClient;


    @Value("${stock-market-analytics.alpha-vantage.apiKey}")
    private String apiKey;

    private static final String ALPHA_VANTAGE_BASE_URL = "http://xyz";

    public AlphaVantageWebClient(WebClient.Builder webClient) {
        this.webClient = webClient.build();
    }

    public Mono<String> getStockPrice(String tickerId) {


        log.info("heartbeat!!");

        return webClient
                .get()
                .uri("https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=IBM&apikey=demo")
                .retrieve()
                .bodyToMono(String.class);
    }



}
