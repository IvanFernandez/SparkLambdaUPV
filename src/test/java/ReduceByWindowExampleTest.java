

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.junit.Before;
import org.junit.Test;

import windowedUse.ReduceByWindowExample;

import java.io.Serializable;

/**
 * A wannabe test for reducebyWindow
 */
public class ReduceByWindowExampleTest implements Serializable{

    /**
     * A test to this method has not been created because we still have not a way to generate
     * randon streams, list of records in different interval,
     * so we can test how reduceByWindow works running this test
     * and moving files to hdfs folder: /user/root/files/ using the following fs command:
     * hadoop fs -put 10.txt hdfs://sandbox.hortonworks.com:8020/user/root/files/
     *
     * A output of this test (moving a file of 10 words to hdfs) is:
     *
     15/02/13 04:32:24 WARN ForeachWindowedRDD:  Tuple Incremental variation for 5.0 s is 10
     15/02/13 04:32:24 WARN ForeachWindowedRDD:  Tuple Incremental variation for 10.0 s is 10
     15/02/13 04:32:24 WARN ForeachWindowedRDD:  Tuple Incremental variation for 15.0 s is 10
     15/02/13 04:32:25 WARN ForeachWindowedRDD:  Tuple Incremental variation for 5.0 s is 10
     15/02/13 04:32:25 WARN ForeachWindowedRDD:  Tuple Incremental variation for 10.0 s is 10
     15/02/13 04:32:25 WARN ForeachWindowedRDD:  Tuple Incremental variation for 15.0 s is 10
     15/02/13 04:32:26 WARN ForeachWindowedRDD:  Tuple Incremental variation for 5.0 s is 10
     15/02/13 04:32:26 WARN ForeachWindowedRDD:  Tuple Incremental variation for 10.0 s is 10
     15/02/13 04:32:26 WARN ForeachWindowedRDD:  Tuple Incremental variation for 15.0 s is 10
     15/02/13 04:32:27 WARN ForeachWindowedRDD:  Tuple Incremental variation for 5.0 s is 10
     15/02/13 04:32:27 WARN ForeachWindowedRDD:  Tuple Incremental variation for 10.0 s is 10
     15/02/13 04:32:27 WARN ForeachWindowedRDD:  Tuple Incremental variation for 15.0 s is 10
     15/02/13 04:32:28 WARN ForeachWindowedRDD:  Tuple Incremental variation for 5.0 s is 10
     15/02/13 04:32:28 WARN ForeachWindowedRDD:  Tuple Incremental variation for 10.0 s is 10
     15/02/13 04:32:28 WARN ForeachWindowedRDD:  Tuple Incremental variation for 15.0 s is 10
     15/02/13 04:32:29 WARN ForeachWindowedRDD:  Tuple Incremental variation for 5.0 s is 0
     15/02/13 04:32:29 WARN ForeachWindowedRDD:  Tuple Incremental variation for 10.0 s is 10
     15/02/13 04:32:29 WARN ForeachWindowedRDD:  Tuple Incremental variation for 15.0 s is 10
     15/02/13 04:32:30 WARN ForeachWindowedRDD:  Tuple Incremental variation for 5.0 s is 0
     15/02/13 04:32:30 WARN ForeachWindowedRDD:  Tuple Incremental variation for 10.0 s is 10
     15/02/13 04:32:30 WARN ForeachWindowedRDD:  Tuple Incremental variation for 15.0 s is 10
     15/02/13 04:32:31 WARN ForeachWindowedRDD:  Tuple Incremental variation for 5.0 s is 0
     15/02/13 04:32:31 WARN ForeachWindowedRDD:  Tuple Incremental variation for 10.0 s is 10
     15/02/13 04:32:31 WARN ForeachWindowedRDD:  Tuple Incremental variation for 15.0 s is 10
     15/02/13 04:32:32 WARN ForeachWindowedRDD:  Tuple Incremental variation for 5.0 s is 0
     15/02/13 04:32:32 WARN ForeachWindowedRDD:  Tuple Incremental variation for 10.0 s is 10
     15/02/13 04:32:32 WARN ForeachWindowedRDD:  Tuple Incremental variation for 15.0 s is 10
     15/02/13 04:32:33 WARN ForeachWindowedRDD:  Tuple Incremental variation for 5.0 s is 0
     15/02/13 04:32:33 WARN ForeachWindowedRDD:  Tuple Incremental variation for 10.0 s is 10
     15/02/13 04:32:33 WARN ForeachWindowedRDD:  Tuple Incremental variation for 15.0 s is 10
     15/02/13 04:32:34 WARN ForeachWindowedRDD:  Tuple Incremental variation for 5.0 s is 0
     15/02/13 04:32:34 WARN ForeachWindowedRDD:  Tuple Incremental variation for 10.0 s is 0
     15/02/13 04:32:34 WARN ForeachWindowedRDD:  Tuple Incremental variation for 15.0 s is 10
     15/02/13 04:32:35 WARN ForeachWindowedRDD:  Tuple Incremental variation for 5.0 s is 0
     15/02/13 04:32:35 WARN ForeachWindowedRDD:  Tuple Incremental variation for 10.0 s is 0
     15/02/13 04:32:35 WARN ForeachWindowedRDD:  Tuple Incremental variation for 15.0 s is 10
     15/02/13 04:32:36 WARN ForeachWindowedRDD:  Tuple Incremental variation for 5.0 s is 0
     15/02/13 04:32:36 WARN ForeachWindowedRDD:  Tuple Incremental variation for 10.0 s is 0
     15/02/13 04:32:36 WARN ForeachWindowedRDD:  Tuple Incremental variation for 15.0 s is 10
     15/02/13 04:32:37 WARN ForeachWindowedRDD:  Tuple Incremental variation for 5.0 s is 0
     15/02/13 04:32:37 WARN ForeachWindowedRDD:  Tuple Incremental variation for 10.0 s is 0
     15/02/13 04:32:37 WARN ForeachWindowedRDD:  Tuple Incremental variation for 15.0 s is 10
     15/02/13 04:32:38 WARN ForeachWindowedRDD:  Tuple Incremental variation for 5.0 s is 0
     15/02/13 04:32:38 WARN ForeachWindowedRDD:  Tuple Incremental variation for 10.0 s is 0
     15/02/13 04:32:38 WARN ForeachWindowedRDD:  Tuple Incremental variation for 15.0 s is 10
     15/02/13 04:32:39 WARN ForeachWindowedRDD:  Tuple Incremental variation for 5.0 s is 0
     15/02/13 04:32:39 WARN ForeachWindowedRDD:  Tuple Incremental variation for 10.0 s is 0
     15/02/13 04:32:39 WARN ForeachWindowedRDD:  Tuple Incremental variation for 15.0 s is 0

     _______________________________________________________________________________________________

     Explanation:

     * We can see taking a look to the timestamp that in t=0 (04:32:24) each window has a value of 10.
        15/02/13 04:32:24 WARN ForeachWindowedRDD:  Tuple Incremental variation for 5.0 s is 10
        15/02/13 04:32:24 WARN ForeachWindowedRDD:  Tuple Incremental variation for 10.0 s is 10
        15/02/13 04:32:24 WARN ForeachWindowedRDD:  Tuple Incremental variation for 15.0 s is 10
     * In t=5 (5 seconds after) the window related to 5 is completely degraded and values 0
        15/02/13 04:32:29 WARN ForeachWindowedRDD:  Tuple Incremental variation for 5.0 s is 0
        15/02/13 04:32:29 WARN ForeachWindowedRDD:  Tuple Incremental variation for 10.0 s is 10
        15/02/13 04:32:29 WARN ForeachWindowedRDD:  Tuple Incremental variation for 15.0 s is 10
     * In t=10 (10 seconds after) the window related to 10 is completely degraded and values 0
        15/02/13 04:32:34 WARN ForeachWindowedRDD:  Tuple Incremental variation for 5.0 s is 0
        15/02/13 04:32:34 WARN ForeachWindowedRDD:  Tuple Incremental variation for 10.0 s is 0
        15/02/13 04:32:34 WARN ForeachWindowedRDD:  Tuple Incremental variation for 15.0 s is 10
     * In t=15 (15 seconds after) each window values 0
        15/02/13 04:32:39 WARN ForeachWindowedRDD:  Tuple Incremental variation for 5.0 s is 0
        15/02/13 04:32:39 WARN ForeachWindowedRDD:  Tuple Incremental variation for 10.0 s is 0
        15/02/13 04:32:39 WARN ForeachWindowedRDD:  Tuple Incremental variation for 15.0 s is 0
     */
    @Test
    public void simpleTest() {
        ReduceByWindowExample.main(null);
    }
}
