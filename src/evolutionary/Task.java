package evolutionary;



import java.util.ArrayList;

public class Task {
    public String name = "t";
    public int[]skills = null;
    public double effort;

    public Task(String name, int[] skills, double effort) {
        this.name = name;
        this.skills = skills;
        this.effort = effort;
    }  
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("TASK---------------------------------------\n");
        sb.append("nama      : "+name+"\n"); 
        if(skills!=null){
            sb.append("skill     : "); 
            for(int i=0;i<skills.length;i++){
                int s = skills[i];
                if(i>0){
                    sb.append(", ");
                }
                sb.append(s);
            }
            sb.append("\n"); 
        }
        sb.append("effort    : "+effort+"\n");
        sb.append("-------------------------------------------\n");        
        return sb.toString();
    }
    
    
    
}
