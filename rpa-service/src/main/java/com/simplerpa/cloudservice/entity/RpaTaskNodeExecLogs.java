package com.simplerpa.cloudservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

@TableName("rpa_task_node_exec_logs")
public class RpaTaskNodeExecLogs {
    public RpaTaskNodeExecLogs(){}

    public RpaTaskNodeExecLogs(Long taskId, String taskName, String nodeId, String taskMsg, Date createdTime){
        setCreatedTime(createdTime);
        setNodeId(nodeId);
        setTaskId(taskId);
        setTaskName(taskName);
        setTaskMsg(taskMsg);
    }

    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long taskId;

    private String taskName;

    private String nodeId;

    private String taskMsg;

    private Date createdTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getTaskMsg() {
        return taskMsg;
    }

    public void setTaskMsg(String taskMsg) {
        this.taskMsg = taskMsg;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
