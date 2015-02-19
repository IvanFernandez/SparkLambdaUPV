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

public class ExporterHDFSSparkBatch {
	private static Logger logger = Logger
			.getLogger(ExporterLocalSparkStreaming.class);

	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setMaster("local[2]").setAppName(
				"ExporterHDFSSparkBatch");
		JavaSparkContext jsc = new JavaSparkContext(conf);

		// por si no funciona HDFS ...
		JavaRDD<String> files = jsc.textFile(LambdaConstants.getHdfsPath());

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

		wordCounts.saveAsTextFile(LambdaConstants.getHdfsPath());

	}
}
