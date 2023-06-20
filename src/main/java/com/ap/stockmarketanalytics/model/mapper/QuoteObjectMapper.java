package com.ap.stockmarketanalytics.model.mapper;

import com.ap.stockmarketanalytics.model.Quote;
import com.ap.stockmarketanalytics.model.StockQuote;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QuoteObjectMapper {

    Quote stockQuoteToQuote(StockQuote stockQuote);
    StockQuote quoteToStockQuote(Quote quote);

}
