package com.xc.flink.process.amount;

import com.xc.flink.model.AnchorOrderJson;
import com.xc.flink.model.ItemCount;
import com.xc.flink.model.ItemCountJoin;
import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.streaming.api.functions.windowing.AllWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

public class AmountCountJsonJoinApply implements JoinFunction<AnchorOrderJson.DataDTO,AnchorOrderJson.DataDTO, ItemCountJoin> {

    @Override
    public ItemCountJoin join(AnchorOrderJson.DataDTO dataDTO, AnchorOrderJson.DataDTO dataDTO2) throws Exception {
        String stream1 =  "map"+dataDTO.getAnchorName();
        String stream2 =  "filter"+dataDTO2.getAnchorName();
        int item_num = Integer.parseInt(dataDTO.getItemNum())+ Integer.parseInt(dataDTO.getItemNum());
        String keytype = dataDTO.getItemType();

        return new ItemCountJoin(stream1,stream2,keytype,item_num);
    }
}