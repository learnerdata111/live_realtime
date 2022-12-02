package com.xc.flink.process.amount;

import com.xc.flink.model.AnchorOrderJson;
import com.xc.flink.model.ItemCountJoin;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.functions.co.ProcessJoinFunction;
import org.apache.flink.table.runtime.operators.join.interval.IntervalJoinFunction;
import org.apache.flink.util.Collector;

public class AmountCountJsonProcess extends ProcessJoinFunction<AnchorOrderJson.DataDTO, AnchorOrderJson.DataDTO, ItemCountJoin>{

    @Override
    public void processElement(AnchorOrderJson.DataDTO left, AnchorOrderJson.DataDTO right, Context context, Collector<ItemCountJoin> out) throws Exception {

        String stream1 =  "map"+left.getAnchorName();
        String stream2 =  "filter"+right.getAnchorName();
        int item_num = Integer.parseInt(left.getItemNum())+ Integer.parseInt(right.getItemNum());
        String keytype = right.getItemType();


        out.collect(new ItemCountJoin(stream1,stream2,keytype,item_num));

    }
}