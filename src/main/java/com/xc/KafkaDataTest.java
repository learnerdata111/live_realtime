package com.xc;

import org.apache.kafka.clients.consumer.*;
import java.util.Collections;
import java.util.Properties;

public class KafkaDataTest {
    public static void main(String[] args) throws Exception {

        try {
            Properties props = new Properties();
            props.put("bootstrap.servers", "localhost:9092");//kafka集群的服务器地址
//            props.put("group.id", "consumer-1-660a9fd0-5ea7-432b-8914-988f3b99650e");
            props.put("enable.auto.commit", "true");
            props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

            consumer.subscribe(Collections.singletonList("binlog_data_pipe"));
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(100);
                System.out.println(consumer);

                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("comsumer:>>>>>offset = %d, key= %s , value = %s\n", record.offset(),
                            record.key(), record.value());
                    String r = record.offset() + "\t" + record.key() + "\t" + record.value();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("when calling kafka output error." + ex.getMessage());
        }
    }
}
