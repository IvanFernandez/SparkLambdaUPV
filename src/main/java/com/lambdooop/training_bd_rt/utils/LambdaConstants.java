package com.lambdooop.training_bd_rt.utils;

public class LambdaConstants {
	
	private static String HdfsPath = "hdfs://localhost:8020/home/profesor/files/";
//	private static String localStreamingInputPath = "file:///root/tmp/spark/input/streaming/";
//	private static String localStreamingOutputPath = "file:///root/tmp/spark/output/streaming/";
//	private static String localBatchInputPath = "file:///root/tmp/spark/input/batch/";
//	private static String localBatchOutputPath = "file:///root/tmp/spark/output/batch/";
	
	private static String localStreamingInputPath = "file:///home/profesor/tmp/spark/input/streaming/";
	private static String localStreamingOutputPath = "file:///home/profesor/tmp/spark/output/streaming/";
	private static String localBatchInputPath = "file:///home/profesor/tmp/spark/input/batch/";
	private static String localBatchOutputPath = "file:///home/profesor/tmp/spark/output/batch/";
	
	public static String getHdfsPath() {
		return HdfsPath;
	}

	public static void setHdfsPath(String hdfsPath) {
		HdfsPath = hdfsPath;
	}

	public static String getLocalStreamingInputPath() {
		return localStreamingInputPath;
	}

	public static void setLocalStreamingInputPath(String localStreamingInputPath) {
		LambdaConstants.localStreamingInputPath = localStreamingInputPath;
	}

	public static String getLocalStreamingOutputPath() {
		return localStreamingOutputPath;
	}

	public static void setLocalStreamingOutputPath(String localStreamingOutputPath) {
		LambdaConstants.localStreamingOutputPath = localStreamingOutputPath;
	}

	public static String getLocalBatchInputPath() {
		return localBatchInputPath;
	}

	public static void setLocalBatchInputPath(String localBatchInputPath) {
		LambdaConstants.localBatchInputPath = localBatchInputPath;
	}

	public static String getLocalBatchOutputPath() {
		return localBatchOutputPath;
	}

	public static void setLocalBatchOutputPath(String localBatchOutputPath) {
		LambdaConstants.localBatchOutputPath = localBatchOutputPath;
	}

}
