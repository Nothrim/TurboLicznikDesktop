package gui;

public class Time {
    private float hours;
    private float minutes;
    public Time(long time){
        hours=time/60;
        minutes= time%60;
    }

    @Override
    public String toString() {
        return "hours:"+hours+" minutes:"+minutes;
    }
    public String toPdfRow(){
        return hours+"h "+minutes+"m";
    }
}
