package com.home.jsquad.knowhunt.math;

public class MathHelper {

    public static double round(double number, int fractionalDigits) {
        double pow = Math.pow(10, fractionalDigits);
        return Math.round(number * pow) / pow;
    }


}
