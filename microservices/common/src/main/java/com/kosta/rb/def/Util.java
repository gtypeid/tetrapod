package com.kosta.rb.def;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Util {

    public static int floorIndex(int x, int y, int size){
        return x + (size * y);
    }

    public static int floorIndex(int[] xy, int size){
        return floorIndex(xy[0], xy[1], size);
    }

    public static int[] floorXY(int index, int size){
        int y = index / size;
        int x = index - (size * y);
        return new int[]{ x, y };
    }

    public static String getResourcePath(String src){
        Path currentPath = Paths.get("");
        String path = currentPath.toAbsolutePath().toString();

        return path + BoardConfig.resourcePath + src;
    }

    public static int getRandomInt(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }

    public static float getRandomFloat(float min, float max) {
        return min + (max - min) * (float) Math.random();
    }
}
