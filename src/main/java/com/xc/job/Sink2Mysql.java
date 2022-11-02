package com.xc.job;


import com.xc.model.Products;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;

import java.util.Properties;


public class Sink2Mysql {
    public static void main(String[] args) throws Exception {

        //创建流环境
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setParallelism(1);

        DataStream<Products> stream = env.fromElements(
                new Products("1001","x","sinktomysql1"),
                new Products("1002","y","sinktomysql2"),
                new Products("1003","y","sinktomysql2") );

        stream.print();
        stream.addSink(
                JdbcSink.sink(
                        "INSERT INTO products_test (id, name,description) VALUES (?,?,?)",
                        (statement, r) -> {
                            statement.setString(1, r.getId());
                            statement.setString(2, r.getName());
                            statement.setString(3, r.getName());

                        },

                        new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()

                                .withUrl("jdbc:mysql://localhost:3306/mydb?useUnicode=true&characterEncoding=utf-8&useSSL=false")
                                .withDriverName("com.mysql.cj.jdbc.Driver")
                                .withUsername("root")
                                .withPassword("123")
                                .build()
                )
        );

        env.execute();
    }
}