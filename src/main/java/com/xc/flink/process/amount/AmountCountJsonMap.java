package com.xc.flink.process.amount;

import com.xc.flink.model.AnchorOrderJson;
import org.apache.flink.api.common.functions.MapFunction;

public class AmountCountJsonMap implements MapFunction<AnchorOrderJson.DataDTO, AnchorOrderJson.DataDTO> {

    @Override
    public AnchorOrderJson.DataDTO map(AnchorOrderJson.DataDTO data) throws Exception {
        AnchorOrderJson.DataDTO outdata =  data;
        outdata.setAnchorId(data.getAnchorId());
        outdata.setAnchorName(data.getAnchorName());
        outdata.setItemNum(data.getItemNum());
        return outdata;
    }
}