# Kafka JSON Demo Architecture

This project demonstrates how a Spring Boot application sends and receives a Java object as JSON using Apache Kafka.

The example uses:

- Java 21
- Spring Boot 3.4.3
- Spring Web
- Spring for Apache Kafka
- Apache Kafka running at `localhost:9092`
- No database, Docker, Lombok, Avro, or Schema Registry

## High-level architecture

```text
+------------------+
| Postman / Client |
+--------+---------+
         |
         | HTTP POST JSON
         v
+--------------------------+
| JsonMessageController    |
| /publish-customer        |
+------------+-------------+
             |
             | Customer Java object
             v
+--------------------------+
| JsonKafkaProducer        |
| KafkaTemplate<String,    |
| Customer>                |
+------------+-------------+
             |
             | JsonSerializer
             | JSON / Kafka bytes
             v
+--------------------------+
| Kafka Broker             |
| localhost:9092           |
| customer-topic           |
+------------+-------------+
             |
             | Kafka record / bytes
             v
+--------------------------+
| JsonDeserializer         |
| Customer Java object     |
+------------+-------------+
             v
+--------------------------+
| JsonKafkaConsumer        |
| @KafkaListener           |
+------------+-------------+
             v
       Console output
```

## Project components

```text
com.example.kafkademo
|
+-- KafkaDemoApplication.java
|
+-- config
|   +-- KafkaTopicConfig.java
|
+-- model
|   +-- Customer.java
|
+-- producer
|   +-- JsonKafkaProducer.java
|
+-- consumer
|   +-- JsonKafkaConsumer.java
|
+-- controller
    +-- JsonMessageController.java
```

### `KafkaDemoApplication`

The main Spring Boot class. `@SpringBootApplication` enables component scanning and starts the embedded web server and Kafka infrastructure.

### `Customer`

The Java model sent through Kafka. It contains:

- `Long id`
- `String name`
- `String email`

It includes a no-argument constructor, an all-argument constructor, getters, setters, and `toString()`.

### `JsonMessageController`

Exposes the REST endpoint:

```text
POST /api/v1/kafka/publish-customer
```

Spring converts the incoming request JSON into a `Customer` object using `@RequestBody`.

### `JsonKafkaProducer`

Receives a `Customer` object from the controller and sends it with:

```java
kafkaTemplate.send(topicName, customer);
```

The producer uses `KafkaTemplate<String, Customer>` and does not manually convert the object to JSON.

### `KafkaTopicConfig`

Creates the `customer-topic` topic using a `NewTopic` bean:

- Partitions: `1`
- Replicas: `1`

The topic name is read from the `app.kafka.customer-topic` property.

### `JsonKafkaConsumer`

Listens to `customer-topic` with the `customer-group` consumer group. Spring Kafka converts the incoming JSON payload directly into a `Customer` object before calling the `consume()` method.

## End-to-end request flow

### 1. Client sends JSON

The client sends:

```json
{
  "id": 101,
  "name": "Anand",
  "email": "anand@gmail.com"
}
```

to:

```text
http://localhost:8080/api/v1/kafka/publish-customer
```

### 2. Spring creates a `Customer` object

The `@RequestBody` annotation uses Spring Web and Jackson to map the request JSON to a `Customer` Java object.

### 3. Controller calls the producer

`JsonMessageController` passes the object to `JsonKafkaProducer`.

### 4. Producer serializes the object

The configured `JsonSerializer` converts the object into JSON bytes.

```text
Customer object
      |
      v
JsonSerializer
      |
      v
JSON bytes
```

### 5. Kafka stores the record

The serialized record is sent to:

```text
Broker: localhost:9092
Topic: customer-topic
```

### 6. Consumer receives the record

The consumer group `customer-group` polls Kafka for records from `customer-topic`.

### 7. Consumer deserializes the JSON

The configured `JsonDeserializer` converts the JSON bytes back into a `Customer` object.

```text
JSON bytes
      |
      v
JsonDeserializer
      |
      v
Customer object
```

### 8. Consumer prints the object

The consumer prints:

```text
Customer received from Kafka: Customer{id=101, name='Anand', email='anand@gmail.com'}
```

## Serialization configuration

The producer configuration is defined in `application.properties`:

```properties
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer
```

The key is serialized as a string. The `Customer` value is serialized as JSON.

The application does not manually call a JSON library. Spring Kafka uses `JsonSerializer` automatically because it is configured as the producer value serializer.

## Deserialization configuration

The consumer configuration is:

```properties
spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.value-deserializer=org.springframework.kafka.support.serializer.JsonDeserializer
```

The key is converted back to a string. The value is converted back to a `Customer` object by `JsonDeserializer`.

## Trusted packages

```properties
spring.kafka.consumer.properties.spring.json.trusted.packages=com.example.kafkademo.model
```

JSON messages can include type information describing the Java class that should be created. Trusted packages tell Spring Kafka which packages are allowed during deserialization.

Restricting trusted packages is safer than trusting every package because it reduces the chance of creating unintended classes from untrusted message data.

## Kafka topic and consumer group

```properties
app.kafka.customer-topic=customer-topic
spring.kafka.consumer.group-id=customer-group
```

The topic is the named stream where customer records are stored. The consumer group identifies the consumers that share the work of reading records.

This example uses one consumer and one partition, so the consumer receives every record from `customer-topic`.

## Runtime sequence

```text
1. Start Kafka at localhost:9092.
2. Start the Spring Boot application.
3. Spring creates customer-topic if it does not already exist.
4. The REST endpoint becomes available on port 8080.
5. Postman sends a customer JSON payload.
6. Spring maps JSON to Customer.
7. JsonKafkaProducer sends Customer to KafkaTemplate.
8. JsonSerializer converts Customer to JSON bytes.
9. Kafka stores the record in customer-topic.
10. JsonKafkaConsumer receives the record.
11. JsonDeserializer converts JSON bytes to Customer.
12. The consumer prints the Customer object.
```

## Testing

Start the application with:

```bash
mvn spring-boot:run
```

Send this request from Postman:

```http
POST http://localhost:8080/api/v1/kafka/publish-customer
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

Expected REST response:

```text
Customer sent to Kafka successfully
```

Expected producer output:

```text
Producing Customer: Customer{id=101, name='Anand', email='anand@gmail.com'}
```

Expected consumer output:

```text
Customer received from Kafka: Customer{id=101, name='Anand', email='anand@gmail.com'}
```

## Design principles demonstrated

- Constructor injection is used in application components.
- `KafkaTemplate<String, Customer>` expresses the message types clearly.
- `JsonSerializer` handles producer-side JSON conversion.
- `JsonDeserializer` handles consumer-side object conversion.
- The controller accepts normal HTTP JSON using `@RequestBody`.
- The application does not manually build or parse JSON.
- The topic name is externalized into configuration.
- The example contains no database or unnecessary infrastructure.
