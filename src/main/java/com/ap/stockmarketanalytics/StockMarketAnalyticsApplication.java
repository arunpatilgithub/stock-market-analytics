package com.ap.stockmarketanalytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class StockMarketAnalyticsApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockMarketAnalyticsApplication.class, args);
	}

}
