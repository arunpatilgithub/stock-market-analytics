package com.ap.stockmarketanalytics.restclient.alphavantage;

import com.ap.stockmarketanalytics.model.CompanyOverview;
import com.ap.stockmarketanalytics.model.Quote;
import com.ap.stockmarketanalytics.model.QuoteResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class AlphaVantageWebClient {

    private final WebClient webClient;


    @Value("${stock-market-analytics.alpha-vantage.apiKey}")
    private String apiKey;

    private static final String ALPHA_VANTAGE_BASE_URL = "https://www.alphavantage.co/";

    public AlphaVantageWebClient(WebClient.Builder webClient) {
        this.webClient =
                webClient
                        .baseUrl(ALPHA_VANTAGE_BASE_URL)
                        .codecs(configurer ->
                                        configurer
                                                .defaultCodecs()
                                                .jackson2JsonDecoder(new Jackson2JsonDecoder()))
                        .build();
    }

    public Mono<Quote> getStockPrice(String tickerId) {

        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/query/")
                        .queryParam("function", "GLOBAL_QUOTE")
                        .queryParam("symbol", tickerId)
                        .queryParam("apikey", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(QuoteResponse.class)
                .map(QuoteResponse::getGlobalQuote);
    }

    public Mono<CompanyOverview> getCompanyOverview(String tickerId) {

        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/query/")
                        .queryParam("function", "OVERVIEW")
                        .queryParam("symbol", tickerId)
                        .queryParam("apikey", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(CompanyOverview.class);
    }


}
