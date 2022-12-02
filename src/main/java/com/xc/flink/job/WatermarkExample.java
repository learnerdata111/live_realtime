package com.xc.flink.job;

import com.xc.flink.model.AnchorOrderTimeJson;
import com.xc.flink.model.ItemCount;
import com.xc.flink.process.amount.AmountCountTimeJsonFlatMap;
import com.xc.flink.process.water.OrderTimeApply;
import com.xc.flink.process.water.OrderTimeWaterStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.util.Properties;

public class WatermarkExample {


    //直接在数据源上使用
    //key state 类型
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
//       分配时间和水位线
//       kafkaSource.assignTimestampsAndWatermarks(
//         WatermarkStrategy
//       .forBoundedOutOfOrderness(Duration.ofSeconds(20)));
        //json流
        //水位线策略
        DataStream stream = env.addSource(kafkaSource).name("add_source");
        //内置水位线
        //无序流的watermark生成 --设置延迟事件2s
//        DataStream<AnchorOrderTimeJson.DataDTO> flatmap_stream = stream.flatMap(new AmountCountTimeJsonFlatMap())
//                .assignTimestampsAndWatermarks(WatermarkStrategy.<AnchorOrderTimeJson.DataDTO>forBoundedOutOfOrderness(Duration.ofSeconds(2))
//                .withTimestampAssigner( (event, timestamp) -> event.getOrderTime()*1000L
//            ));

        DataStream<AnchorOrderTimeJson.DataDTO> flatmap_stream = stream.flatMap(new AmountCountTimeJsonFlatMap())
                .assignTimestampsAndWatermarks(new OrderTimeWaterStrategy());

        //根据订单时间的窗口统计 TumblingEventTimeWindows
        //基于窗口处理时间统计 TumblingProcessingTimeWindows
//        SingleOutputStreamOperator<BigDecimal> apply_stream = flatmap_stream.keyBy(value ->value.getAnchorId())
//                .window(TumblingProcessingTimeWindows.of(Time.seconds(5)))
//                .aggregate(new OrderTimeAggreate());

        SingleOutputStreamOperator<ItemCount> apply_stream = flatmap_stream.keyBy(value ->value.getAnchorId())
                .window(TumblingEventTimeWindows.of(Time.seconds(5)))
                .apply(new OrderTimeApply());

        apply_stream.print();
        env.execute("waterExample");

    }




}
