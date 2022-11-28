package com.xc.flink.job;

import com.xc.flink.model.AnchorOrderJson;
import com.xc.flink.model.AnchorOrderTime;
import com.xc.flink.model.AnchorOrderTimeJson;
import com.xc.flink.process.AmountCountJsonFlatMap;
import com.xc.flink.process.AmountCountTimeJsonFlatMap;
import org.apache.flink.api.common.eventtime.WatermarkGenerator;
import org.apache.flink.api.common.eventtime.WatermarkGeneratorSupplier;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.time.Duration;
import java.util.Properties;

public class StateExample {
    //key state 类型
    public static void main(String[] args) throws Exception{

    }





}
