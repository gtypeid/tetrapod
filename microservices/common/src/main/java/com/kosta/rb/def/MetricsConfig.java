package com.kosta.rb.def;

import com.kosta.rb.def.abs.Config;
import com.kosta.rb.def.abs.WindowContext;
import com.kosta.rb.rule.abs.Rule;
import com.kosta.rb.rule.dice.RConstructor;
import com.kosta.rb.rule.metrics.RInsertComputer;
import com.kosta.rb.rule.metrics.RInsertTraffic;
import com.kosta.rb.rule.metrics.RMetricsConstructor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MetricsConfig extends Config {

    public MetricsConfig(){
        windowContext = new WindowContext.Builder()
                .setWindowTitle("Metrics")
                .setWindowStartPointX(5)
                .setWindowStartPointY(5)
                .setWindowSizeWidth(1500)
                .setWindowSizeHeight(800)
                .build();
    }

    @Override
    public Class<RMetricsConstructor> entryState() {
        return RMetricsConstructor.class;
    }

    @Override
    public List<Class<? extends Rule>> getRules() {
        List<Class<? extends Rule>> list = new ArrayList<>();
        list.add(RMetricsConstructor.class);
        list.add(RInsertComputer.class);
        list.add(RInsertTraffic.class);

        return list;
    }

};
