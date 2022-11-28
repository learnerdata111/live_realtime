package com.xc.flink.process;

import com.xc.flink.model.AnchorOrderJson;
import com.xc.flink.model.ItemCount;
import org.apache.flink.api.common.functions.ReduceFunction;

public class AmountCountJsonReduce implements ReduceFunction<AnchorOrderJson.DataDTO> {
    //第一个字段历史流汇总，第二个字段是当前流
    @Override
    public AnchorOrderJson.DataDTO reduce(AnchorOrderJson.DataDTO hist, AnchorOrderJson.DataDTO this1) throws Exception {
        AnchorOrderJson.DataDTO newdata = new AnchorOrderJson.DataDTO();
        newdata.setItemType(this1.getItemType());
        newdata.setItemNum(this1.getItemNum()+hist.getItemNum());
        newdata.setOrderAmount(this1.getOrderAmount()+hist.getOrderAmount());

        return newdata;
    }
}