package com.kosta.rb.def;

import com.kosta.rb.def.abs.Config;
import com.kosta.rb.rule.abs.Rule;
import com.kosta.rb.rule.dice.*;
import com.kosta.rb.rule.metrics.RInsertComputer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BoardConfig extends Config {

    protected String windowTitle = "Board";

    public static final int MAX_PLAYER_SIZE = 4;

    public final int goal = 1;

    public final boolean isDrawPath = true;
    public final Color drawPathColor = new Color(50, 255, 0, 110);
    public final int drawPathLineStroke = 8;

    public final int floorSize = 13;
    public final byte[] floorData = new byte[]{
            2, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 2,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            2, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 2
    };

    @Override
    public Class<RConstructor> entryState() {
        return RConstructor.class;
    }

    @Override
    public List<Class<? extends Rule>> getRules() {
        List<Class<? extends Rule>> list = new ArrayList<>();
        list.add(RConstructor.class);
        list.add(RSpawnActors.class);
        list.add(RUserActive.class);
        list.add(RThrowDice.class);
        list.add(RMoveFigure.class);
        list.add(REnd.class);
        return list;
    }
};
