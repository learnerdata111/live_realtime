package com.xc.bigdata;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

/**
 * 批处理的word count
 *
 * @author wxg
 */
public class BatchWordCount {
    public static void main(String[] args) throws Exception {
        //获取Flink批处理执行环境
        ExecutionEnvironment environment = ExecutionEnvironment.getExecutionEnvironment();

        //从文件中获取数据源
        final String fileName = "/Users/yuyan/Downloads/数据仓库/实时计算/live_realtime/src/main/resources/WorldCount.txt" ;
        DataSource<String> dataSource = environment.readTextFile(fileName);
        //单词计数
        dataSource
                //将一行句子按照空格拆分,输入一个字符串,输出一个2元组,key为一个单词,value为1
                .flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
                    public void flatMap(String s, Collector<Tuple2<String, Integer>> collector) throws Exception {
                        //对读取到的每一行数据按照空格分割
                        String[] split = s.split(" ");
                        //将每个单词放入collector中作为输出,格式类似于{word:1}
                        for (String word : split) collector.collect(new Tuple2<String, Integer>(word, 1));
                    }
                })
                //聚合算子,按照第一个字段(即word字段)进行分组
                .groupBy(0)
                //聚合算子,对每一个分租内的数据按照第二个字段进行求和
                .sum(1)
                //打印结果到控制台
                .print();

    }
}

