package com.lambdooop.training_bd_rt.streaming;

import java.util.Arrays;

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
 *  	spark-submit --class com.lambdooop.training_bd_rt.streaming.ExporterLocalSparkStreaming$run target/training-bd-rt-0.1.0-SNAPSHOT.jar
 * 
 *  Copy some files (training-bd-rt/solutions/src/main/resources/incremental.txt) to LambdaConstants.getLocalStreamingInputPath() (check if you have permissions)
 *  cp training-bd-rt/solutions/src/main/resources/incremental.txt LambdaConstants.getLocalStreamingInputPath()
 *  Check LambdaConstants.getLocalStreamingOutputPath() directory where mapreduce will be saved
 *  
 */
public class ExporterLocalSparkStreaming {
	private static Logger logger = Logger.getLogger(ExporterLocalSparkStreaming.class);
	
	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setMaster("local[2]").setAppName("ExporterLocalSparkStreaming");
		JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(1));
		
		// por si no funciona HDFS ...
		JavaDStream<String> lineas = jssc.textFileStream(LambdaConstants.getLocalStreamingInputPath());

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

		
		Function<JavaPairRDD<String, Integer>, Void> foreachLocalFunc = new Function<JavaPairRDD<String, Integer>, Void>() {

			@Override
			public Void call(JavaPairRDD<String, Integer> resultados)
					throws Exception {
				// como no hay un rdd.isEmpty vamos a escribir todo el rato a HDFS (aunque no haya nada)
				// esto se puede subsanar de manera un poco ñapera.
				//resultados.saveAsTextFile("file:///root/tmp/spark/output/streaming" + String.valueOf(Time.now()));	// El fichero no tiene que existir. De hecho si existe da un error
				resultados.saveAsTextFile(LambdaConstants.getLocalStreamingOutputPath() + String.valueOf(Time.now()));	// El fichero no tiene que existir. De hecho si existe da un error
				return null;
			}
			
		};
		wordCounts.foreach(foreachLocalFunc);

	
		jssc.start();
		jssc.awaitTermination();
	}

}
