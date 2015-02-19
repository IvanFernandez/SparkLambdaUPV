package com.lambdooop.training_bd_rt.sparksql;

import java.io.Serializable;

/**
 * 
 * @author Lambdoop
 * 
 * A simple POJO for a Word Count model.
 * Check more about model inferring in SparkSQL in the following link:
 * http://spark.apache.org/docs/1.2.0/sql-programming-guide.html#rdds
 */
public class WordCountTuple implements Serializable{
	private String key;
	private int repetitions;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public int getRepetitions() {
		return repetitions;
	}
	public void setRepetitions(int count) {
		this.repetitions = count;
	}
	
	public String toString() {
		return "[" + key + "] = " + repetitions; 
	}
	
	
}
