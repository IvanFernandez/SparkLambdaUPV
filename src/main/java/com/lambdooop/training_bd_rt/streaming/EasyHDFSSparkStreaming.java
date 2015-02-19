package com.lambdooop.training_bd_rt.streaming;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import com.lambdooop.training_bd_rt.utils.LambdaConstants;

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
 *  	spark-submit --class com.lambdooop.training_bd_rt.streaming.EasyHDFSSparkStreaming$run target/training-bd-rt-0.1.0-SNAPSHOT.jar
 * 
 *  The spark listener will be listening on adding some files to /user/root/files/ in hdfs
 *  Copy some files to HDFS  hadoop fs -put pom.xml /user/root/files/
 *  And see console ...
 *  
 */
public class EasyHDFSSparkStreaming {
	
	private static Logger logger = Logger.getLogger(EasyHDFSSparkStreaming.class);
	
	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setMaster("local[2]").setAppName("EasyHDFSSparkStreaming");
		JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(1));
		
		// Creamos otro DStream que se va a conectar a HDFS
		JavaDStream<String> lines = jssc.textFileStream(LambdaConstants.getHdfsPath());
		
		// Tokenizamos las líneas
		FlatMapFunction<String, String> tokenizarLineasFunction = new FlatMapFunction<String, String>() {
			@Override
			public Iterable<String> call(String linea) throws Exception {
				String[] palabras = linea.split(" ");
				return Arrays.asList(palabras);
			}
			
		};
		
		// Separamos cada línea en palabras
		JavaDStream<String> words = lines.flatMap(tokenizarLineasFunction);
		
		words.print();
		// o un foreach ... Ver http://spark.apache.org/docs/1.2.0/streaming-programming-guide.html#output-operations-on-dstreams
	
		jssc.start();
		jssc.awaitTermination();
	}

}
