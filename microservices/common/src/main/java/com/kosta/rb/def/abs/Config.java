package com.kosta.rb.def.abs;

import com.kosta.rb.rule.abs.Rule;
import com.kosta.rb.rule.dice.RConstructor;

import java.util.List;

public abstract class Config {
    //public static final String resourcePath = "\\web_prj\\src\\main\\java\\com\\kosta\\rb\\resource\\";
    public static final String resourcePath = "\\microservices\\common\\src\\main\\java\\com\\kosta\\rb\\resource\\";

    protected WindowContext windowContext;

    public final int floorWorldGrid = 25;
    public enum ELayer{
        BACK(0),
        FLOOR(1),
        FLOOR_FRONT(2),
        FIGURE(3),
        FIGURE_FRONT(4),
        FRONT(5),
        NONE(6);

        private final int value;

        ELayer(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public Config(){
        windowContext = new WindowContext.Builder()
                .setWindowTitle("Title")
                .setWindowStartPointX(150)
                .setWindowStartPointY(100)
                .setWindowSizeWidth(1000)
                .setWindowSizeHeight(750)
                .build();
    }

    public abstract <T extends Rule> Class<T> entryState();
    public abstract List<Class<? extends Rule>> getRules();

    public WindowContext getWindowContext() {
        return windowContext;
    }
}
