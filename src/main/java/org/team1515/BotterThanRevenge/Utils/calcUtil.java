package org.team1515.BotterThanRevenge.Utils;

public class calcUtil {
    public static double[] rampArr(int n, double low, double high){
        double[] arr = new double[n];
        int half = n/2;
        boolean extra = (half*2)!=(n);
        double mult = low;
        double scale = (high-low)/(double)half;
        for(int i = 0; i<n;i++){
            arr[i]=mult;
            if(mult==high){
                if(extra){
                    i++;
                    arr[i]=high;
                    extra = false;
                }
                scale*=-1.0;
            }
            mult += scale;
        }
        return arr;
    }
}