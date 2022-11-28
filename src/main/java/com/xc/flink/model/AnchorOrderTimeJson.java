package com.xc.flink.model;


import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnchorOrderTimeJson {

  @SerializedName("data")
  private List<DataDTO> data;
  private String database;
  private long es;
  private int id;
  private boolean isDdl;
  private MysqlTypeDTO mysqlType;
  private Object old;
  private Object pkNames;
  private String sql;
  private SqlTypeDTO sqlType;
  private String table;
  private long ts;
  private String type;

  @NoArgsConstructor
  @Data
  public static class MysqlTypeDTO {
    @SerializedName("order_time")
    private String orderTime;
    @SerializedName("create_time")
    private String createTime;
    @SerializedName("anchor_id")
    private String anchorId;
    @SerializedName("anchor_name")
    private String anchorName;
    @SerializedName("order_id")
    private String orderId;
    @SerializedName("sale_type")
    private String saleType;
    @SerializedName("item_type")
    private String itemType;
    @SerializedName("order_amount")
    private String orderAmount;
    @SerializedName("item_num")
    private String itemNum;
  }

  @NoArgsConstructor
  @Data
  public static class SqlTypeDTO {
    private int orderTime;
    private int createTime;
    private int anchorId;
    private int anchorName;
    private int orderId;
    private int saleType;
    private int itemType;
    private int orderAmount;
    private int itemNum;
  }

  @NoArgsConstructor
  @Data
  public static class DataDTO {
    @SerializedName("order_time")
    private String orderTime;
    @SerializedName("create_time")
    private String createTime;
    @SerializedName("anchor_id")
    private String anchorId;
    @SerializedName("anchor_name")
    private String anchorName;
    @SerializedName("order_id")
    private String orderId;
    @SerializedName("sale_type")
    private String saleType;
    @SerializedName("item_type")
    private String itemType;
    @SerializedName("order_amount")
    private String orderAmount;
    @SerializedName("item_num")
    private String itemNum;


  }
}
