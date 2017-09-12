package com.epam.mentoring.highload;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;

public class WordCountTask

{
    private static final Logger LOGGER = LoggerFactory.getLogger(WordCountTask.class);

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("you need file name");
            System.exit(1);
        }
        new WordCountTask().run(args[1]);
    }

    public void run(String inputFilePath) {
        String master = "local[2]";

        SparkConf conf = new SparkConf()
                .setAppName(WordCountTask.class.getName())
                .setMaster(master);
        JavaSparkContext context = new JavaSparkContext(conf);

        JavaRDD<String> lines = context.textFile(inputFilePath, 1);

        JavaRDD<String> words = lines.flatMap((FlatMapFunction<String, String>) s -> Arrays.asList(s.split(" ")).iterator());

        JavaPairRDD<String, Integer> count = words.mapToPair((PairFunction<String, String, Integer>) s -> new Tuple2<>(s, 1)).reduceByKey((Function2<Integer, Integer, Integer>) (x, y) -> x + y);

        //Count more 5 and length more 3
        count = count.filter(v1 -> v1._2 > 5 && v1._1.length() > 3);

        List<Tuple2<String, Integer>> output = count.collect();

        LOGGER.info("\n>>>RESULTS\n");
        output.forEach(t -> LOGGER.info(t._1() + ":" + t._2()));
        LOGGER.info("\n<<<RESULTS\n");

        context.close();
    }
}

