package test;

import evolutionary.Employee;
import evolutionary.EvolutionaryAlgorithm;
import evolutionary.Task;

public class Main {
    public static void main(String[] args) {
        /*
         Skill
         s0 = UML
         s1 = Database
         s2 = Programmer
         s3 = Web Design
         s4 = Test        
         */

        //EMPLOYEES
        int numEmployees = 4;
        Employee[] employees = new Employee[numEmployees];

        //initialize Employee_0
        String name_0 = "e_0";
        int[] skills_0 = {0, 3};
        double dedication_0 = 0.85;
        double salary_0 = 3000;
        Employee e_0 = new Employee(name_0, skills_0, dedication_0, salary_0);
        employees[0] = e_0;

        //initialize Employee_1
        String name_1 = "e_1";
        int[] skills_1 = {0, 1, 2, 4};
        double dedication_1 = 1;
        double salary_1 = 4000;
        Employee e_1 = new Employee(name_1, skills_1, dedication_1, salary_1);
        employees[1] = e_1;

        //initialize Employee_2
        String name_2 = "e_2";
        int[] skills_2 = {1, 2};
        double dedication_2 = 1;
        double salary_2 = 3500;
        Employee e_2 = new Employee(name_2, skills_2, dedication_2, salary_2);
        employees[2] = e_2;
        

        //initialize Employee_3
        String name_3 = "e_3";
        int[] skills_3 = {0, 2, 3, 4};
        double dedication_3 = 0.8;
        double salary_3 = 3500;
        Employee e_3 = new Employee(name_3, skills_3, dedication_3, salary_3);
        employees[3] = e_3;

        //TASK
        int numTask = 8;
        Task[] task = new Task[numTask];

        //initialize Task_0
        String tname_0 = "t_0";
        int[] tskills_0 = {1, 2, 4};
        double teffort_0 = 5;
        Task t_0 = new Task(tname_0, tskills_0, teffort_0);
        task[0] = t_0;

        //initialize Task_1
        String tname_1 = "t_1";
        int[] tskills_1 = {1, 2};
        double teffort_1 = 25.0;
        Task t_1 = new Task(tname_1, tskills_1, teffort_1);
        task[1] = t_1;

        //initialize Task_2
        String tname_2 = "t_2";
        int[] tskills_2 = {4};
        double teffort_2 = 5.0;
        Task t_2 = new Task(tname_2, tskills_2, teffort_2);
        task[2] = t_2;

        //initialize Task_3
        String tname_3 = "t_3";
        int[] tskills_3 = {0, 3};
        double teffort_3 = 20.0;
        Task t_3 = new Task(tname_3, tskills_3, teffort_3);
        task[3] = t_3;

        //initialize Task_4
        String tname_4 = "t_4";
        int[] tskills_4 = {0, 2, 3, 4};
        double teffort_4 = 60.0;
        Task t_4 = new Task(tname_4, tskills_4, teffort_4);
        task[4] = t_4;

        //initialize Task_5
        String tname_5 = "t_5";
        int[] tskills_5 = {0, 3};
        double teffort_5 = 30.0;
        Task t_5 = new Task(tname_5, tskills_5, teffort_5);
        task[5] = t_5;

        //initialize Task_6
        String tname_6 = "t_6";
        int[] tskills_6 = {1};
        double teffort_6 = 10.0;
        Task t_6 = new Task(tname_6, tskills_6, teffort_6);
        task[6] = t_6;

        //initialize Task_7
        String tname_7 = "t_7";
        int[] tskills_7 = {0, 2, 4};
        double teffort_7 = 35.0;
        Task t_7 = new Task(tname_7, tskills_7, teffort_7);
        task[7] = t_7;

        //Task Precedence Graph (TPG)
        //value tpg = -1 berarti tidak ada precendence
        int[][] tpg = {
            {-1},
            {0},
            {0},
            {1, 2},
            {1, 3},
            {4},
            {4},
            {5}
        };
        
        //PROSES EVOLUTIONARY---------------------------------------------------
        EvolutionaryAlgorithm ea = new EvolutionaryAlgorithm(employees, task, tpg);
        
        //----------------------------------------------------------------------
        
        
    }
}
