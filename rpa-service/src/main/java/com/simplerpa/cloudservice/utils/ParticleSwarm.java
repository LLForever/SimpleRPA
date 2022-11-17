package com.simplerpa.cloudservice.utils;

import com.alibaba.fastjson.JSONObject;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ParticleSwarm {
    /**
     * 变量的个数
     * */
    public int nVar;

    public double sum;

    /**
     * 粒子的位置，包含解的信息
     * */
    public	double [] position;
    /**
     * 粒子的速度
     * */
    public	double [] velecity;
    /**
     * 粒子历史最有位置
     * */
    public	double [] P_position;
    /**
     * 粒子的历史最优适应值
     * */
    public	double P_fitness;
    /**
     * 粒子的适应值
     * */
    public	double fitness;
    /**
     * 粒子的运动边界 -- 下
     * */
    public 	double[] x_low;
    /**
     * 粒子的运动边界--上
     * */
    public 	double[] x_up;
    static Random rand1=new Random();   // 随机量生成
    // java 的构造函数（没有析构函数）
    //粒子的初始化
    /**
     输入变量：所求变量的个数，变量的下边界，上边界
     */
    ParticleSwarm(int nVar,double[] x_low,double[] x_up){
        position = new double[nVar];
        velecity = new double[nVar];
        P_position = new double[nVar];
        sum = 0;
        this.x_low=x_low;
        this.x_up=x_up;
        // 初始化在边界内：位置，速度
        for(int i=0;i<nVar;i++){
            int flag = rand1.nextBoolean()? 1 : -1;
            position[i] = getRandPosition(x_up[i], x_low[i]);
            velecity[i]=rand1.nextDouble() * flag;
        }
    }

    public static double getRandPosition(double up, double low){
        return rand1.nextDouble() * (up - low) + low;
    }

    public static double getR1(int i, int MaxIter){
//        return rand1.nextDouble();
        double no = (double)i / MaxIter;
        return (1 - no);
    }

    public static double getR2(int i, int MaxIter){
//        return rand1.nextDouble();
        double no = (double)i / MaxIter;
        return (0.01 + no);
    }
}

class WindowQueue{
    private double[] list;
    private int size, pos;
    private double sum;
    private static final double MIN_NUM = 0.001, MAX_STEP = 1.49;
    public WindowQueue(int size){
        this.size = size;
        this.pos = 0;
        this.sum = 0;
        this.list = new double[size];
        Arrays.fill(list, 0);
    }
    public double getParamsRes(){
        double res = 0.149 / (Math.sqrt(sum + MIN_NUM));
//        if (res > MAX_STEP){
//            res = MAX_STEP;
//        }
        return res;
    }
    public void insertElement(double item){
        sum -= list[pos]*list[pos];
        sum += item*item;
        list[pos++] = item;
        pos %= size;
    }
    public void insertOriginElement(double item){
        sum -= list[pos];
        sum += item;
        list[pos++] = item;
        pos %= size;
    }
    public double getElementAVG(){
        return sum / size;
    }
    public double getMinElement(){
        double minRes = list[0];
        for(int i=0; i<size; i++){
            minRes = Math.min(list[i], minRes);
            if(list[i] == 0){
                break;
            }
        }
        return minRes;
    }
}

class AdamDelta{
    /**
     * 每个粒子的维度列表
     * */
    WindowQueue[] list, globalList;
    WindowQueue fitnessWindow;
    public AdamDelta(int dimensionNum, int windowSize){
        list = new WindowQueue[dimensionNum];
        globalList = new WindowQueue[dimensionNum];
        for(int i=0; i<dimensionNum; i++){
            list[i] = new WindowQueue(windowSize);
            globalList[i] = new WindowQueue(windowSize);
        }
        fitnessWindow = new WindowQueue(windowSize);
    }
}

/*
粒子群算法类
*/
class PSO{
    /**
     * 算法的学习因子
     * */
    public double c1, c2, R1, R2;

    /**
     * 服务器状态
     * */
    private JSONObject machinesAllocateInfo, machinePerformanceJSON;
    private Long[] ids;

    /**
     * 惯性权重系数
     * */
    public double w;
    /**
     * 粒子群种群
     * */
    public ParticleSwarm[] pop;
    /**
     * 最大迭代次数
     * */
    public int MaxIter;
    /**
     * 种群数量
     * */
    public int nPop;
    public double[] x_low;	// 变量下边界
    public double[] x_up;		// 变量上边界
    /**
     * 全局最优适应值
     * */
    public double best_fitness, worstFitness;
    /**
     * 全局最优位置（最优解）
     * */
    public double[] best_solution, best_speed;
    /**
     * 变量个数(评价函数的维度)
     * */
    public int nVar;

