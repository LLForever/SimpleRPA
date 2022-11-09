package com.simplerpa.cloudservice.utils;

import com.google.common.util.concurrent.AtomicDouble;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TaskCostCountUtil {
    private static final AtomicDouble cost;
    private static final HashMap<Long, List<Double>> taskCostMap;
    private static final Integer CPU_ID = 0, MEM_ID = 1, NET_ID = 2;
    private static final Double MAX_MEM = 7819.15;
    static {
        cost = new AtomicDouble();
        cost.set(0.0);

        taskCostMap = new HashMap<>();
        taskCostMap.put(16L, getDoubleList(7.469, 12.709, 166.39, 13.751, 31.0));
        taskCostMap.put(17L, getDoubleList(6.704, 9.802, 219.39, 31.686, 15.0));
        taskCostMap.put(18L, getDoubleList(6.277, 8.79, 156.14, 8.698, 60.0));
        taskCostMap.put(19L, getDoubleList(7.127, 9.70, 164.98, 14.308, 32.0));
        taskCostMap.put(20L, getDoubleList(6.589, 10.21, 151.14, 9.953, 46.0));
        taskCostMap.put(21L, getDoubleList(9.41, 28.42, 837.43, 10.192, 57.0));
        taskCostMap.put(22L, getDoubleList(13.15, 28.36, 1025.56, 9.518, 63.0));
        taskCostMap.put(23L, getDoubleList(6.67, 7.34, 113.37, 34.891, 14.0));
        taskCostMap.put(24L, getDoubleList(2.56, 2.56, 1.0, 0.0, 1.0));
        taskCostMap.put(25L, getDoubleList(7.11, 9.91, 142.07, 10.172, 44.0));
        taskCostMap.put(26L, getDoubleList(11.1, 51.65, 1701.99, 3.949, 149.0));
        taskCostMap.put(27L, getDoubleList(7.04, 10.92, 121.19, 14.857, 28.0));
        taskCostMap.put(28L, getDoubleList(7.31, 10.76, 124.32, 13.765, 29.0));
        taskCostMap.put(29L, getDoubleList(8.17, 9.52, 117.75, 33.72, 14.0));
        taskCostMap.put(30L, getDoubleList(10.62, 11.72, 172.18, 16.325, 30.0));
        taskCostMap.put(31L, getDoubleList(11.38, 14.21, 252.16, 11.53, 58.0));
        taskCostMap.put(32L, getDoubleList(12.4, 27.35, 215.73, 6.498, 74.0));
//        taskCostMap.put(33, getDoubleList(8.11, 11.55, 120.75, 7.602, 29.0));
        taskCostMap.put(34L, getDoubleList(7.12, 10.72, 117.91, 6.988, 27.0));
    }
    public static void addCost(Double num){
        cost.addAndGet(num);
    }
    public static void minusCost(Double num){
        cost.addAndGet(-num);
    }
    private static List<Double> getDoubleList(Double avg_cpu, Double max_cpu, Double mem, Double net, Double time){
        return Arrays.asList(avg_cpu*0.5+max_cpu*0.5, mem/MAX_MEM, net, time);
    }
    public static List<Double> getCostListByTaskId(Long id){
        if(taskCostMap.containsKey(id)){
            return taskCostMap.get(id);
        }
        return null;
    }
    public static Double getSumCostById(Long id){
        if(taskCostMap.containsKey(id)){
            return taskCostMap.get(id).get(0) + taskCostMap.get(id).get(1) + taskCostMap.get(id).get(2);
        }
        return 0.0;
    }
}
