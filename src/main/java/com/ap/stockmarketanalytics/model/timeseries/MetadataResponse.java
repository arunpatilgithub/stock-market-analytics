package com.ap.stockmarketanalytics.model.timeseries;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MetadataResponse {

    //private Metadata metadata;
    @JsonProperty("1. Information")
    private String information;

    @JsonProperty("2. Symbol")
    private String symbol;

    @JsonProperty("3. Last Refreshed")
    private String lastRefreshed;

    @JsonProperty("4. Output Size")
    private String outputSize;

    @JsonProperty("5. Time Zone")
    private String timeZone;

}
