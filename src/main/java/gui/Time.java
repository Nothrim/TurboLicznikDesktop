package gui;

import java.util.concurrent.TimeUnit;

public class Time {
    private float hours;
    private float minutes;
    public Time(long time){
        hours=TimeUnit.MILLISECONDS.toMinutes(time)/60;
        minutes= TimeUnit.MILLISECONDS.toMinutes(time)%60;
    }

    @Override
    public String toString() {
        return "hours:"+hours+" minutes:"+minutes;
    }
}
