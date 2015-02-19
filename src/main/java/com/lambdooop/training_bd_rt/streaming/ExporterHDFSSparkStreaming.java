package com.lambdooop.training_bd_rt.streaming;

import java.util.Arrays;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.OutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Time;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import com.lambdooop.training_bd_rt.utils.LambdaConstants;

import scala.Tuple2;



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
 *  	spark-submit --class com.lambdooop.training_bd_rt.streaming.ExporterHDFSSparkStreaming$run target/training-bd-rt-0.1.0-SNAPSHOT.jar
 * 
 *  Copy some files (training-bd-rt/solutions/src/main/resources/file.txt)
 *  Copy some files to HDFS  hadoop fs -put training-bd-rt/solutions/src/main/resources/file.txt /user/root/files/
 *  Check hdfs:///user/root/files + timestamp to see that tuples have been written to HDFS.
 *  And see console ...
 *  
 */
public class ExporterHDFSSparkStreaming {
	
	private static Logger logger = Logger.getLogger(ExporterHDFSSparkStreaming.class);
	
	@SuppressWarnings({ "serial", "unchecked" })
	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setMaster("local[2]").setAppName("ExporterHDFSSparkStreaming");
		JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(5));
		
		// Creamos otro DStream que se va a conectar a HDFS
		JavaDStream<String> lineas = jssc.textFileStream(LambdaConstants.getHdfsPath());

		// Tokenizamos las líneas
		FlatMapFunction<String, String> tokenizarLineasFunction = new FlatMapFunction<String, String>() {
			@Override
			public Iterable<String> call(String linea) throws Exception {
				String[] palabras = linea.split(" ");
				return Arrays.asList(palabras);
			}
			
		};
	
		// Split each line into words
		JavaDStream<String> words = lineas.flatMap(tokenizarLineasFunction);

		// Count each word in each batch
		//OJO: en el manual de Spark está mal pone que hay que hacer pero realmente es un mapToPair
		JavaPairDStream<String, Integer> pairs = words.mapToPair(
		  new PairFunction<String, String, Integer>() {
		    @Override public Tuple2<String, Integer> call(String s) throws Exception {
		      return new Tuple2<String, Integer>(s, 1);
		    }
		  });
		
		JavaPairDStream<String, Integer> wordCounts = pairs.reduceByKey(
		  new Function2<Integer, Integer, Integer>() {
		    @Override public Integer call(Integer i1, Integer i2) throws Exception {
		      return i1 + i2;
		    }
		  });

		final Class<? extends OutputFormat<?,?>> outputFormatClass = (Class<? extends
				OutputFormat<?,?>>) (Class<?>) TextOutputFormat.class;
		
		Function<JavaPairRDD<String, Integer>, Void> foreachToHadoopFunc = new Function<JavaPairRDD<String, Integer>, Void>() {

			@Override
			public Void call(JavaPairRDD<String, Integer> resultados)
					throws Exception {
				resultados.saveAsNewAPIHadoopFile(LambdaConstants.getHdfsPath() + String.valueOf(Time.now()), Text.class, Text.class, outputFormatClass);
				return null;
			}
			
		};
		
		wordCounts.foreach(foreachToHadoopFunc);

	
		jssc.start();
		jssc.awaitTermination();
	}

}
