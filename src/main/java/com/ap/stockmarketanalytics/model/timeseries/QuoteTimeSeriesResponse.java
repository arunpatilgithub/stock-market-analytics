package com.ap.stockmarketanalytics.model.timeseries;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;

@Data
public class QuoteTimeSeriesResponse {

    @JsonProperty("Meta Data")
    private MetadataResponse metadataResponse;

    @JsonProperty("Time Series (Daily)")
    private Map<LocalDate, TimeSeriesDaily> timeSeriesDaily;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    public void setTimeSeriesData(Map<String, TimeSeriesDaily> timeSeriesData) {

        this.timeSeriesDaily = new TreeMap<>();

        for (Map.Entry<String, TimeSeriesDaily> entry : timeSeriesData.entrySet()) {
            String dateString = entry.getKey();
            TimeSeriesDaily timeSeriesDataValue = entry.getValue();
            LocalDate date = LocalDate.parse(dateString, DATE_FORMATTER);
            this.timeSeriesDaily.put(date, timeSeriesDataValue);
        }

    }


}
