package com.xc.flink.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemCountJoin {
  private String stram1;
  private String stram2;
  private String keyType;
  private int itemNum;

}
