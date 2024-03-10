package org.team1515.BotterThanRevenge.Utils;

public class Point {
    public double x;
    public double y;
    public Point(double x, double y){
        this.x = x;
        this.y = y;
    }
    public void plus(Point p){
        x+=p.x;
        y+=p.y;
    }
}
