package com.xc.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.concurrent.ExecutionException;


public class Producer1 {
    public static void main(String[] args) throws Exception {

        // 1. 创建 kafka 生产者的配置对象
        Properties properties = new Properties();
        // 2. 给 kafka 配置对象添加配置信息：bootstrap.servers
        //哭了 我又哭了
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9093");

        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        // 3. 创建 kafka 生产者对象
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(properties);
        System.out.println("开始发送数据");
             // 4. 调用 send 方法,发送消息
        for (int i = 0; i < 5; i++) {
            try {
                kafkaProducer.send(new ProducerRecord<>("java_test","a111111"+i )).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        // 5. 关闭资源
        kafkaProducer.close();
    }

}
