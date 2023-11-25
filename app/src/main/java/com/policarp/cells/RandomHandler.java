package com.policarp.cells;

import java.util.Random;

public class RandomHandler {
    private static Random _random = new Random();
    public static boolean convertIntToBoolean(int n){
        return n == 0 ? false : true;
    }
    public static int getRandomIntInBounds(int min, int max){
        if(max - min <= 0)
            return 0;
        return _random.nextInt(max - min) + min;
    }
    public static boolean getRandomBoolean(int trueChancePercentage){
        if(trueChancePercentage > 50)
            return true;
        if(trueChancePercentage == 0)
            return false;
        if(trueChancePercentage < 0)
            throw new ArithmeticException("Chance can't be less than zero ! But was : " + trueChancePercentage);
        boolean prev = true;
        for(int i = 0; i <= trueChancePercentage * 2 / 100; ++i){
            prev = prev && convertIntToBoolean(_random.nextInt(1));
        }
        return prev;
    }
}