    /**
     * AdamDelta优化器
     * */
    public AdamDelta[] adamDeltas;

    /**
     * 散点图数据集
     * */
    XYSeriesCollection dataSet;

    private static final boolean GRAPH_SHOW = false;

    public static final int WindowSize = 10, TEST_COUNT = 1;
    //    static Random rand1=new Random();

    PSO(double[] x_low, double[] x_up, JSONObject mai, JSONObject mpj, Long[] ids){
//        this(1.49445, 1.49445, 0.729, ids.length, 200, 50, x_low, x_up);
        machinesAllocateInfo = mai;
        machinePerformanceJSON = mpj;
        this.ids = ids.clone();
        initEveryParams(1.49445, 1.49445, 0.729, ids.length, 500, 50, x_low, x_up);
    }

	/*
	构造函数初始化
	*/
    PSO(double c1,double c2,double w,int nVar,int MaxIter,int nPop,double[] x_low,double[] x_up){
        initEveryParams(c1, c2, w, nVar, MaxIter, nPop, x_low, x_up);
    }

    private void initEveryParams(double c1,double c2,double w,int nVar,int MaxIter,int nPop,double[] x_low,double[] x_up){
        this.c1=c1;
        this.c2=c2;
        this.w=w;
        this.nVar=nVar;
        this.MaxIter=MaxIter;
        this.nPop=nPop;
        this.x_low=x_low;
        this.x_up=x_up;
        this.pop = new ParticleSwarm[nPop];
        this.adamDeltas = new AdamDelta[nPop];
        this.R1 = ParticleSwarm.getR1(0, MaxIter);
        this.R2 = ParticleSwarm.getR2(0, MaxIter);

        // 种群初始化
        for(int i=0;i<nPop;i++){
            pop[i]=new ParticleSwarm(nVar,x_low,x_up);  				// 初始化每个粒子
            pop[i].fitness=function_fitness(pop[i].position);	// 计算每个粒子的适应值
            pop[i].P_fitness=pop[i].fitness;					// 初始化粒子的最优适应值
            pop[i].P_position=(double[])pop[i].position.clone();// 初始化粒子的最优位值，数组的复制用clone，
            adamDeltas[i] = new AdamDelta(nVar, WindowSize);

            if(i == 0){
                best_fitness=pop[i].fitness;
                best_solution=(double[])pop[i].position.clone();
                best_speed = pop[i].velecity.clone();
            }else if(best_fitness > pop[i].fitness){
                best_fitness=pop[i].fitness;
                best_solution=(double[])pop[i].position.clone();
                best_speed = pop[i].velecity.clone();
            }
        }

        if(GRAPH_SHOW){
            initGraphUI();
        }
    }

