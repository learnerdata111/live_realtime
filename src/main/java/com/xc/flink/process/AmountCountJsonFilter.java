package com.xc.flink.process;

import com.xc.flink.model.AnchorOrderJson;
import org.apache.flink.api.common.functions.FilterFunction;


public class AmountCountJsonFilter implements FilterFunction<AnchorOrderJson.DataDTO> {

    @Override
    public boolean filter(AnchorOrderJson.DataDTO data) throws Exception {
        //判断字符是否相等
        return data.getAnchorId().equals("a_2");
    }
}