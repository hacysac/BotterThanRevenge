package org.team1515.BotterThanRevenge.Utils;


import java.util.function.DoubleFunction;

public class Equation {
    public DoubleFunction<Double> x;
    public DoubleFunction<Double> y;
    public Equation(DoubleFunction<Double> x, DoubleFunction<Double> y){
        this.x = x;
        this.y = y;
    }
    public double applyX(double num){
        return x.apply(num);
    }
    public double applyY(double num){
        return y.apply(num);
    }
}
