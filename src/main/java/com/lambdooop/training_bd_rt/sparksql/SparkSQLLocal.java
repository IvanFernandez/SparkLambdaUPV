package com.lambdooop.training_bd_rt.sparksql;

import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.api.java.JavaSQLContext;
import org.apache.spark.sql.api.java.JavaSchemaRDD;
import org.apache.spark.sql.api.java.Row;

import com.lambdooop.training_bd_rt.utils.LambdaConstants;

import twitter4j.internal.logging.Logger;

/**
 * 
 * @author Lambdoop
 * 
 * Option 1) Run class as a Java Application within Eclipse
 * Option 2) Compile project from the root folder with maven:
 * 	- mvn clean install -DskipTests
 *  - Run spark with the following command: 
 *  	spark-submit --class com.lambdooop.training_bd_rt.sparksql.SparkSQLLocal$run target/training-bd-rt-0.1.0-SNAPSHOT.jar
 *	- Check console to notice how values from batch and streaming are queried.
 *  
 */
public class SparkSQLLocal {

	private static Logger log4j = Logger.getLogger(SparkSQLLocal.class);
	
	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setMaster("local[2]").setAppName(
				"SparkSQLLocal");
		JavaSparkContext jsc = new JavaSparkContext(conf);
		JavaSQLContext sqlContext = new JavaSQLContext(jsc);

		JavaRDD<WordCountTuple> wordCountTuple = jsc.textFile(LambdaConstants.getLocalBatchOutputPath() + "*").map(new Function<String, WordCountTuple>() {
			@Override
			public WordCountTuple call(String line) throws Exception {
				String[] keyValue = line.replace("(", "").replace(")","").trim().split(",");
				WordCountTuple wct = new WordCountTuple();
				wct.setKey(keyValue[0]);
				try {
					wct.setRepetitions(Integer.valueOf(keyValue[1]));
				}
				catch(Exception e) {	//errores de formato ...
					//log4j.error(e.getMessage());
				}
				return wct;
			}
		});
		
		JavaRDD<WordCountTuple> wordCountTupleStreaming = jsc.textFile(LambdaConstants.getLocalStreamingOutputPath() + "**/*").map(new Function<String, WordCountTuple>() {
			@Override
			public WordCountTuple call(String line) throws Exception {
				String[] keyValue = line.replace("(", "").replace(")","").trim().split(",");
				WordCountTuple wct = new WordCountTuple();
				wct.setKey(keyValue[0]);
				try {
					wct.setRepetitions(Integer.valueOf(keyValue[1]));
				}
				catch(Exception e) {
					//log4j.error(e.getMessage());
				}
				return wct;
			}
		});
		
		JavaSchemaRDD schemaWordCount = sqlContext.applySchema(wordCountTuple, WordCountTuple.class);
		schemaWordCount.registerTempTable("wordCounts");
		
		
		JavaSchemaRDD schemaWordCountStreaming = sqlContext.applySchema(wordCountTupleStreaming, WordCountTuple.class);
		schemaWordCountStreaming.registerTempTable("wordCountsStreaming");
		
		// Cuidado con poner como nombre del esquema una palabra reservada de SQL 
		JavaSchemaRDD moreThan10000 = sqlContext.sql("SELECT wc.key, wc.repetitions, wcs.repetitions FROM wordCounts as wc  LEFT JOIN wordCountsStreaming as wcs ON wc.key = wcs.key WHERE wc.repetitions > 10000");
		
		List<String> moreThan10000Words = moreThan10000.map(new Function<Row, String>() {
			public String call (Row row) {
				String clave = row.getString(0);
				int batchCount = row.getInt(1);
				int streamingCount = 0;
				if (!row.isNullAt(2)) {
					streamingCount = row.getInt(2);
				}
				//log4j.warn(clave + " : " + batchCount + " " + streamingCount);
				int total = batchCount + streamingCount;
				return clave + " : " + total;
			}
		}).collect();
		
		for (String word : moreThan10000Words) {
			log4j.warn(word);
		}
	}

}
