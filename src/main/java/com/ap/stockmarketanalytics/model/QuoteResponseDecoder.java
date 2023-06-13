package com.ap.stockmarketanalytics.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.reactivestreams.Publisher;
import org.springframework.core.ResolvableType;
import org.springframework.core.codec.Decoder;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class QuoteResponseDecoder implements Decoder<Map<String, Object>> {


    @Override
    public boolean canDecode(ResolvableType elementType, MimeType mimeType) {
        return mimeType != null
                && mimeType.equals(MimeTypeUtils.APPLICATION_JSON)
                && elementType.resolve() == Quote.class;
    }

    @Override
    public Flux<Map<String, Object>> decode(Publisher<DataBuffer> inputStream,
                                            ResolvableType elementType,
                                            MimeType mimeType,
                                            Map<String, Object> hints) {
        ObjectMapper objectMapper = new ObjectMapper();

        return Flux.from(inputStream)
                //.collectList()
                   .flatMap(dataBuffers -> {
                       DataBuffer mergedDataBuffer =
                               DataBufferUtils.join(Flux.from(dataBuffers));
                       try {
                           // Convert the merged data buffer to a byte array
                           byte[] data = new byte[mergedDataBuffer.readableByteCount()];
                           mergedDataBuffer.read(data);
                           // Deserialize the JSON response into a Map
                           Map<String, Object> responseMap = objectMapper.readValue(data, Map.class);
                           // Extract the nested "Global Quote" object
                           Map<String, Object> globalQuoteMap = (Map<String, Object>) responseMap.get("Global Quote");
                           // Map the values to the Quote object
                           Quote quote = new Quote();
                           quote.setSymbol((String) globalQuoteMap.get("01. symbol"));
                           quote.setOpen(Double.parseDouble((String) globalQuoteMap.get("02. open")));
                           quote.setHigh(Double.parseDouble((String) globalQuoteMap.get("03. high")));
                           quote.setLow(Double.parseDouble((String) globalQuoteMap.get("04. low")));
                           quote.setPrice(Double.parseDouble((String) globalQuoteMap.get("05. price")));
                           quote.setVolume(Double.parseDouble((String) globalQuoteMap.get("06. volume")));
                           quote.setLatestTradingDay((String) globalQuoteMap.get("07. latest trading day"));
                           quote.setPreviousClose(Double.parseDouble((String) globalQuoteMap.get("08. previous close")));
                           quote.setChange(Double.parseDouble((String) globalQuoteMap.get("09. change")));
                           quote.setChangePercent(Double.parseDouble(((String) globalQuoteMap.get("10. change percent")).replace("%", "")));

                           return Mono.just(quote);
                       } catch (IOException e) {
                           return Mono.error(new RuntimeException("Error decoding the response", e));
                       }
                   });
    }



    @Override
    public Mono<Map<String, Object>> decodeToMono(Publisher<DataBuffer> inputStream,
                                                  ResolvableType elementType,
                                                  MimeType mimeType,
                                                  Map<String, Object> hints) {
        return null;
    }

    @Override
    public List<MimeType> getDecodableMimeTypes() {
        return null;
    }
}
