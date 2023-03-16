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
 * RPA任务协作对象 rpa_task_cooperation
 * 
 * @author ChenRui98
 * @date 2023-03-15
 */
public class RpaTaskCooperation
{
    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 任务ID */
    @Excel(name = "任务ID")
    private Long taskId;

    /** 导入此任务作为协作者的用户ID */
    @Excel(name = "导入此任务作为协作者的用户ID")
    private Long userId;

    /** 任务名称 */
    @Excel(name = "任务名称")
    private String taskName;

    /** 任务导入时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "任务导入时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date importTime;

    private Integer coEnable;

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
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setTaskName(String taskName) 
    {
        this.taskName = taskName;
    }

    public String getTaskName() 
    {
        return taskName;
    }
    public void setImportTime(Date importTime) 
    {
        this.importTime = importTime;
    }

    public Date getImportTime() 
    {
        return importTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("taskId", getTaskId())
            .append("userId", getUserId())
            .append("taskName", getTaskName())
            .append("importTime", getImportTime())
            .toString();
    }

    public Integer getCoEnable() {
        return coEnable;
    }

    public void setCoEnable(Integer coEnable) {
        this.coEnable = coEnable;
    }
}
