package com.xc.flink;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

public class FlinkSql {
    public static void main(String[] args) {
        //TODO 1,获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);


        //TODO 2,使用DDL方式建表
        tableEnv.executeSql(
                "CREATE TABLE products (\n" +
                        " id INT,\n" +
                        "    name STRING,\n" +
                        "    description STRING,\n" +
                        "    PRIMARY KEY (id) NOT ENFORCED \n" +
                        ") WITH (\n" +
                        "   'connector' = 'jdbc',\n" +
                        "   'url' = 'jdbc:mysql://localhost:3306/mydb',\n" +
                        "   'table-name' = 'products' ,\n" +
                        "   'username' = 'root' ,\n" +
                        "   'password' = '123' \n"+
                        ")"
        );


        //TODO 3,查询并打印
        tableEnv.sqlQuery("select * from products")
                .execute()
                .print();
    }
}
