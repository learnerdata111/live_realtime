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
    private final long maxTimeLag =  2000; // 2 秒
    @Override
    public void onEvent(AnchorOrderTimeJson.DataDTO event, long eventTimestamp, WatermarkOutput output) {

            long ordertime = event.getOrderTime()*1000L;
            long newTimestamp = Math.max(ordertime, eventTimestamp);
            System.out.println(newTimestamp - maxTimeLag);
            output.emitWatermark(new Watermark(newTimestamp - maxTimeLag-1));
    }

    @Override
    public void onPeriodicEmit(WatermarkOutput output) {
        // onEvent 中已经实现
    }

}



