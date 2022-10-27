package com.xc.job;


import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;


public class KafkaStreamSinkTest {
    public static void main(String[] args) throws Exception {

        //创建流环境
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //kafka配置
        KafkaSource<String> source = KafkaSource.<String>builder()
                .setBootstrapServers("localhost:9093")
                .setTopics("mydb_products_test1")                   //topic订阅
                .setGroupId("my-group")                             //消费组
                .setStartingOffsets(OffsetsInitializer.earliest())  //起始消费位点
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();
        //生成数据流
        DataStreamSource<String> data = env.fromSource(source, WatermarkStrategy.noWatermarks(), "Kafka Source");

        data.print();
        env.execute();
    }
}