package com.xc.flink.process.amount;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xc.flink.model.AnchorOrderJson;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;

public class AmountCountJsonFlatMap implements FlatMapFunction<String, AnchorOrderJson.DataDTO> {
    @Override
    public void flatMap(String s, Collector<AnchorOrderJson.DataDTO> out) throws Exception {
        Gson gson = new Gson();
        AnchorOrderJson product = gson.fromJson(s, new TypeToken<AnchorOrderJson>() {
        }.getType());
        if (product.getData() == null){
            out.collect(new AnchorOrderJson.DataDTO());
        }
        else{
            for(AnchorOrderJson.DataDTO record:product.getData()){
                out.collect(record);
            }
        }

    }
}