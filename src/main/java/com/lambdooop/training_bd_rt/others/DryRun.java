package com.lambdooop.training_bd_rt.others;

import java.util.Map;
import java.util.UUID;

import kafka.serializer.StringDecoder;

import org.apache.spark.SparkConf;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaPairReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;

import com.google.common.collect.ImmutableMap;


/**
 * # kafka-topics.sh --zookeeper localhost:2181 --list
 * # src/test/resources/kafka_dry_run.sh 
Usage src/test/resources/kafka_dry_run.sh [consumer | producer | create_topic] <topic>
 * 
 * 
 src/test/resources/kafka_dry_run.sh create_topic dry_run
 src/test/resources/kafka_dry_run.sh producer dry_run
 src/test/resources/kafka_dry_run.sh consumer dry_run
 * */
public class DryRun {
	public static void main (String [] args) {
		final String topic = "dry_run";
		
		SparkConf conf = new SparkConf().setMaster("local[5]")
										.setAppName(DryRun.class.getName());
		JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(1));
		
		final String consumerGroup = "kakfaCG_" + System.currentTimeMillis() + UUID.randomUUID().toString().replace('-', '_'); 
		Map<String, String> kafkaParams = ImmutableMap.of(
                								"zookeeper.connect", "localhost:2181",
                								"group.id", consumerGroup);
		JavaPairReceiverInputDStream<String, String> sequentialKafkaInputDStream =
                KafkaUtils.createStream(jssc,
                        String.class, String.class, StringDecoder.class, StringDecoder.class,
                        kafkaParams,
                        ImmutableMap.of(topic, 1), // one kafka consumer (thread) per receiver
                        StorageLevel.MEMORY_ONLY_2()); 
		sequentialKafkaInputDStream.print();
		
		jssc.start();              // Start the computation
		jssc.awaitTermination();   // Wait for the computation to terminate
	}
}
