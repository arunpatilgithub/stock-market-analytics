package com.ap.stockmarketanalytics.model.mapper;

import com.ap.stockmarketanalytics.model.TimeSeriesDailyQuote;
import com.ap.stockmarketanalytics.model.timeseries.TimeSeriesDaily;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TimeSeriesMapper {

    TimeSeriesDaily timeSeriesDailyQuoteToTimeSeriesDaily(TimeSeriesDailyQuote timeSeriesDailyQuote);
    TimeSeriesDailyQuote timeSeriesDailyToTimeSeriesDailyQuote(TimeSeriesDaily timeSeriesDaily);

}
