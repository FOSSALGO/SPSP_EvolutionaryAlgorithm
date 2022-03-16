package evolutionary;

import java.util.Random;

public class EvolutionaryAlgorithm {

    //VARIABEL INPUT
    public Employee[] employees = null;
    public Task[] task = null;
    public int[][] tpg = null;

    //VARIABEL PROSES
    int numEmployees;
    int numTask;
    int[][] qualify = null;
    int[] numEmployeeQualifyInTask = null;
    boolean status = false;
    Random rand = new Random();

    //VARIABEL EA (Parameter EA)
    int numIndividu = 1000;//populasi
    int numGenerasi = 100;//MAX GENERASI
    int numIndividuTerseleksi = 50;// numIndividuTerseleksi harus lebih kecil dari numIndividu
    int numCrossoverPoints = 10;//0 <= numCrossoverPoint <= numEmployees x numTask
    double mutationRate = 0.5;//between 0 - 1
    int numMutationPoints = 10;

    //VARIABEL OUTPUT EA
    double bestFitness = 0;
    Individu individuElitisme = null;

    public EvolutionaryAlgorithm(Employee[] employees, Task[] task, int[][] tpg) {
        this.employees = employees;
        this.task = task;
        this.tpg = tpg;
        initialize();
        prosesEA();
    }

    private boolean initialize() {
        status = false;
        //validasi
        if (employees != null
                && task != null
                && tpg != null
                && tpg.length == task.length) {
            numEmployees = employees.length;
            numTask = task.length;

            //bikin array untuk employee yang qualify di task dengan value 0 atau 1. 1 untuk yang qualifiy
            //numEmployees = |E|
            //numTask      = |T|
            qualify = new int[numEmployees][numTask];
            for (int i = 0; i < qualify.length; i++) {
                int[] skill_employee = employees[i].skills;
                for (int j = 0; j < qualify[i].length; j++) {
                    int[] skill_task = task[j].skills;
                    //lakukan Matching kebutuhan skill minimum untuk task ke j
                    int value = 1;
                    for (int k = 0; k < skill_task.length; k++) {
                        int skill_k = skill_task[k];
                        //cari skill_k di skill_employee
                        boolean ada = false;
                        for (int l = 0; l < skill_employee.length; l++) {
                            int skill_l = skill_employee[l];
                            if (skill_k == skill_l) {
                                ada = true;
                                break;
                            }
                        }
                        if (ada == false) {
                            value = 0;
                            break;
                        }
                    }
                    qualify[i][j] = value;
                }
            }

            //VALIDASI QUALIFY untuk memasikan bahwa semua task dapat terselesaikan
            //harus ada minimal satu employee yang mengerjakan task tersebut  
            status = true;
            numEmployeeQualifyInTask = new int[numTask];
            for (int j = 0; j < numTask; j++) {
                int nEmployeeQualify = 0;
                for (int i = 0; i < numEmployees; i++) {
                    if (qualify[i][j] == 1) {
                        nEmployeeQualify++;
                    }
                }
                numEmployeeQualifyInTask[j] = nEmployeeQualify;
                if (nEmployeeQualify == 0) {
                    status = false;
                }
            }
        }//end of validasi
        //System.out.println("STATUS: " + status);
        return status;
    }

