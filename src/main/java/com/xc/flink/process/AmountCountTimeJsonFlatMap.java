package com.xc.flink.process;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xc.flink.model.AnchorOrderJson;
import com.xc.flink.model.AnchorOrderTimeJson;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;

public class AmountCountTimeJsonFlatMap implements FlatMapFunction<String, AnchorOrderTimeJson.DataDTO> {
    @Override
    public void flatMap(String s, Collector<AnchorOrderTimeJson.DataDTO> out) throws Exception {
        Gson gson = new Gson();
        AnchorOrderTimeJson product = gson.fromJson(s, new TypeToken<AnchorOrderTimeJson>() {
        }.getType());
        if (product.getData() == null){
            out.collect(new AnchorOrderTimeJson.DataDTO());
        }
        else{
            for(AnchorOrderTimeJson.DataDTO record:product.getData()){
                out.collect(record);
            }
        }

    }
}