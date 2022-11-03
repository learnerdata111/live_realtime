package com.xc.job;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xc.model.ProductJson;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.util.Collector;

import java.util.Properties;

import static java.lang.System.*;


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


        //json流
        DataStreamSource<String> stream = env.addSource(new FlinkKafkaConsumer<String>(
                "mydb_products",
                new SimpleStringSchema(),
                properties
        ));

        //转换成javabean
        DataStream<ProductJson.DataDTO> data_stram = stream.flatMap(new FlatMapFunction<String, ProductJson.DataDTO>() {
            @Override
            public void flatMap(String s, Collector<ProductJson.DataDTO> out) throws Exception {
                Gson gson = new Gson();
                ProductJson product = gson.fromJson(s, new TypeToken<ProductJson>() {
                }.getType());
                for(ProductJson.DataDTO record:product.getData()){
                    out.collect(record);
                }
            }
        });


        data_stram.print();
        //写入到mysql
        data_stram.addSink(new SinkToMySQL());

//        data_stram.addSink(JdbcSink.sink("INSERT INTO products_test (id, name,description) VALUES (?,?,?)",
//                (statement, r) -> {
//                    statement.setString(1, r.getId());
//                    statement.setString(2, r.getName());
//                    statement.setString(3, r.getDescription());
//                    statement.executeUpdate();
//                },
//                new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
//                        .withUrl("jdbc:mysql://localhost:3306/mydb?useUnicode=true&characterEncoding=utf-8&useSSL=false")
//                        .withDriverName("com.mysql.cj.jdbc.Driver")
//                        .withUsername("root")
//                        .withPassword("123")
//                        .build())
//                );


        env.execute();
    }

}