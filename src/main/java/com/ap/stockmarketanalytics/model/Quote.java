package com.ap.stockmarketanalytics.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Quote {

    private String symbol;

    private double open;

    private double high;

    private double low;

    private double price;

    private double volume;

    private String latestTradingDay;

    private double previousClose;

    private double change;

    private double changePercent;

}
