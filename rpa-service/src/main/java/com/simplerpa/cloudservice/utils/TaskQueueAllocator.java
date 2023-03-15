package com.simplerpa.cloudservice.utils;

import com.simplerpa.cloudservice.entity.VO.TaskDetailVO;
import lombok.SneakyThrows;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;

public class TaskQueueAllocator implements Runnable{
    private static final ConcurrentLinkedDeque<TaskDetailVO> queue = new ConcurrentLinkedDeque<>();
    private static int node;
    private static boolean isRunning = false;

    @SneakyThrows
    @Override
    public void run() {
        while(true){
            if(queue.isEmpty()){
                synchronized (queue){
                    if(queue.isEmpty()){
                        isRunning = false;
                        break;
                    }
                }
            }
            double rm = TaskCostCountUtil.getRm(node);
            TaskDetailVO first = queue.getFirst();
            List<Double> costListByTaskId = TaskCostCountUtil.getCostListByTaskId(first.getId());
            Double memory = costListByTaskId.get(TaskCostCountUtil.MEM_ID);
            memory = TaskCostCountUtil.getMemCost(node, memory);
            if(rm - memory >= 0 || rm > 15){
                try{
                    ThreadPoolSingleton.getInstance().submit(new RpaTaskExecutor(first));
                }catch (Exception e){
                    e.printStackTrace();
                    continue;
                }
                queue.removeFirst();
                TaskCostCountUtil.setRm(rm - memory);
            }else{
                Thread.sleep(500);
            }
        }
    }

    public static void pushElement(TaskDetailVO vo){
        synchronized (queue){
            queue.add(vo);
            if(!isRunning){
                Thread thread = new Thread(new TaskQueueAllocator());
                thread.start();
                isRunning = true;
                TaskCostCountUtil.setNodeNum(node);
            }
        }
    }

    public static void setNode(int node) {
        TaskQueueAllocator.node = node;
    }

    public static boolean isIsRunning() {
        return isRunning;
    }

    public static int getQSize(){
        return queue.size();
    }
}
