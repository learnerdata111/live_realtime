package com.xc.flink.job;

import com.xc.flink.model.AnchorOrderTimeJson;
import com.xc.flink.process.amount.AmountCountTimeJsonFlatMap;
import com.xc.flink.process.state.CountFlatMap;
import com.xc.flink.process.water.OrderTimeWaterStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.util.Properties;

public class StateExample {
    public static void main(String[] args) throws Exception{
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

        FlinkKafkaConsumer<String> kafkaSource = new FlinkKafkaConsumer<String>(
                "mydb_anchor_order_time",
                new SimpleStringSchema(),
                properties
        );
        KeyedStream<AnchorOrderTimeJson.DataDTO,String> key_stream = env.addSource(kafkaSource).name("add_source")
                            .flatMap(new AmountCountTimeJsonFlatMap()).name("flatmap")
                            .assignTimestampsAndWatermarks(new OrderTimeWaterStrategy()).name("watermarks")
                            .keyBy(event->event.getAnchorId());

        //key-state 类型 --clear() --Descriptor
        //ValueState<T> --保存一个可以更新和检索的值
        //ListState<T> --保存一个元素的列表
        //ReducingState<T> --保存一个单值
        //AggregatingState<IN, OUT> --保留一个单值，表示添加到状态的所有值的聚合
        //MapState<UK, UV> --维护了一个映射列表
        //必须创建一个 StateDescriptor，才能得到对应的状态句柄,状态通过RuntimeContext进行访问，因此只能在richfunctions中使用
        //RichFunction 中 RuntimeContext 提供如下方法
        //ValueState<T> getState(ValueStateDescriptor<T>)
        //ReducingState<T> getReducingState(ReducingStateDescriptor<T>)
        //ListState<T> getListState(ListStateDescriptor<T>)
        //AggregatingState<IN, OUT> getAggregatingState(AggregatingStateDescriptor<IN, ACC, OUT>)
        //MapState<UK, UV> getMapState(MapStateDescriptor<UK, UV>)
        DataStream<Tuple2<String, Long>> f_stram =  key_stream.flatMap(new CountFlatMap());
        f_stram.print();
        env.execute("stateExample");



    }





}
