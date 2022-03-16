package evolutionary;

public class GanttVertex {

    public double start_time;//hari ke
    public double end_time;
    public double duration;
    public double[] dedicationOfEmployee;

    public GanttVertex(double start_time, double duration, double[] dedicationOfEmployee) {
        this.start_time = start_time;
        this.end_time = start_time + duration;
        this.duration = duration;
        this.dedicationOfEmployee = dedicationOfEmployee;
    }

    public void setStartTime(double start_time) {
        this.start_time = start_time;
        this.end_time = start_time + duration;
    }    
}