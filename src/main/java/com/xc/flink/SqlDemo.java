package com.xc.flink;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;


public class SqlDemo {
    public static void main(String[] args) throws Exception {

        //创建执行环境-TableEnvironment
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        EnvironmentSettings settings = EnvironmentSettings.newInstance()
                                       .useBlinkPlanner()
                                       .inStreamingMode() //inBatchMode() --批处理or流处理
                                       .build();
        TableEnvironment tableEnv = StreamTableEnvironment.create(env, settings);
        //在Catlog中创建表
        //把kafka 中的topic映射成一个输入临时表
        tableEnv.executeSql(
                "create table kafka_test (user_id String,user_name String," +
                        " work_month String,work_amount String) with (" +
                        "'connector' = 'kafka'," +
                        "'topic' = 'binlog_data_pipe'," +
                        "'properties.bootstrap.servers' = 'localhost:9093'," +
                        "'properties.group.id' = 'test_java_group'," +
                        "'scan.startup.mode' = 'earliest-offset'," +
                        "'format' = 'json')");

        String mysql_sql = "CREATE TABLE mysql_sink (\n" +
                            "user_id     VARCHAR,\n" +
                            "user_name   VARCHAR,\n" +
                            "work_month  VARCHAR,\n" +
                            "work_amount VARCHAR \n" +
                            ") WITH (\n" +
                            "'connector' = 'jdbc',\n" +
                            "'url' = 'jdbc:mysql://localhost:3306/data_pipe" +
                            "'table-name' = 'java_kafka_test',\n" +
                            "'username' = 'root',\n" +
                            "'password' = '123'\n" +
                            " )";

        tableEnv.executeSql(mysql_sql);
//        tableEnv.executeSql("insert into mysql_sink select * from kafka_test");
    }
}