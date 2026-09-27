# Spring Boot Kafka JSON Demo

A beginner-friendly Spring Boot application that sends and consumes a Java `Customer` object as JSON using Apache Kafka.

## Technology stack

- Java 21
- Spring Boot 3.4.3
- Maven
- Spring Web
- Spring for Apache Kafka
- Apache Kafka running locally at `localhost:9092`

This project does not use Docker, Lombok, a database, Avro, or Schema Registry.

## Architecture

```text
Postman / Client
       |
       | POST JSON
       v
JsonMessageController
       |
       | Customer object
       v
JsonKafkaProducer
       |
       | KafkaTemplate<String, Customer>
       v
JsonSerializer
       |
       | JSON / Kafka bytes
       v
Kafka Broker: localhost:9092
       |
       | customer-topic
       v
JsonDeserializer
       |
       | Customer object
       v
JsonKafkaConsumer
       |
       v
Console output
```

For a detailed explanation, see [ARCHITECTURE.md](ARCHITECTURE.md).

## Project structure

```text
src/main/java/com/example/kafkademo
├── KafkaDemoApplication.java
├── config/KafkaTopicConfig.java
├── model/Customer.java
├── producer/JsonKafkaProducer.java
├── consumer/JsonKafkaConsumer.java
└── controller/JsonMessageController.java
```

## Kafka configuration

The application expects Kafka to be available at:

```text
localhost:9092
```

The application creates or uses the following topic:

```text
customer-topic
```

The consumer group is:

```text
customer-group
```

The producer uses `JsonSerializer` and the consumer uses `JsonDeserializer`. The consumer trusts only the model package:

```properties
spring.kafka.consumer.properties.spring.json.trusted.packages=com.example.kafkademo.model
```

Trusted packages limit which Java packages can be instantiated while deserializing JSON messages.

## Prerequisites

Install or have available:

- Java 21
- Maven
- Apache Kafka running locally on port `9092`

Docker is not required for this project.

## Build the project

From the project directory, run:

```bash
mvn clean compile
```

## Start the application

```bash
mvn spring-boot:run
```

The REST API starts on port `8080`.

## Test with Postman

Send a `POST` request to:

```text
http://localhost:8080/api/v1/kafka/publish-customer
```

Header:

```text
Content-Type: application/json
```

Request body:

```json
{
  "id": 101,
  "name": "Anand",
  "email": "anand@gmail.com"
}
```

Expected response:

```text
Customer sent to Kafka successfully
```

## Expected console output

Producer output:

```text
Producing Customer: Customer{id=101, name='Anand', email='anand@gmail.com'}
```

Consumer output:

```text
Customer received from Kafka: Customer{id=101, name='Anand', email='anand@gmail.com'}
```

## Serialization and deserialization

The producer flow is:

```text
Customer object -> JsonSerializer -> JSON bytes -> Kafka topic
```

The consumer flow is:

```text
Kafka topic -> JSON bytes -> JsonDeserializer -> Customer object
```

No manual JSON conversion or parsing is required. Spring Kafka handles both operations using the configured serializers.

## Main endpoint

| Method | Endpoint | Purpose |
|---|---|---|
| `POST` | `/api/v1/kafka/publish-customer` | Sends a customer JSON object to Kafka |

## Verification

The project has been verified with:

```bash
mvn -B clean compile
```

The build completes successfully.
