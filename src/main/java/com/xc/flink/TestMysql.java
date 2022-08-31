package com.xc.flink;
import org.apache.kafka.clients.consumer.*;

import java.util.*;


public class TestMysql {
    public static void main(String[] args) throws Exception {

            Properties properties=new Properties();
            properties.put("bootstrap.servers","localhost:9092");
            properties.put("group.id", "test-consumer-group");
            properties.put("enable.auto.commit", "true");
            properties.put("auto.commit.interval.ms", "1000");
            properties.put("auto.offset.reset", "latest");
            properties.put("session.timeout.ms", "30000");
            properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

            KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<String, String>(properties);
            kafkaConsumer.subscribe(Arrays.asList("binlog_data_pipe"));

            while (true) {
                /**
                 * 消费者必须持续对Kafka进行轮询，否则会被认为已经死亡，他的分区会被移交给群组里的其他消费者。
                 * poll返回一个记录列表，每个记录包含了记录所属主题的信息，
                 * 记录所在分区的信息，记录在分区里的偏移量，以及键值对。
                 * poll需要一个指定的超时参数，指定了方法在多久后可以返回。
                 * 发送心跳的频率，告诉群组协调器自己还活着。
                 */
                ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
                for (ConsumerRecord<String, String> record : records) {
                    //Thread.sleep(1000);
                    System.out.printf("offset = %d, value = %s", record.offset(), record.value());
                    System.out.println();
                }
            }



    }
}