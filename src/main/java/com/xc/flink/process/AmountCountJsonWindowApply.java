package com.xc.flink.process;

import com.xc.flink.model.AnchorOrderJson;
import com.xc.flink.model.ItemCount;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

public class AmountCountJsonWindowApply implements WindowFunction<AnchorOrderJson.DataDTO,ItemCount, String, TimeWindow>{

    //Iterable 和 Collector的区别 多可以存放不同的数据类型
    //Iterable 迭代器
    //Collector 数据收集器
    @Override
    public void apply(String key, TimeWindow window, Iterable<AnchorOrderJson.DataDTO> in, Collector<ItemCount> out) throws Exception {
        long start = window.getStart();
        long end = window.getEnd();
        String output = "[" + (start) + "~" +(end) + "] -> " + key ;

        // 对窗口中的数据进行聚合
        int count = 0 ;
        for (AnchorOrderJson.DataDTO item: in){
            count += Integer.parseInt( item.getItemNum());
        }

        out.collect(new ItemCount(output,count));
    }
}