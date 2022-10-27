package com.xc.job;


import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;

import java.util.Properties;


public class Kafka2Mysql {
    public static void main(String[] args) throws Exception {

        //创建流环境
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //kafka环境
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9093");
        properties.setProperty("group.id", "consumer-group");
        properties.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty("auto.offset.reset", "earliest");
        env.setParallelism(1);

        //source流
        DataStreamSource<String> stream = env.addSource(new FlinkKafkaConsumer<String>(
                "mydb_products",
                new SimpleStringSchema(),
                properties
        ));
        //读取kafka独享转换成java对象

        //source流写入sink流
        stream.addSink(new FlinkKafkaProducer<String>(
                            "mydb_products_sink",
                             new SimpleStringSchema(),
                             properties
                ));
        env.execute();
    }
}