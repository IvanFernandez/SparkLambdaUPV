package com.lambdooop.training_bd_rt.batch;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

import com.lambdooop.training_bd_rt.streaming.ExporterLocalSparkStreaming;
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
 *  	spark-submit --class com.lambdooop.training_bd_rt.batch.ExporterLocalSparkBatch$run target/training-bd-rt-0.1.0-SNAPSHOT.jar
 * 
 *  Text files need to be previously copied to LambdaConstants.getLocalBatchInputPath() (check if you have permissions)
 *  Check LambdaConstants.getLocalBatchOutputPath() directory where output will be saved
 *  
 */
public class ExporterLocalSparkBatch {
	private static Logger logger = Logger
			.getLogger(ExporterLocalSparkBatch.class);

	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setMaster("local[2]").setAppName(
				"ExporterLocalSparkBatch");
		JavaSparkContext jsc = new JavaSparkContext(conf);

		// por si no funciona HDFS en el entorno...
		JavaRDD<String> files = jsc.textFile(LambdaConstants.getLocalBatchInputPath());

		// Tokenizamos las líneas
		FlatMapFunction<String, String> tokenizarLineasFunction = new FlatMapFunction<String, String>() {
			@Override
			public Iterable<String> call(String linea) throws Exception {
				String[] palabras = linea.split(" ");
				return Arrays.asList(palabras);
			}

		};

		// Split each line into words
		JavaRDD<String> words = files.flatMap(tokenizarLineasFunction);

		// Count each word in each batch
		// OJO: en el manual de Spark está mal pone que hay que hacer pero
		// realmente es un mapToPair
		JavaPairRDD<String, Integer> pairs = words
				.mapToPair(new PairFunction<String, String, Integer>() {
					@Override
					public Tuple2<String, Integer> call(String s)
							throws Exception {
						return new Tuple2<String, Integer>(s, 1);
					}
				});

		JavaPairRDD<String, Integer> wordCounts = pairs
				.reduceByKey(new Function2<Integer, Integer, Integer>() {
					@Override
					public Integer call(Integer i1, Integer i2)
							throws Exception {
						return i1 + i2;
					}
				});

		// OJO: si se vuelve a ejecutar, ¿qué pasa?
		wordCounts.saveAsTextFile(LambdaConstants.getLocalBatchOutputPath());

	}
}
