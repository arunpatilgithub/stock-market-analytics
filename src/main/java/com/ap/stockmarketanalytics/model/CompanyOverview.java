package com.ap.stockmarketanalytics.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CompanyOverview {

    @JsonProperty("Symbol")
    public String symbol;

    @JsonProperty("AssetType")
    public String assetType;

    @JsonProperty("Name")
    public String name;

    @JsonProperty("Description")
    public String description;

    @JsonProperty("CIK")
    public String cIK;

    @JsonProperty("Exchange")
    public String exchange;

    @JsonProperty("Currency")
    public String currency;

    @JsonProperty("Country")
    public String country;

    @JsonProperty("Sector")
    public String sector;

    @JsonProperty("Industry")
    public String industry;

    @JsonProperty("Address")
    public String address;

    @JsonProperty("FiscalYearEnd")
    public String fiscalYearEnd;

    @JsonProperty("LatestQuarter")
    public String latestQuarter;

    @JsonProperty("MarketCapitalization")
    public String marketCapitalization;

    @JsonProperty("EBITDA")
    public String eBITDA;

    @JsonProperty("PERatio")
    public String pERatio;

    @JsonProperty("PEGRatio")
    public String pEGRatio;

    @JsonProperty("BookValue")
    public String bookValue;

    @JsonProperty("DividendPerShare")
    public String dividendPerShare;

    @JsonProperty("DividendYield")
    public String dividendYield;

    @JsonProperty("EPS")
    public String ePS;

    @JsonProperty("RevenuePerShareTTM")
    public String revenuePerShareTTM;

    @JsonProperty("ProfitMargin")
    public String profitMargin;

    @JsonProperty("OperatingMarginTTM")
    public String operatingMarginTTM;

    @JsonProperty("ReturnOnAssetsTTM")
    public String returnOnAssetsTTM;

    @JsonProperty("ReturnOnEquityTTM")
    public String returnOnEquityTTM;

    @JsonProperty("RevenueTTM")
    public String revenueTTM;

    @JsonProperty("GrossProfitTTM")
    public String grossProfitTTM;

    @JsonProperty("DilutedEPSTTM")
    public String dilutedEPSTTM;

    @JsonProperty("QuarterlyEarningsGrowthYOY")
    public String quarterlyEarningsGrowthYOY;

    @JsonProperty("QuarterlyRevenueGrowthYOY")
    public String quarterlyRevenueGrowthYOY;

    @JsonProperty("AnalystTargetPrice")
    public String analystTargetPrice;

    @JsonProperty("TrailingPE")
    public String trailingPE;

    @JsonProperty("ForwardPE")
    public String forwardPE;

    @JsonProperty("PriceToSalesRatioTTM")
    public String priceToSalesRatioTTM;

    @JsonProperty("PriceToBookRatio")
    public String priceToBookRatio;

    @JsonProperty("EVToRevenue")
    public String eVToRevenue;

    @JsonProperty("EVToEBITDA")
    public String eVToEBITDA;

    @JsonProperty("Beta")
    public String beta;

    @JsonProperty("52WeekHigh")
    public String _52WeekHigh;

    @JsonProperty("52WeekLow")
    public String fiftyTwoWeekLow;

    @JsonProperty("50DayMovingAverage")
    public String fiftyDayMovingAverage;

    @JsonProperty("200DayMovingAverage")
    public String twoHundredDayMovingAverage;

    @JsonProperty("SharesOutstanding")
    public String sharesOutstanding;

    @JsonProperty("DividendDate")
    public String dividendDate;

    @JsonProperty("ExDividendDate")
    public String exDividendDate;

}
