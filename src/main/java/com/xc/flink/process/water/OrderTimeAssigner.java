package com.xc.flink.process.water;

import com.xc.flink.model.AnchorOrderTimeJson;
import javafx.animation.KeyFrame;
import org.apache.flink.api.common.eventtime.Watermark;
import org.apache.flink.api.common.eventtime.WatermarkGenerator;
import org.apache.flink.api.common.eventtime.WatermarkOutput;
import org.apache.flink.table.shaded.org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderTimeAssigner implements WatermarkGenerator<AnchorOrderTimeJson.DataDTO> {
    private final long maxTimeLag = 5000; // 1 秒
    @Override
    public void onEvent(AnchorOrderTimeJson.DataDTO event, long eventTimestamp, WatermarkOutput output) {
        String ordertime =  event.getOrderTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date orderdatetime = format.parse(ordertime);
            long startDay = (long) (orderdatetime.getTime() / 1000);
            long newTimestamp = Math.max(startDay, eventTimestamp);
            output.emitWatermark(new Watermark(newTimestamp - maxTimeLag));

        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onPeriodicEmit(WatermarkOutput output) {
        // onEvent 中已经实现
    }

}



