package com.simplerpa.cloudservice.utils.gentic;

import com.alibaba.fastjson.JSONObject;
import com.simplerpa.cloudservice.entity.util.DictionaryUtil;
import com.simplerpa.cloudservice.utils.TaskCostCountUtil;
import com.simplerpa.cloudservice.utils.TaskScheduleAllocator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author JackJun
 * 2019/8/12 19:49
 * Life is just about survival.
 * 单个个体
 */
public class Individual {
    private List<Gene> genes;

    private final JSONObject machinesAllocateInfo, machinePerformanceJSON;
    private final Long[] ids;

    private double fitness;

    public Individual(JSONObject machinesAllocateInfo, JSONObject machinePerformanceJSON, Long[] ids){
        genes = new ArrayList<>();
        Random random = new Random();
        for (int i=0; i<ids.length; i++){
            Gene gene = new Gene();
            int[] geneArr = gene.getGene();
            for (int j=0;j<geneArr.length;j++){
                geneArr[j] = random.nextInt(2);
            }
            genes.add(gene);
        }
        this.ids = ids;
        this.machinePerformanceJSON = machinePerformanceJSON;
        this.machinesAllocateInfo = machinesAllocateInfo;
    }

    public Individual(List<Gene> list, JSONObject machinesAllocateInfo, JSONObject machinePerformanceJSON, Long[] ids){
        setGenes(list);
        this.ids = ids;
        this.machinePerformanceJSON = machinePerformanceJSON;
        this.machinesAllocateInfo = machinesAllocateInfo;
    }

    /**
     * 计算个体适应度
     * @return y值
     */
//    public double calcFitness(){
//        Gene gene;
//        if((gene=genes.get(0))==null){
//            return 0;
//        }
//        double val = gene.decode(gene.getGene());
//        return val*Math.sin(2*Math.PI*val)+2;
//    }

    public double calcFitness(){
        double[][] sumList = new double[4][3];
        double sum;
        double F = 0.0, G = 0.0, T[] = new double[TaskScheduleAllocator.machineName.size()];
        double maxBalance = -1;
        for(int i=0; i<ids.length; i++){
            Gene gene = genes.get(i);
            double val = gene.decode(gene.getGene());
            int targetMachine = (int) Math.abs(val);
            if(targetMachine > 3){
                targetMachine = 3;
            }
            List<Double> costListByTaskId = TaskCostCountUtil.getCostListByTaskId(ids[i]);
            sumList[targetMachine][0] += costListByTaskId.get(0); // cpu
            sumList[targetMachine][1] += costListByTaskId.get(1); // mem
            sumList[targetMachine][2] += costListByTaskId.get(2); // net
            T[targetMachine] += costListByTaskId.get(3);
        }
        for(int i=0; i<4; i++){
            String machineName = TaskScheduleAllocator.machineName.get(i);
            double[] machineCostList = machinesAllocateInfo.getJSONObject(machineName).getObject(TaskCostCountUtil.LIST, double[].class);
            sumList[i][0] += machineCostList[0];
            sumList[i][1] += machineCostList[1];
            sumList[i][2] += machineCostList[2];
        }
        for(int i=0; i<4; i++){
            sumList[i][1] = TaskCostCountUtil.getMemCost(i, sumList[i][1]);
            double Nc, Nm, Nn;
            double Rc, Rm, Rn;
            Nc = machinePerformanceJSON.getJSONObject(TaskScheduleAllocator.machineName.get(i)).getDouble("cpu");
            Nm = machinePerformanceJSON.getJSONObject(TaskScheduleAllocator.machineName.get(i)).getDouble("mem");
            Nn = machinePerformanceJSON.getJSONObject(TaskScheduleAllocator.machineName.get(i)).getDouble("net");
            Rc = 100-Nc;
            Rm = 100-Nm;
            Rn = 100-Nn;

            Rc = DictionaryUtil.checkValueAndChange(sumList[i][0]/Rc);
            Rm = DictionaryUtil.checkValueAndChange(sumList[i][1]/Rm);
            Rn = DictionaryUtil.checkValueAndChange(sumList[i][2]/Rn);
            Nc = DictionaryUtil.checkValueAndChange(sumList[i][0] + Nc);
            Nm = DictionaryUtil.checkValueAndChange(sumList[i][1] + Nm);
            Nn = DictionaryUtil.checkValueAndChange(sumList[i][2] + Nn);

            F += Math.sqrt(Rc*Rc + Rm*Rm + Rn*Rn)*T[i];
            G += Math.sqrt(Nc*Nc + Nm*Nm + Nn*Nn);
            maxBalance = Math.max(maxBalance, Math.sqrt(Nc*Nc + Nm*Nm + Nn*Nn));
        }
        sum = DictionaryUtil.F_VAL*F + 0.5*DictionaryUtil.G_VAL*(G + maxBalance*TaskScheduleAllocator.machineName.size());
        return 1/Math.log(sum);
    }

    public List<Gene> getGenes() {
        return genes;
    }

    public void setGenes(List<Gene> genes) {
        this.genes = genes;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }
}
