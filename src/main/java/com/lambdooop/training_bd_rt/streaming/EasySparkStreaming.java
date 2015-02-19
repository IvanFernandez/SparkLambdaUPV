package com.lambdooop.training_bd_rt.streaming;

import java.util.Arrays;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

/**
 * 
 * @author Lambdoop
 * 
 * Based on http://spark.apache.org/docs/1.2.0/streaming-programming-guide.html
 * 
 * Option 1) Run class as a Java Application within Eclipse
 * Option 2) Compile project from the root folder with maven:
 * 	- mvn clean install -DskipTests
 *  - Run spark with the following command: 
 *  	spark-submit --class com.lambdooop.training_bd_rt.streaming.EasySparkStreaming$run target/training-bd-rt-0.1.0-SNAPSHOT.jar
 * 
 *  The spark listener will be listening on port 9999. 
 *  Run nc -lk 9999
 *  And write something ...
 *  And see console ...
 *  
 */
public class EasySparkStreaming {
	
	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setMaster("local[2]").setAppName("UpvTest");
		JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(1));
		
		// Creamos un DStream que se va a conectar al puerto hostname:port
		JavaReceiverInputDStream<String> lines = jssc.socketTextStream("localhost", 9999);
		
		// Tokenizamos las líneas
		FlatMapFunction<String, String> tokenizarLineas = new FlatMapFunction<String, String>() {
			@Override
			public Iterable<String> call(String linea) throws Exception {
				String[] palabras = linea.split(" ");
				return Arrays.asList(palabras);
			}
			
		};
		
		// Separamos cada línea en palabras
		JavaDStream<String> words = lines.flatMap(tokenizarLineas);
		
		words.print();
		
		jssc.start();
		jssc.awaitTermination();
	}

}
