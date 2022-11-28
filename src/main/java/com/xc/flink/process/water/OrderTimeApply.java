package com.xc.flink.process.water;

import com.xc.flink.model.AnchorOrderTimeJson;
import com.xc.flink.model.ItemCount;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.sql.Timestamp;

//IN, OUT, KEY, Winodw
public class OrderTimeApply  implements WindowFunction<AnchorOrderTimeJson.DataDTO, ItemCount, String, TimeWindow> {

    @Override
    public void apply(String key, TimeWindow timeWindow, Iterable<AnchorOrderTimeJson.DataDTO> iterable, Collector<ItemCount> collector) throws Exception {

        Timestamp start = new Timestamp(timeWindow.getStart());
        System.out.println(1);
        Timestamp end = new Timestamp(timeWindow.getEnd());
        String output = "[" + (start) + "~" +(end) + "] -> " + key ;

        // 对窗口中的数据进行聚合
        int count = 0 ;
        for (AnchorOrderTimeJson.DataDTO item: iterable){
            count += Integer.parseInt( item.getItemNum());
        }

        collector.collect(new ItemCount(output,count));

    }
}



