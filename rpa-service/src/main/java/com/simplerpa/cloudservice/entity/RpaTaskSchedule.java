package com.simplerpa.cloudservice.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * rpa任务计划详情对象 rpa_task_schedule
 * 
 * @author ChenRui98
 * @date 2023-03-15
 */
public class RpaTaskSchedule extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 任务ID */
    @Excel(name = "任务ID")
    private Long taskId;

    /** 任务名称 */
    @Excel(name = "任务名称")
    private String taskName;

    /** 创建任务的用户ID */
    @Excel(name = "创建任务的用户ID")
    private Long userId;

    /** 任务创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "任务创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date creationTime;

    /** 计划含义(中文) */
    @Excel(name = "计划含义(中文)")
    private String scheduleMeaning;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setTaskId(Long taskId) 
    {
        this.taskId = taskId;
    }

    public Long getTaskId() 
    {
        return taskId;
    }
    public void setTaskName(String taskName) 
    {
        this.taskName = taskName;
    }

    public String getTaskName() 
    {
        return taskName;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setCreationTime(Date creationTime) 
    {
        this.creationTime = creationTime;
    }

    public Date getCreationTime() 
    {
        return creationTime;
    }
    public void setScheduleMeaning(String scheduleMeaning) 
    {
        this.scheduleMeaning = scheduleMeaning;
    }

    public String getScheduleMeaning() 
    {
        return scheduleMeaning;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("taskId", getTaskId())
            .append("taskName", getTaskName())
            .append("userId", getUserId())
            .append("creationTime", getCreationTime())
            .append("scheduleMeaning", getScheduleMeaning())
            .toString();
    }
}
