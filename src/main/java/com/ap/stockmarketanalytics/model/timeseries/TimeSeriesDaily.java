package com.ap.stockmarketanalytics.model.timeseries;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimeSeriesDaily {

    @JsonProperty("1. open")
    private double open;

    @JsonProperty("2. high")
    private double high;

    @JsonProperty("3. low")
    private double low;

    @JsonProperty("4. close")
    private double close;

    @JsonProperty("5. adjusted close")
    private double adjustedClose;

    @JsonProperty("6. volume")
    private long volume;

    @JsonProperty("7. dividend amount")
    private double dividendAmount;

    @JsonProperty("8. split coefficient")
    private double splitCoefficient;

    @Override
    public String toString() {
        return "TimeSeriesDaily{" +
                "open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", close=" + close +
                ", adjustedClose=" + adjustedClose +
                ", volume=" + volume +
                ", dividendAmount=" + dividendAmount +
                ", splitCoefficient=" + splitCoefficient +
                '}';
    }
}