    private void prosesEA() {
        if (status) {
            Individu[] populasi = new Individu[numIndividu];

            //GENERATE GENERASI AWAL SECARA RANDOM------------------------------
            for (int h = 0; h < populasi.length; h++) {
                populasi[h] = new Individu(employees, task, tpg, qualify, status);
                populasi[h].hitungNilaiFitness();
                //System.out.println("Fitness-" + h + ": " + populasi[h].fitness);
            }
            //END OF GENERATE GENERASI AWAL SECARA RANDOM-----------------------  

            //SORT DESC POPULASI BERDASARKAN NILAI FITNESS----------------------
            Individu[] populasiEA = sortPopulasiBerdasarkanNilaiFitness(populasi);
            //------------------------------------------------------------------

            //OPERASI ELITISME--------------------------------------------------
            if (bestFitness < populasiEA[0].fitness) {
                bestFitness = populasiEA[0].fitness;
                individuElitisme = populasiEA[0];
            }
            System.out.println("Best: " + bestFitness);
            //------------------------------------------------------------------

            //PROSES EVOLUSI----------------------------------------------------
            for (int g = 1; g <= numGenerasi; g++) {
                //PROSES SELEKSI TURNAMEN---------------------------------------
                if (numIndividuTerseleksi > numIndividu) {
                    numIndividuTerseleksi = numIndividu;
                }
                Individu[] populasiProsesEA = new Individu[numIndividu];
                for (int p = 0; p < numIndividuTerseleksi; p++) {
                    populasiProsesEA[p] = populasiEA[p].clone();
                }
                //END OF PROSES SELEKSI TURNAMEN--------------------------------

                //PROSES CROSSOVER----------------------------------------------
                int k = numIndividuTerseleksi;
                int numGens = numEmployees * numTask;
                while (k < numIndividu) {
                    //random individu yang akan dijadikan parents
                    int indexParent1 = getRandomInteger(0, numIndividuTerseleksi - 1);
                    int indexParent2 = getRandomInteger(0, numIndividuTerseleksi - 1);
                    while (indexParent1 == indexParent2) {
                        indexParent2 = getRandomInteger(0, numIndividuTerseleksi - 1);
                    }
                    double[][] kromosomParent1 = populasiProsesEA[indexParent1].kromosom;
                    double[][] kromosomParent2 = populasiProsesEA[indexParent2].kromosom;
                    //random titik crossover
                    int[] crossoverPoint = randomPoints(numCrossoverPoints, numGens);
                    //inisialisasi kromosom offspring
                    double[][] kromosomOffspring1 = new double[numEmployees][numTask];
                    double[][] kromosomOffspring2 = new double[numEmployees][numTask];
                    int m = 0;//index baris reshape kromosom
                    int n = 0;
                    boolean cross = false;
                    for (int i = 0; i < numEmployees; i++) {
                        for (int j = 0; j < numTask; j++) {
                            if (n < numCrossoverPoints && m == crossoverPoint[n]) {
                                n++;
                                if (!cross) {
                                    cross = true;
                                } else {
                                    cross = false;
                                }
                            }
                            if (cross) {
                                kromosomOffspring1[i][j] = kromosomParent2[i][j];
                                kromosomOffspring2[i][j] = kromosomParent1[i][j];
                            } else {
                                kromosomOffspring1[i][j] = kromosomParent1[i][j];
                                kromosomOffspring2[i][j] = kromosomParent2[i][j];
                            }
                            m++;
                        }
                    }

                    if (k < numIndividu) {
                        //set offspring1 ke populasi
                        Individu individu = new Individu(kromosomOffspring1, employees, task, tpg, qualify);
                        populasiProsesEA[k] = individu;
                        k++;
                    }
                    if (k < numIndividu) {
                        //set offspring2 ke populasi
                        Individu individu = new Individu(kromosomOffspring2, employees, task, tpg, qualify);
                        populasiProsesEA[k] = individu;
                        k++;
                    }
                }
                //END OF PROSES CROSSOVER---------------------------------------

                //MUTATION------------------------------------------------------
                for (int p = 0; p < numIndividu; p++) {
                    double randMutation = rand.nextDouble();
                    if (randMutation <= mutationRate) {//lakukan mutasi jika randMutation lebih kecil dari samadengan mutationRate
                        int[] mutationPoint = randomPoints(numMutationPoints, numGens);
                        int m = 0;//index baris reshape kromosom
                        int n = 0;
                        for (int i = 0; i < numEmployees; i++) {
                            for (int j = 0; j < numTask; j++) {
                                if (n < numMutationPoints && m == mutationPoint[n]) {
                                    n++;
                                    if (populasiProsesEA[p].qualify[i][j] == 1) {
                                        double max_dedication = employees[i].dedication;
                                        double leftLimit = 0;
                                        double rightLimit = max_dedication;
                                        double value = acak(leftLimit, rightLimit);
                                        populasiProsesEA[p].kromosom[i][j] = value;
                                    }
                                }
                                m++;
                            }
                        }
                    }
                }
                //END OF MUTATION-----------------------------------------------

                //VALIDASI INDIVIDU DAN HITUNG NILAI FITNESS--------------------
                for (int p = 0; p < numIndividu; p++) {
                    //setiap task harus dikerjakan oleh minimal satu employee
                    for (int j = 0; j < numTask; j++) {
                        double dedicationInTask = 0;
                        for (int i = 0; i < numEmployees; i++) {
                            dedicationInTask += populasiProsesEA[p].kromosom[i][j];
                        }
                        if (dedicationInTask <= 0) {
                            boolean ada = false;
                            while (!ada) {//selama belum ada employee yang ditugaskan di task tersebut
                                dedicationInTask = 0;
                                for (int i = 0; i < numEmployees; i++) {
                                    if (populasiProsesEA[p].qualify[i][j] == 1) {
                                        double max_dedication = employees[i].dedication;
                                        double leftLimit = 0;
                                        double rightLimit = max_dedication;
                                        double value = acak(leftLimit, rightLimit);
                                        if (value > 0) {
                                            populasiProsesEA[p].kromosom[i][j] = value;
                                            dedicationInTask += value;
                                        }
                                    }
                                }
                                if (dedicationInTask > 0) {
                                    ada = true;
                                }
                            }
                        }
                    }
                    //HITUNG NILAI FITNESS SETELAH DIVALIDASI
                    populasiProsesEA[p].hitungNilaiFitness();
                }
                //END OF VALIDASI INDIVIDU--------------------------------------

                //SORT DESC POPULASI BERDASARKAN NILAI FITNESS----------------------
                populasiEA = sortPopulasiBerdasarkanNilaiFitness(populasiProsesEA);
                //------------------------------------------------------------------

                //OPERASI ELITISME----------------------------------------------
                if (bestFitness < populasiEA[0].fitness) {
                    bestFitness = populasiEA[0].fitness;
                    individuElitisme = populasiEA[0];
                }
                System.out.println("Best-" + g + ": " + bestFitness);
                //--------------------------------------------------------------

            }//END OF PROSES EVOLUSI--------------------------------------------
            
            //TAMPILKAN HASIL EA------------------------------------------------
            System.out.println("-----------------------------------------------");
            if(individuElitisme!=null){
                System.out.println("DURASI PROJECT: "+individuElitisme.duration+" hari");
                System.out.println("BIAYA PROJECT : "+individuElitisme.cost);
            }

        }
    }

