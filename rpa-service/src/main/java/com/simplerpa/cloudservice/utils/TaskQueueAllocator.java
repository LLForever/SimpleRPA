package com.simplerpa.cloudservice.utils;

import com.simplerpa.cloudservice.entity.VO.TaskDetailVO;
import lombok.SneakyThrows;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

public class TaskQueueAllocator implements Runnable{
    private static final ConcurrentLinkedDeque<TaskDetailVO> queue = new ConcurrentLinkedDeque<>();
    private static int node;
    private static volatile boolean isRunning = false;

    @SneakyThrows
    @Override
    public void run() {
        while(!queue.isEmpty()){
            double rm = TaskCostCountUtil.getRm(node);
            TaskDetailVO first = queue.getFirst();
            List<Double> costListByTaskId = TaskCostCountUtil.getCostListByTaskId(first.getId());
            Double memory = costListByTaskId.get(TaskCostCountUtil.MEM_ID);
            memory = TaskCostCountUtil.getMemCost(node, memory);
            if(rm - memory >= 0 || rm < 90.0){
                TaskCostCountUtil.setRm(rm - memory);
                ThreadPoolSingleton.getInstance().submit(new RpaTaskExecutor(first));
                queue.removeFirst();
            }else{
                Thread.sleep(500);
            }
        }
        isRunning = false;
    }

    public static void pushElement(TaskDetailVO vo){
        queue.add(vo);
        if(!isRunning){
            synchronized (TaskQueueAllocator.class){
                if(!isRunning){
                    Thread thread = new Thread(new TaskQueueAllocator());
                    thread.start();
                    isRunning = true;
                    TaskCostCountUtil.setNodeNum(node);
                }
            }
        }
    }

    public static void setNode(int node) {
        TaskQueueAllocator.node = node;
    }
}
