package com.xc.flink.process.state;

import com.xc.flink.model.AnchorOrderTimeJson;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;
import scala.math.BigInt;

//in&out
public class CountFlatMapListState extends RichFlatMapFunction<AnchorOrderTimeJson.DataDTO, Tuple2<String, Long>> {

    private transient ListState<BigInt> sum;
    @Override
    public void flatMap(AnchorOrderTimeJson.DataDTO input, Collector<Tuple2<String, Long>> output) throws Exception {
        //获取状态数据
        BigInt currentSum = sum.ge;
        //状态数据处理
        currentSum.f0 += 1;
        currentSum.f1 += Long.valueOf(input.getItemNum());
        // 更新状态数据
        sum.update(currentSum);
        //如果超过2次,输出平均数并清除状态
        if (currentSum.f0 >= 2) {
            output.collect(new Tuple2<>(input.getAnchorId(), currentSum.f1 / currentSum.f0));
            sum.clear();
        }

    }
    @Override
    public void open(Configuration config) {
        ListStateDescriptor<BigInt> descriptor = new ListStateDescriptor<BigInt>(
                "sum_list", );

        sum = getRuntimeContext().getListState(descriptor);
    }
}
