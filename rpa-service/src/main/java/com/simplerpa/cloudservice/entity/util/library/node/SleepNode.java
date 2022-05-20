package com.simplerpa.cloudservice.entity.util.library.node;

import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.RpaTaskOutput;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;

public class SleepNode implements IRpaTaskNode {
    private final TaskNodeDetail nodeDetail;
    private Long sleepTime;
    private Integer unit;
    private static final Integer MILLIS_SECOND = 0;
    private static final Integer SECOND = 1;
    private static final Integer MINUTE = 2;
    private static final Integer HOUR = 3;
    private static final Long MAX_SLEEP_TIME = (long) (1000 * 60 * 60 * 12);

    public SleepNode(TaskNodeDetail nodeDetail){
        this.nodeDetail = nodeDetail;
    }

    @Override
    public RpaTaskOutput run(RpaTaskOutput input) throws Exception {
        if(unit == null || sleepTime == null){
            throw new Exception(this.getClass().getName() + " : 缺少必要参数，执行失败！");
        }
        long time = 0L;
        if(SECOND.equals(unit)){
            time += sleepTime*1000;
        }else if(MINUTE.equals(unit)){
            time += sleepTime*1000*60;
        }else if(HOUR.equals(unit)){
            time += sleepTime*1000*60*60;
        }
        time = Math.min(time, MAX_SLEEP_TIME);
        Thread.sleep(time);
        return null;
    }

    @Override
    public TaskNodeDetail getRpaTaskDetail() {
        return nodeDetail;
    }

    public Long getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(Long sleepTime) {
        this.sleepTime = sleepTime;
    }

    public Integer getUnit() {
        return unit;
    }

    public void setUnit(Integer unit) {
        this.unit = unit;
    }
}
