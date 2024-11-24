package com.kosta.rb.def.abs;

public class WindowContext {

    private String windowTitle;
    private int windowStartPointX;
    private int windowStartPointY;
    private int windowSizeWidth;
    private int windowSizeHeight;

    private WindowContext(Builder builder) {
        this.windowTitle = builder.windowTitle;
        this.windowStartPointX = builder.windowStartPointX;
        this.windowStartPointY = builder.windowStartPointY;
        this.windowSizeWidth = builder.windowSizeWidth;
        this.windowSizeHeight = builder.windowSizeHeight;
    }

    public String getWindowTitle() {
        return windowTitle;
    }

    public void setWindowTitle(String windowTitle) {
        this.windowTitle = windowTitle;
    }

    public int getWindowStartPointX() {
        return windowStartPointX;
    }

    public void setWindowStartPointX(int windowStartPointX) {
        this.windowStartPointX = windowStartPointX;
    }

    public int getWindowStartPointY() {
        return windowStartPointY;
    }

    public void setWindowStartPointY(int windowStartPointY) {
        this.windowStartPointY = windowStartPointY;
    }

    public int getWindowSizeWidth() {
        return windowSizeWidth;
    }

    public void setWindowSizeWidth(int windowSizeWidth) {
        this.windowSizeWidth = windowSizeWidth;
    }

    public int getWindowSizeHeight() {
        return windowSizeHeight;
    }

    public void setWindowSizeHeight(int windowSizeHeight) {
        this.windowSizeHeight = windowSizeHeight;
    }

    // Static inner Builder class
    public static class Builder {
        private String windowTitle;
        private int windowStartPointX;
        private int windowStartPointY;
        private int windowSizeWidth;
        private int windowSizeHeight;

        // Builder methods for setting properties
        public Builder setWindowTitle(String windowTitle) {
            this.windowTitle = windowTitle;
            return this;
        }

        public Builder setWindowStartPointX(int windowStartPointX) {
            this.windowStartPointX = windowStartPointX;
            return this;
        }

        public Builder setWindowStartPointY(int windowStartPointY) {
            this.windowStartPointY = windowStartPointY;
            return this;
        }

        public Builder setWindowSizeWidth(int windowSizeWidth) {
            this.windowSizeWidth = windowSizeWidth;
            return this;
        }

        public Builder setWindowSizeHeight(int windowSizeHeight) {
            this.windowSizeHeight = windowSizeHeight;
            return this;
        }

        // Build method to create a WindowContext instance
        public WindowContext build() {
            return new WindowContext(this);
        }
    }
}
