package com.xc.flink.process.water;

import com.xc.flink.model.AnchorOrderTimeJson;
import org.apache.flink.api.common.eventtime.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

public class OrderTimeWaterStrategy implements WatermarkStrategy<AnchorOrderTimeJson.DataDTO> {

    //timestampAssigner 和 WatermarkGenerator
    //根据策略实例化一个 watermark 生成器
    @Override
    public WatermarkGenerator<AnchorOrderTimeJson.DataDTO> createWatermarkGenerator(WatermarkGeneratorSupplier.Context context) {
        return new OrderTimeAssigner();
    }

    //根据策略实例化一个可分配时间戳的 {@link TimestampAssigner}。
    @Override
    public TimestampAssigner<AnchorOrderTimeJson.DataDTO> createTimestampAssigner(TimestampAssignerSupplier.Context context) {
        return new TimestampAssigner<AnchorOrderTimeJson.DataDTO>() {
            @Override
            public long extractTimestamp(AnchorOrderTimeJson.DataDTO dataDTO, long recordTimestamp) {
                String ordertime =  dataDTO.getOrderTime();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date orderdatetime = null;
                try {
                    orderdatetime = format.parse(ordertime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long startDay = (long) (orderdatetime.getTime() / 1000);
                return startDay;
            }
        };
    }

}



