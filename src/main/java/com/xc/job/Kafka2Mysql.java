package com.xc.job;


import com.alibaba.fastjson.JSONObject;
import com.xc.model.Products;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.util.Properties;


public class Kafka2Mysql {



    public static void main(String[] args) throws Exception {

        //创建流环境
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //设置并行度
        env.setParallelism(1);
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

        if (stream != null) {
            Products products = JSONObject.parseObject(String.valueOf(stream), Products.class);
        }
        products



//        stream.addSink(
//                JdbcSink.sink(
//                        "INSERT INTO products (id, name,description) VALUES (?,?,?)",
//                        (statement, r) -> {
//                            statement.setString(1, r.id);
//                            statement.setString(2, r.name);
//                            statement.setString(3, r.description);
//
//                        },
//                        new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
//                                .withUrl("jdbc:mysql://localhost:3306/mydb")
//                                .withDriverName("com.mysql.cj.jdbc.Driver")
//                                .withUsername("root")
//                                .withPassword("123")
//                                .build()
//                )
//        );

        env.execute();
    }

}