package evolutionary;

import java.util.Arrays;
import java.util.Random;

public class Individu {

    int numEmployees;
    int numTask;
    Employee[] employees = null;
    Task[] task = null;
    int[][] tpg = null;
    int[][] qualify = null;
    double[][] kromosom = null;
    double duration;//project duration
    double cost;//project cost
    double fitness;//fitness individu
    double[] taskDuration = null;
    GanttVertex[] ganttDiagram = null;

    private Individu() {

    }

    public Individu(Employee[] employees, Task[] task, int[][] tpg, int[][] qualify, boolean status) {
        this.employees = employees;
        this.task = task;
        this.tpg = tpg;
        this.qualify = qualify;
        generateRandomDedicationDegreeMatrix(employees, task, qualify, status);
    }

    public Individu(double[][] kromosom, Employee[] employees, Task[] task, int[][] tpg, int[][] qualify) {
        this.employees = employees;
        this.task = task;
        this.tpg = tpg;
        this.qualify = qualify;
        this.kromosom = kromosom.clone();
        numEmployees = employees.length;
        numTask = task.length;
    }

    public Individu clone() {
        Individu newIndividu = new Individu();
        newIndividu.numEmployees = this.numEmployees;
        newIndividu.numTask = this.numTask;
        newIndividu.employees = this.employees.clone();
        newIndividu.task = this.task.clone();
        newIndividu.tpg = this.tpg.clone();
        newIndividu.qualify = this.qualify.clone();
        newIndividu.kromosom = this.kromosom.clone();
        newIndividu.duration = this.duration;//project duration
        newIndividu.cost = this.cost;//project cost
        newIndividu.fitness = this.fitness;//fitness individu
        newIndividu.taskDuration = this.taskDuration.clone();
        newIndividu.ganttDiagram = this.ganttDiagram.clone();
        return newIndividu;
    }

    private double[][] generateRandomDedicationDegreeMatrix(Employee[] employees, Task[] task, int[][] qualify, boolean status) {
        this.kromosom = null;
        if (status
                && employees != null
                && task != null
                && qualify != null
                && qualify.length == employees.length
                && qualify[0].length == task.length) {
            //generate random chromosome----------------------------------------            
            numEmployees = employees.length;
            numTask = task.length;
            kromosom = new double[numEmployees][numTask];
            int[] sudahTerisiOlehEmployee = new int[numTask];
            for (int j = 0; j < numTask; j++) {
                while (sudahTerisiOlehEmployee[j] <= 0) {
                    for (int i = 0; i < numEmployees; i++) {
                        if (qualify[i][j] == 1) {
                            double max_dedication = employees[i].dedication;
                            double leftLimit = 0;
                            double rightLimit = max_dedication;
                            double value = acak(leftLimit, rightLimit);
                            if (value > 0) {
                                sudahTerisiOlehEmployee[j]++;
                                kromosom[i][j] = value;
                            }
                        }
                    }
                }
            }
        }
        return this.kromosom;
    }

    public double hitungNilaiFitness() {
        this.fitness = 0;
        hitungTaskDuration();
        validasiGanttDiagram();
        hitungCostProject();
        this.fitness = 1.0 / (this.cost * this.duration);
        return this.fitness;
    }

    private double hitungCostProject() {
        if (this.kromosom != null) {
            double projectCost = 0;//project cost
            for (int i = 0; i < numEmployees; i++) {
                double salary = employees[i].salary;
                for (int j = 0; j < numTask; j++) {
                    double tj_dur = taskDuration[j];
                    double x_ij = kromosom[i][j];
                    projectCost += (salary * x_ij * tj_dur);
                }
            }
            this.cost = projectCost;
        }
        return this.cost;
    }

    private GanttVertex[] validasiGanttDiagram() {
        if (this.kromosom != null) {
            this.ganttDiagram = new GanttVertex[numTask];
            double projectDuration = 0;
            for (int j = 0; j < numTask; j++) {
                double dur = taskDuration[j];
                int[] precedence = tpg[j];
                double startTime = 0;
                //PERIKSA TPG-------------------------------------------------------
                for (int i = 0; i < precedence.length; i++) {
                    int p = precedence[i];
                    if (p >= 0 && p < j) {
                        if (ganttDiagram[i].end_time > startTime) {
                            startTime = ganttDiagram[i].end_time;
                        }
                    }
                }
                //PERIKSA EMPLOYEE--------------------------------------------------
                double[] dedicationOfEmployee = new double[numEmployees];
                for (int i = 0; i < numEmployees; i++) {
                    dedicationOfEmployee[i] = kromosom[i][j];
                }
                ganttDiagram[j] = new GanttVertex(startTime, dur, dedicationOfEmployee);

                //PERIKSA MAX DEDICATION--------------------------------------------
                //periksa apakah ada employee yang bekerja diatas batas dedikasi maksimumnya
                double[] dedicationLimit = dedicationOfEmployee.clone();
                double startA = startTime;
                double endA = startTime + dur;
                double newStart = startTime;
                for (int k = j - 1; k >= 0; k--) {
                    //periksa apakah waktu pengerjaan task di gantt diagram saling beririsan
                    double startB = ganttDiagram[k].start_time;
                    double endB = ganttDiagram[k].end_time;

                    double start = Math.max(startA, startB);
                    double end = Math.min(endA, endB);

                    if (end - start > 0) {
                        for (int l = 0; l < numEmployees; l++) {
                            dedicationLimit[l] += ganttDiagram[k].dedicationOfEmployee[l];
                            if (dedicationLimit[l] > employees[l].dedication && newStart < endB) {
                                newStart = endB;
                            }
                        }
                    }
                }
                ganttDiagram[j].setStartTime(newStart);//update start time
                if (ganttDiagram[j].end_time > projectDuration) {
                    projectDuration = ganttDiagram[j].end_time;//update tProject
                }
            }
            this.duration = projectDuration;//update durasi project
        }
        return this.ganttDiagram;
    }

    private double[] hitungTaskDuration() {
        if (this.kromosom != null) {
            //hitung durasi task
            taskDuration = new double[numTask];
            for (int j = 0; j < numTask; j++) {
                double t_effort = task[j].effort;
                double sigma = 0;
                for (int i = 0; i < numEmployees; i++) {
                    if (kromosom[i][j] > 0) {
                        sigma += kromosom[i][j];
                    }
                }
                double t_duration = t_effort / sigma;
                taskDuration[j] = t_duration;
            }
        }
        return taskDuration;
    }

    private double acak(double leftLimit, double rightLimit) {
        if (rightLimit < leftLimit) {
            double temp = rightLimit;
            rightLimit = leftLimit;
            leftLimit = temp;
        }
        double generatedDouble = leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
        return generatedDouble;
    }

    public void cetakKromosom() {
        if (this.kromosom != null) {
            System.out.println("INDIVIDU-----------------------------------");
            for (int i = 0; i < kromosom.length; i++) {
                System.out.print("kromosom_" + i);
                System.out.print(": [ ");
                for (int j = 0; j < kromosom[i].length; j++) {
                    if (j > 0) {
                        System.out.print(" | ");
                    }
                    System.out.print(kromosom[i][j]);
                }
                System.out.println(" ]");
            }
            System.out.println("-------------------------------------------");
        } else {
            System.out.println("CHROMOSOME = NULL");
        }
    }

}
