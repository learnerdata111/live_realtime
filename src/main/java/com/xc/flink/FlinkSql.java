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
                "CREATE TABLE products ( " +
               "    id INT NOT NULL,    "      +
               "    name STRING NOT NULL,    "      +
               "    description STRING NOT NULL ,    "      +
               "    PRIMARY KEY (id) NOT ENFORCED    "   +
               "    ) WITH ( " +
               "            'connector' = 'mysql-cdc', " +
               "            'hostname' = 'localhost', " +
               "            'port' = '3306', " +
               "            'username' = 'root', " +
               "            'password' = '123', " +
               "            'database-name' = 'mydb', " +
               "           'table-name' = 'products' "   +
               "  )  "
        );


        //TODO 3,查询并打印
        tableEnv.sqlQuery("select * from products")
                .execute()
                .print();
    }
}