    private int[] randomPoints(int numPoints, int numGens) {
        int[] points = null;
        if (numPoints <= numGens) {
            points = new int[numPoints];
            for (int i = 0; i < points.length; i++) {
                boolean unique = false;
                int value = -1;
                while (!unique) {
                    value = getRandomInteger(0, numGens - 1);
                    //cek value harus unik
                    unique = true;
                    for (int j = 0; j < i; j++) {
                        if (value == points[j]) {
                            unique = false;
                            break;
                        }
                    }
                }
                if (value >= 0 && value < numGens) {
                    points[i] = value;
                }
            }
            //sorting ascending crossover points
            for (int i = 0; i < points.length - 1; i++) {
                for (int j = 1 + i; j < points.length; j++) {
                    if (points[i] > points[j]) {
                        int temp = points[i];
                        points[i] = points[j];
                        points[j] = temp;
                    }
                }
            }
        }
        return points;
    }

    private int getRandomInteger(int min, int max) {
        if (min >= max) {
            int temp = min;
            min = max;
            max = temp;
        }
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
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

    private Individu[] sortPopulasiBerdasarkanNilaiFitness(Individu[] populasi) {
        Individu[] newPopulasi = null;
        if (populasi != null) {
            newPopulasi = new Individu[populasi.length];
            //urutkan fitness dan index array populasi
            double[][] data = new double[populasi.length][2];
            for (int i = 0; i < data.length; i++) {
                data[i][0] = populasi[i].fitness;
                data[i][1] = i;
            }
            //sorting
            for (int i = 0; i < data.length - 1; i++) {
                for (int j = i + 1; j < data.length; j++) {
                    if (data[i][0] < data[j][0]) {
                        //SWAP
                        double temp0 = data[i][0];
                        double temp1 = data[i][1];
                        data[i][0] = data[j][0];
                        data[i][1] = data[j][1];
                        data[j][0] = temp0;
                        data[j][1] = temp1;
                    }
                }
            }
            //susun kembali populasi berdasarkan nilai fitness descending
            for (int i = 0; i < populasi.length; i++) {
                int index = (int) data[i][1];
                newPopulasi[i] = populasi[index].clone();
            }
        }
        return newPopulasi;
    }

}//end of class
