package windowedUse;

import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.streaming.Duration;

/**
 * This class will implement a Function<JavaRDD<Long>, Void> with a windowDuration field
 * to be able to print the window duration pass as a parameter in the constructor.
 */
public class ForeachWindowedRDD implements Function<JavaRDD<Long>, Void> {

    private Duration windowDuration;
    private static Logger log = Logger.getLogger(ForeachWindowedRDD.class);


    public ForeachWindowedRDD(Duration windowDuration) {
        this.windowDuration = windowDuration;
    }

    @Override
    public Void call(JavaRDD<Long> increment) throws Exception {
        try {
            String msg = buildMessage(increment.first());
            log.warn(msg);
            //FIXME: there isn't a rdd.isEmpty function, so a workaround to know if a rdd is empty
            // would be to catch an UnsupportedOperationException when, i.e, a first method is called
            // in a empty rdd
        } catch(UnsupportedOperationException uoe) {
            log.error(uoe.getMessage());
        }
        return null;
    }

    private String buildMessage(Long increment) {
        return " Tuple Incremental variation for " + windowDuration.prettyPrint() + " is " + increment;
    }
}
