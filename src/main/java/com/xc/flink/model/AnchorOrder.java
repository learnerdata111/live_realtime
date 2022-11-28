package com.xc.flink.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnchorOrder {
  private String anchorId;
  private String anchorName;
  private String orderId;
  private String saleType;
  private String itemType;
  private long orderAmount;
  private long itemNum;

}