    private void initGraphUI(){
        XYSeriesCollection dataSet = new XYSeriesCollection();
        JFreeChart freeChart = ChartFactory.createScatterPlot(
                "散点图",
                "X",
                "Y",
                dataSet,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        ChartPanel chartPanel = new ChartPanel(freeChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(560, 400));
        JFrame frame = new JFrame("饼图");
        frame.setLocation(500, 400);
        frame.setSize(600, 500);

        //将主窗口的内容面板设置为图表面板
        frame.setContentPane(chartPanel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        this.dataSet = dataSet;
    }

    // 适应值函数
    public double function_fitness(double[] var){
//        return fun8(var[0], var[1]);
//        return Sphere(var);
//        return Rosenbrock(var);
        return Schedule(var);
    }

    private double Sphere(double[] var){
        double sum=0;
        for(int i=0;i<nVar;i++)
        {
            sum+=var[i]*var[i];
        }
        return sum;
    }
    private double Rosenbrock(double[] var){
        double sum=0;
        for(int i=0;i<nVar-1;i++)
        {
            sum += 100*(var[i+1] - var[i]*var[i])*(var[i+1] - var[i]*var[i]) + (var[i]-1)*(var[i]-1);
        }
        return sum;
    }

    private double Schedule(double[] var){
        double[][] sumList = new double[4][3];
        double sum;
        for(int i=0; i<4; i++){
            for (int j=0; j<3; j++){
                sumList[i][j] = 0.0;
            }
        }
        for(int i=0; i<nVar; i++){
            int targetMachine = (int) var[i];
            if(targetMachine > 3){
                targetMachine = 3;
            }
            List<Double> costListByTaskId = TaskCostCountUtil.getCostListByTaskId(ids[i]);
            sumList[targetMachine][0] += costListByTaskId.get(0); // cpu
            sumList[targetMachine][1] += costListByTaskId.get(1); // mem
            sumList[targetMachine][2] += costListByTaskId.get(2); // net
        }
        for(int i=0; i<4; i++){
            String machineName = TaskScheduleAllocator.machineName.get(i);
            double[] machineCostList = machinesAllocateInfo.getJSONObject(machineName).getObject(TaskCostCountUtil.LIST, double[].class);
            sumList[i][0] += machineCostList[0];
            sumList[i][1] += machineCostList[1];
            sumList[i][2] += machineCostList[2];
        }
        double F = 0.0, G = 0.0;
        for(int i=0; i<4; i++){
            double Nc, Nm, Nn;
            double Rc, Rm, Rn;
            Nc = machinePerformanceJSON.getJSONObject(TaskScheduleAllocator.machineName.get(i)).getDouble("cpu");
            Nm = machinePerformanceJSON.getJSONObject(TaskScheduleAllocator.machineName.get(i)).getDouble("mem");
            Nn = machinePerformanceJSON.getJSONObject(TaskScheduleAllocator.machineName.get(i)).getDouble("net");
            Rc = 100-Nc;
            Rm = 100-Nm;
            Rn = 100-Nn;
            F += Math.sqrt((sumList[i][0]/Rc)*(sumList[i][0]/Rc) + (sumList[i][1]/Rm)*(sumList[i][1]/Rm) + (sumList[i][2]/Rn)*(sumList[i][2]/Rn));
            G += Math.sqrt((sumList[i][0] + Nc)*(sumList[i][0] + Nc) + (sumList[i][1] + Nm)*(sumList[i][1] + Nm) + (sumList[i][2] + Nn)*(sumList[i][2] + Nn));
        }
        sum = 0.5*F + 0.5*G;
        return sum;
    }

    /*
    种群搜索过程，粒子更新的方法
    1.先计算粒子的速度，按公式计算，采用基本粒子群算法的更新公式
    2.对出界的速度进行限制
    3.按公式更新粒子的位置
    4.对出界的位置进行限制
    */
    public void up_search(){
        for(int i=0;i<nPop;i++){
            for(int j=0;j<nVar;j++){
                pop[i].velecity[j]= w * pop[i].velecity[j]+R1*(pop[i].P_position[j]-pop[i].position[j])*getC1(i, j) + R2*(best_solution[j]-pop[i].position[j])*getC2(i, j);
//                adamDeltas[i].list[j].insertElement(pop[i].velecity[j] - old_vel);
//                adamDeltas[i].globalList[j].insertElement(pop[i].velecity[j] - old_vel);
                if(pop[i].velecity[j] > (x_up[j] - x_low[j])*0.1){
                    pop[i].velecity[j] = (x_up[j] - x_low[j])*0.1;
                }
                if(pop[i].velecity[j] < (x_low[j] - x_up[j])*0.1){
                    pop[i].velecity[j] = (x_low[j] - x_up[j])*0.1;
                }

//                adamDeltas[i].list[j].insertElement(pop[i].velecity[j]);
//                adamDeltas[i].globalList[j].insertElement(pop[i].velecity[j]);

                pop[i].position[j]=pop[i].position[j]+pop[i].velecity[j];
                if(pop[i].position[j]>x_up[j]){
                    pop[i].position[j]=x_up[j];
                }
                if(pop[i].position[j]<x_low[j]){
                    pop[i].position[j]=x_low[j];
                }
            }

        }
    }
    //更新适应值
    public void up_date(int it){
        for(int i=0;i<this.nPop;i++){
            //计算适应值
            pop[i].fitness=function_fitness(pop[i].position);
            worstFitness = Math.max(pop[i].fitness, worstFitness);
            // 如果个体的适应值大于个体历史最优适应值，则更新个体历史最优适应值，位置信息同样的也更新
            if(pop[i].fitness < pop[i].P_fitness){
                pop[i].P_position=(double[])pop[i].position.clone();
                pop[i].P_fitness = pop[i].fitness;
                // 如果个体的适应值比全局的适应值优，则更新全局的适应值和位置
                if(pop[i].fitness < best_fitness)
                {
                    best_fitness=pop[i].fitness;
                    best_solution = pop[i].position.clone();
                    best_speed = pop[i].velecity.clone();
                }
            }

//            pop[i].sum += pop[i].fitness;
//            double sigma = Math.abs(pop[i].P_fitness - pop[i].sum / it);
//            double lambda = (pop[i].sum / it) / nPop;
//            if(sigma < lambda){
//                for(int j=0; j<nVar; j++){
//                    pop[i].position[j] = Math.abs(worstFitness - pop[i].fitness) * (best_speed[j] + best_fitness);
//                    if(pop[i].position[j]>x_up[j]){
//                        pop[i].position[j]=x_up[j];
//                    }
//                    if(pop[i].position[j]<x_low[j]){
//                        pop[i].position[j]=x_low[j];
//                    }
//                }
//            }

            adamDeltas[i].fitnessWindow.insertOriginElement(pop[i].fitness);
            if(adamDeltas[i].fitnessWindow.getMinElement() > pop[i].P_fitness || Math.abs(adamDeltas[i].fitnessWindow.getElementAVG() - pop[i].P_fitness) < 1e-3){
                for(int j=0; j<nVar; j++){
                    pop[i].position[j] += pop[i].velecity[j]*Math.abs(worstFitness - pop[i].fitness)*ParticleSwarm.rand1.nextDouble();
                    if(pop[i].position[j]>x_up[j]){
                        pop[i].position[j]=x_up[j];
                    }
                    if(pop[i].position[j]<x_low[j]){
                        pop[i].position[j]=x_low[j];
                    }
                }
                pop[i].P_fitness = function_fitness(pop[i].position);
                adamDeltas[i].fitnessWindow = new WindowQueue(WindowSize);
            }
        }
    }
    // 显示结果，显示每一次迭代计算后的最优适应值
    public void show_result(int Iter_c){
//        System.out.printf("Iteration: %3d , global best fit:%5f\n",Iter_c,best_fitness);
        if(Iter_c==(MaxIter-1) && false)
        {
            for(int i=0;i<nVar;i++){
                System.out.println(best_solution[i]);
            }
//            System.out.println("The PSO end ,plase look up the result if need!");
        }
    }
    // PSO 程序开始运行
    public JSONObject run()
    {
//        up_date();
        // 按照设置的最大迭代次数迭代计算
        for(int it =0;it<MaxIter;it++){
            this.R1 = ParticleSwarm.getR1(it, MaxIter);
            this.R2 = ParticleSwarm.getR2(it, MaxIter);
            this.R1 = ParticleSwarm.rand1.nextDouble();
            this.R2 = ParticleSwarm.rand1.nextDouble();
            this.w = getW(it, MaxIter);

            up_search();	// 速度位置更新
            up_date(it+1);		// 适应值的更新
            show_result(it);// 输出结果的显示

            try{
                if(GRAPH_SHOW){
                    MainPSO.outputPSO(this, dataSet);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("best_fit", best_fitness);
        jsonObject.put("best_sov", best_solution);
        return jsonObject;
    }

    private double getC1(int i, int j){
//        double c1 = adamDeltas[i].list[j].getParamsRes();
        return c1;
    }

    private double getC2(int i, int j){
//        double c2 = adamDeltas[i].globalList[j].getParamsRes();
        return c2;
    }

    private double getW(int i, int max){
        double x = (double) i / max;
        x = 2*x - 1;
        double w = Math.cos(x) / (1 + Math.exp(3*x)) + 0.1;
//        w = 0.9 - 0.8*((double) i / max);
//        w = 0.4 + 0.2*ParticleSwarm.rand1.nextDouble();
        return w;
    }
}

// 定义主类进行计算
class MainPSO{
    public static void main(String[] args){
        // 初始赋值
        double[] sum = new double[2];
        double smallestNum = 1e20;
        sum[0] = 0;
        sum[1] = 0;

        for(int i = 0; i < PSO.TEST_COUNT; i++){
            int dimension = 1;
            double[] x_low = new double[dimension], x_up = new double[dimension];
            for(int j=0; j<dimension; j++){
//                x_low = new double[]{-15, -3};
//                x_up = new double[]{-5, 3};
                x_low[j] = 0;
                x_up[j] = 20;
            }
            System.out.println("The PSO START....");
//            long st = System.currentTimeMillis();
            PSO pso=new PSO(1.49445,1.49445,0.729,dimension,500,50,x_low,x_up);
            JSONObject run = pso.run();
//            long et = System.currentTimeMillis();
//            System.out.println(et - st);
            sum[1] += run.getDouble("best_fit");
            smallestNum = Math.min(run.getDouble("best_fit"), smallestNum);
            System.out.println("The PSO END....");
//            System.out.println("worstFitness : " + pso.worstFitness);
//            System.out.println("bestSpeed : " + Arrays.toString(pso.best_speed));
        }
        System.out.println("bestFit(mean): " + sum[1]/ PSO.TEST_COUNT);
        System.out.println("bestFit: " + smallestNum);
    }

    public static void outputPSO(PSO pso, XYSeriesCollection dataSet) throws InterruptedException {
        XYSeries pointData = new XYSeries("data");
        for(int j=0; j<pso.nPop; j++){
            pointData.add(pso.pop[j].position[0], pso.pop[j].position[1]);
        }
        pointData.add(pso.x_low[0], pso.x_low.length == 1 ? 0 : pso.x_low[1]);
        pointData.add(pso.x_up[0], pso.x_up.length == 1 ? 0 : pso.x_up[1]);
        dataSet.removeAllSeries();
        dataSet.addSeries(pointData);
        Thread.sleep(50);
    }
}