package com.ap.stockmarketanalytics.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class QuoteResponse {

    @JsonProperty("Global Quote")
    private Quote globalQuote;

    public Quote getGlobalQuote() {
        return globalQuote;
    }

    public void setGlobalQuote(Quote globalQuote) {
        this.globalQuote = globalQuote;
    }

}
