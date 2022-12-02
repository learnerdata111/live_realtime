package com.xc.flink.process.water;

import com.ibm.icu.math.BigDecimal;
import com.xc.flink.model.AnchorOrderTimeJson;
import org.apache.flink.api.common.functions.AggregateFunction;

//IN&累加器&out
//执行顺序 create->add->merge->result
public class OrderTimeAggreate implements AggregateFunction<AnchorOrderTimeJson.DataDTO, Integer, BigDecimal> {

        @Override
        public Integer createAccumulator() {
            return new Integer(0);
        }

        @Override
        public Integer add(AnchorOrderTimeJson.DataDTO dataDTO, Integer bigInt) {
            return bigInt+Integer.valueOf(dataDTO.getItemNum());
        }

        @Override
        public BigDecimal getResult(Integer bigInt) {
            return BigDecimal.valueOf( bigInt+0.2);
        }

        @Override
        public Integer merge(Integer integer1, Integer integer2) {
            return integer1+integer2;
        }
}



