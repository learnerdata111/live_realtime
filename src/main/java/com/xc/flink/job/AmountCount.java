package com.xc.flink.job;


import com.xc.flink.model.AnchorOrderJson;
import com.xc.flink.model.ItemCount;
import com.xc.flink.model.ItemCountJoin;


import com.xc.flink.process.amount.*;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.ProcessingTimeSessionWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.util.Properties;


public class AmountCount {

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
        DataStream stream = env.addSource(new FlinkKafkaConsumer<String>(
                "mydb_anchor_order",
                new SimpleStringSchema(),
                properties
        ));
        //flatmap
        DataStream<AnchorOrderJson.DataDTO> flatmap_stream = stream.flatMap(new AmountCountJsonFlatMap());
        //map
        DataStream<AnchorOrderJson.DataDTO> map_stream = flatmap_stream.map(new AmountCountJsonMap());
        //filter 取主播id = 'a_2'
        DataStream<AnchorOrderJson.DataDTO> filter_stream = map_stream.filter(new AmountCountJsonFilter());

        //KeyBy
        KeyedStream<AnchorOrderJson.DataDTO, String> key_stream= filter_stream.keyBy(value -> value.getItemType());
        //KeyBy
        KeyedStream<AnchorOrderJson.DataDTO, String> fliter_key_stream= filter_stream.keyBy(value -> value.getItemType());

        // Reduce 根据商品类型进行分组
        DataStream<AnchorOrderJson.DataDTO> reduce_stream = key_stream.reduce(new AmountCountJsonReduce());

        //window
        //滚动窗口不重叠，指定时间长度
        DataStream<AnchorOrderJson.DataDTO> window_stream =
                filter_stream.keyBy(value -> value.getItemType())
                             .window(TumblingProcessingTimeWindows.of(Time.seconds(5)))
                             .reduce(new AmountCountJsonWindowReduce());

        //滑动计数窗口 --滑动窗口可以允许窗口重叠,大小为10s,滑动距离5s
        DataStream<AnchorOrderJson.DataDTO>  slide_stream =
                filter_stream.keyBy(value -> value.getItemType())
                        .window(TumblingProcessingTimeWindows.of(Time.seconds(10), Time.seconds(5)))
                        .reduce(new AmountCountJsonWindowReduce());

        //会话窗口  --设置时间间隔
        DataStream<AnchorOrderJson.DataDTO>  session_stream =
                filter_stream.keyBy(value -> value.getItemType())
                        .window(ProcessingTimeSessionWindows.withGap(Time.seconds(5)))
                        .reduce(new AmountCountJsonWindowReduce());
        //全局窗口 --不分区
        DataStream<AnchorOrderJson.DataDTO> windowall_stream =
                filter_stream.windowAll(TumblingProcessingTimeWindows.of(Time.seconds(5)))
                        .reduce(new AmountCountJsonWindowReduce());

        //窗口函数apply  作用在WindowedStream → DataStream --求和
        DataStream<ItemCount> apply_stream =  filter_stream.keyBy(value -> value.getItemType())
                .window(TumblingProcessingTimeWindows.of(Time.seconds(5)))
                .apply(new AmountCountJsonWindowApply());

        //全局函数apply  作用在allWindowedStream → DataStream --求和
        DataStream<ItemCount> apply_allstream = filter_stream
                .windowAll(TumblingProcessingTimeWindows.of(Time.seconds(5)))
                .apply(new AmountCountJsonAllWindowApply());

        //Union --数据合并
        DataStream<ItemCount> union_stream = apply_allstream.union(apply_stream);

        //Window Join
        DataStream<ItemCountJoin> join_stream =map_stream.join(filter_stream)
                .where(value->value.getItemType()).equalTo(value->value.getItemType())
                .window(TumblingProcessingTimeWindows.of(Time.seconds(5)))
                .apply(new AmountCountJsonJoinApply());

        //Interval Join KeyedStream,KeyedStream → DataStream #
        DataStream<ItemCountJoin> laterval_stream =key_stream.intervalJoin(fliter_key_stream)
                .between(Time.milliseconds(-5), Time.milliseconds(5))
                .process(new AmountCountJsonProcess());

        //comap  DataStream -> ConnectedStream → DataStream #
        DataStream<ItemCount> connectedStreams = apply_allstream.connect(laterval_stream)
                .map(new AmountCountJsonCoMap());

        //Iterate
        //随机分区
        //shuffle --随机
        //dataStream.rescale();   --轮询
        //dataStream.broadcast(); --广播

        DataStream<AnchorOrderJson.DataDTO> shuffle_datastream = window_stream.shuffle();

        //算在链接在一个线程可以提高性能
        DataStream<AnchorOrderJson.DataDTO> link_stream = stream
                .flatMap(new AmountCountJsonFlatMap())
                .map(new AmountCountJsonMap())
                .filter(new AmountCountJsonFilter());

        //AggregateFunction 输入数据到达窗口时直接进行增量聚合
        //ProcessWindowFunction 以及用来获取时间和状态信息的 Context 对象

        //流打印
        apply_stream.print();
        env.execute("AmountCount");
    }

}