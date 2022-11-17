package com.simplerpa.cloudservice.utils.gentic;

import com.alibaba.fastjson.JSONObject;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author JackJun
 * 2019/8/12 20:04
 * Life is just about survival.
 */
public class Population {

    /**
     * 种群个体数量
     */
    private int count=50;

    /**
     * 种群变异率
     */
    private double variation=0.1;

    /**
     * 最优个体保留数目
     */
    private int retain=10;

    private double crossRate=0.5;

    /**
     * 最大变异基因个数
     */
    private int maxVariationNum = 10;

    private List<Individual> individuals = new ArrayList<>();

    private final JSONObject machinesAllocateInfo, machinePerformanceJSON;
    private final Long[] ids;


    public Population(JSONObject machinesAllocateInfo, JSONObject machinePerformanceJSON, Long[] ids){
        this.machinePerformanceJSON = machinePerformanceJSON;
        this.machinesAllocateInfo = machinesAllocateInfo;
        this.ids = ids;
        initPopulation(100,0.1,4,0.5);
    }

    public JSONObject run(int cnt){
        for(int i=0; i < cnt; i++){
            reproduction();
        }
        Individual topIndividual = getTopIndividual();
        double best_fitness = topIndividual.getFitness();
        double[] best_solve = new double[topIndividual.getGenes().size()];
        for(int i=0; i < topIndividual.getGenes().size(); i++){
            Gene gene = topIndividual.getGenes().get(i);
            best_solve[i] = gene.decode(gene.getGene());
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("best_fit", best_fitness);
        jsonObject.put("best_sov", best_solve);
        return jsonObject;
    }

    /**
     * 初始化种群
     * @param count 种群个体数量
     * @param variation 种群变异率
     * @param retain 最优个体保留数目
     */
    public void initPopulation(int count,double variation,int retain,double crossRate){
        if(count<=0 || variation <0 || variation>1 || retain<=0 || crossRate<0){
            throw new IllegalArgumentException("参数错误!");
        }
        this.individuals = new ArrayList<>();
        this.variation = variation;
        this.retain = retain;
        this.crossRate = crossRate;
        for(int i=0;i<count;i++){
            Individual individual = new Individual(machinesAllocateInfo, machinePerformanceJSON, ids);
            this.individuals.add(individual);
        }
    }

    /**
     * 繁衍一代
     */
    public void reproduction(){
        select();
        cross();
        variation();
    }

    /**
     * 获取最优秀子代
     */
    public Individual getTopIndividual(){
        //计算适应度
        individuals.forEach(item -> item.setFitness(item.calcFitness()));
        return individuals.stream().max((o1, o2) -> (int)(o1.calcFitness()*1000-o2.calcFitness()*1000)).orElse(new Individual(machinesAllocateInfo, machinePerformanceJSON, ids));
    }

    /**
     * 选择
     */
    private void select(){
        List<Individual> sorted = individuals.stream().peek(item->item.setFitness(item.calcFitness())).sorted((o1, o2) -> {
            double v = o2.getFitness() - o1.getFitness();
            if(v > 1e-10){
                return 1;
            }else if(v < -1e-10){
                return -1;
            }else{
                return 0;
            }
        }).collect(Collectors.toList());

        // 选择后种群
        List<Individual> newPopulation = new ArrayList<>();
        // 复制最优个体
        for(int i=0;i<retain;i++){

            newPopulation.add(sorted.get(i));
        }

        for(int i=0;i<count-retain;i++){
            Individual selected = null;
            while(selected==null) {
                int num = new Random().nextInt(count);
                Individual individual = individuals.get(num);
                double probability = (individual.getFitness() + 20) / 40;
                double val = Math.random();
                if(val<=probability){
                    selected=individual;
                }
            }
            newPopulation.add(selected);
        }
        this.individuals=newPopulation;
    }

    /**
     * 交叉
     */
    private void cross(){
        List<Individual> newIndividual = new ArrayList<>();
        for(Individual item : individuals){
            double rate = Math.random();
            //进行交叉
            if (rate <= this.crossRate) {
                Individual target = individuals.get(new Random().nextInt(individuals.size()));
                ArrayList<Gene> genes = new ArrayList<>();
                int index = new Random().nextInt(item.getGenes().get(0).getGene().length);

                for (int z=0; z<ids.length; z++){
                    Gene p1 = item.getGenes().get(z);
                    Gene p2 = target.getGenes().get(z);

                    int[] newGene = new int[p1.getGene().length];
                    for(int i=0;i<newGene.length;i++){
                        if(i<=index){
                            newGene[i]=p1.getGene()[i];
                        }else{
                            newGene[i]=p2.getGene()[i];
                        }
                    }
                    Gene resGene = new Gene();
                    resGene.setGene(newGene);
                    genes.add(resGene);
                }
                Individual result = new Individual(genes, machinesAllocateInfo, machinePerformanceJSON, ids);
                newIndividual.add(result);
            }
        }
        individuals.addAll(newIndividual);
    }

    /**
     * 变异
     */
    private void variation(){
        int num=0;
        for(Individual item : individuals){
            double rate = Math.random();
            //进行变异 最优子代不变异
            if(num!=0 && rate<=this.variation){
                int count = new Random().nextInt(maxVariationNum+1);
                for(int i=0;i<count;i++){
                    Gene gene = item.getGenes().get(0);
                    int index = new Random().nextInt(gene.getGene().length);
                    gene.getGene()[index]^=1;
                }
            }
            num++;
        }
    }

    public List<Individual> getIndividuals() {
        return individuals;
    }

    public void setIndividuals(List<Individual> individuals) {
        this.individuals = individuals;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getVariation() {
        return variation;
    }

    public void setVariation(double variation) {
        this.variation = variation;
    }

    public int getRetain() {
        return retain;
    }

    public void setRetain(int retain) {
        this.retain = retain;
    }

    public double getCrossRate() {
        return crossRate;
    }

    public void setCrossRate(double crossRate) {
        this.crossRate = crossRate;
    }
}
