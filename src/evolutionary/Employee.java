package evolutionary;



import java.util.ArrayList;

public class Employee {
    public String name = "e";
    public int[]skills = null;
    public double dedication;
    public double salary;  

    public Employee(String name, int[]skills, double dedication, double salary) {
        this.name = name;
        this.skills = skills.clone();
        this.dedication = dedication;
        this.salary = salary;
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("EMPLOYEE-----------------------------------\n");
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
        sb.append("dedication: "+dedication+"\n");
        sb.append("salary    : "+salary+"\n");
        sb.append("-------------------------------------------\n");        
        return sb.toString();
    }
    
}
