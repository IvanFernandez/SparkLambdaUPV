package windowedUse;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.log4j.Logger;

/**
 * This class will simulate how the reduceByKeyAndWindow works:
 * 1: declare a JavaDStream with a batch interval duration of 1 second
 * 2: it reads files that are moved to a hdfs directory
 * 3: it counts how many words are within each file
 * 4: for each window duration (5s,10s,15s) calculate the window for the sliding duration
 * 5: print to console how each window is changing
 */
public class ReduceByWindowExample {


    private static  Logger log4j = Logger.getLogger(ReduceByWindowExample.class);

    public ReduceByWindowExample() {
    }

    public static void main(String[] args) {
        // Streaming batch interval definition
        Duration streamingDuration = new Duration(1000L);

        // Definimos cuanto va a valer cada ventana
        Duration windowOfFiveSecondsDuration = new Duration(5000L);
        Duration windowOfTenSecondsDuration = new Duration(10000L);
        Duration windowOfFifteenSecondsDuration = new Duration(15000L);

        // Definimos el slide duration
        Duration slideDuration = new Duration(1000L);

        SparkConf sparkConf = new SparkConf();
        sparkConf.setMaster("local[4]");
        sparkConf.setAppName("reduceByWindowExampleTest");
        JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, streamingDuration);
        jssc.checkpoint("hdfs://sandbox.hortonworks.com:8020/user/root/tmp");
        JavaDStream<String> lines = jssc.textFileStream("hdfs://sandbox.hortonworks.com:8020/user/root/files/");

        Function<String, Long> getCountForRecord = new Function<String, Long>() {

            @Override
            public Long call(String line) throws Exception {
                Long words = Long.valueOf(line.split(" ").length);
                log4j.warn("Words in file: " + words);
                return words;
            }
        };

        JavaDStream<Long> getCountForRecordJavaDStream = lines.map(getCountForRecord);

        JavaDStream<Long> incrementalCountsByWindowOfFiveSeconds = getCountForRecordJavaDStream.reduceByWindow(new incrementNewValues(), new decrementOldValues(), windowOfFiveSecondsDuration, slideDuration);
        JavaDStream<Long> incrementalCountsByWindowOfTenSeconds = getCountForRecordJavaDStream.reduceByWindow(new incrementNewValues(), new decrementOldValues(), windowOfTenSecondsDuration, slideDuration);
        JavaDStream<Long> incrementalCountsByWindowOfFifteenSeconds = getCountForRecordJavaDStream.reduceByWindow(new incrementNewValues(), new decrementOldValues(), windowOfFifteenSecondsDuration, slideDuration);

        incrementalCountsByWindowOfFiveSeconds.foreachRDD(new ForeachWindowedRDD(windowOfFiveSecondsDuration));
        incrementalCountsByWindowOfTenSeconds.foreachRDD(new ForeachWindowedRDD(windowOfTenSecondsDuration));
        incrementalCountsByWindowOfFifteenSeconds.foreachRDD(new ForeachWindowedRDD(windowOfFifteenSecondsDuration));

        jssc.start();
        jssc.awaitTermination();
    }


    private static class incrementNewValues implements Function2<Long, Long, Long> {
        @Override
        public Long call(Long v1, Long v2) throws Exception {
            return v1 + v2;
        }
    }


    private static class decrementOldValues implements Function2<Long, Long, Long> {
        @Override
        public Long call(Long v1, Long v2) throws Exception {
            return v1 - v2;
        }
    }
}
