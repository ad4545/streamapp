package com.example;


import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;

public class SimpleStreamsApp {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "simple-streams-app");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "ec2-13-201-94-176.ap-south-1.compute.amazonaws.com:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        StreamsBuilder builder = new StreamsBuilder();

        Map<String, String> hardcodedMap = new HashMap<>();
        hardcodedMap.put("Communication", "[\"https://rukminim2.flixcart.com/fk-p-flap/1600/270/image/1b011c536d905180.jpg?q=20\"]");
        hardcodedMap.put("Wearables", "[\"https://rukminim2.flixcart.com/fk-p-flap/1600/270/image/04bb152bb2d3f429.jpg?q=20\"]");
        hardcodedMap.put("Clothing", "[\"https://rukminim2.flixcart.com/fk-p-flap/1600/270/image/8b4e46e572ce96b2.jpg?q=20\"]");

        KStream<String, String> source = builder.stream("productevent");

        KStream<String, String> processedStream = source.mapValues(value -> {
            // Match the input value with keys in the map
            String result = hardcodedMap.get(value);
            return result != null ? result : "[]"; // Return the matched value or empty array if not found
        });
        
        processedStream.to("adevent", Produced.with(Serdes.String(), Serdes.String()));


        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }
}