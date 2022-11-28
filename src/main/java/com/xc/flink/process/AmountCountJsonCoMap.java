package com.xc.flink.process;

import com.xc.flink.model.ItemCount;
import com.xc.flink.model.ItemCountJoin;
import org.apache.flink.streaming.api.functions.co.CoMapFunction;

public class AmountCountJsonCoMap implements CoMapFunction<ItemCount, ItemCountJoin, ItemCount> {


    @Override
    public ItemCount map1(ItemCount item) throws Exception {
        return item;
    }

    @Override
    public ItemCount map2(ItemCountJoin itemCountJoin) throws Exception {
        return new ItemCount(itemCountJoin.getKeyType(),itemCountJoin.getItemNum());
    }
}