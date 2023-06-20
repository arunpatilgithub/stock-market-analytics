package com.ap.stockmarketanalytics.rest;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestRestController {

    private final StreamsBuilderFactoryBean streamsBuilderFactoryBean;

    public TestRestController(StreamsBuilderFactoryBean streamsBuilderFactoryBean) {
        this.streamsBuilderFactoryBean = streamsBuilderFactoryBean;
    }

    @GetMapping
    public Map<String, Long> getTestData() {

        Map<String, Long> leaderboard = new HashMap<>();

        KeyValueIterator<String, Long> range = getStore().all();
        while (range.hasNext()) {
            KeyValue<String, Long> next = range.next();
            String game = next.key;
            Long highScores = next.value;
            leaderboard.put(game, highScores);
        }

        return leaderboard;
    }

    ReadOnlyKeyValueStore<String, Long> getStore() {
        return streamsBuilderFactoryBean.getKafkaStreams().store(
                StoreQueryParameters.fromNameAndType(
                        "five-day-moving-average-view",
                        QueryableStoreTypes.keyValueStore()));
    }

}
