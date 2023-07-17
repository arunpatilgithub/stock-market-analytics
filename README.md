# Stock Market Analytics

Stock Market Analytics is a Java-based project that utilizes Apache Kafka, Redis, and Avro to perform real-time processing and analysis of stock market data. The project aims to calculate the 5-day moving average of stock prices and store the results for further analysis.

## Features

- Consumes stock market data from a Kafka topic using `Apache Kafka Streams`.
- Performs aggregation and windowing operations to calculate the 5-day moving average.
- Utilizes a `TumblingWindow` to group events within distinct and non-overlapping intervals.
- Stores time series stock data in `Redis` using the `Redisson` client library.
- Uses `Avro` for serialization and deserialization of data when interacting with Kafka.

## Prerequisites

Before running the project, make sure you have the following prerequisites installed:

- Apache Kafka: [Installation Guide](https://kafka.apache.org/documentation/#quickstart)
- Redis: [Installation Guide](https://redis.io/topics/quickstart)


## Components

The project consists of the following components:

- `KafkaConfig`: Configures Kafka properties and sets up Kafka Streams configuration.
- `RedisConnectionFactory`: Configures the Redis connection details using Redisson client library.
- `FiveDayTrailingTimeStampExtractor`: Custom timestamp extractor for Kafka Streams.
- `TimeSeriesQuoteConsumer`: Consumes stock market data, performs aggregation, and calculates the 5-day moving average.
- `AlphaVantageWebClient`: Makes REST API calls to retrieve stock market data from Alpha Vantage.
- `AlphaVantageScheduler`: Retrieves time series stock data, logs it, stores it in Redis, and publishes it to a Kafka topic.