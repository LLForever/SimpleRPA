package com.simplerpa.cloudservice.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * rpa面板任务详情对象 rpa_panel_task_detail
 *
 * @author rpa
 * @date 2022-04-20
 */
public class TaskDetail extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 任务ID */
    @Excel(name = "任务ID")
    private Long taskId;

    /** 任务执行状态 */
    @Excel(name = "任务执行状态")
    private String taskStatus;

    /** 任务执行进度 */
    @Excel(name = "任务执行进度")
    private Double taskProgress;

    /** 任务版本号 */
    @Excel(name = "任务版本号")
    private Long taskVersion;

    /** 任务名称 */
    @Excel(name = "任务名称")
    private String taskName;

    /** easy-flow的线段信息 */
    @Excel(name = "easy-flow的线段信息")
    private String lineListJson;

    /** easy-flow的节点信息 */
    @Excel(name = "easy-flow的节点信息")
    private String nodeListJson;

    /** 创建任务的用户ID */
    @Excel(name = "创建任务的用户ID")
    private Long userId;

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
    public void setTaskStatus(String taskStatus)
    {
        this.taskStatus = taskStatus;
    }

    public String getTaskStatus()
    {
        return taskStatus;
    }
    public void setTaskProgress(Double taskProgress)
    {
        this.taskProgress = taskProgress;
    }

    public Double getTaskProgress()
    {
        return taskProgress;
    }
    public void setTaskVersion(Long taskVersion)
    {
        this.taskVersion = taskVersion;
    }

    public Long getTaskVersion()
    {
        return taskVersion;
    }
    public void setTaskName(String taskName)
    {
        this.taskName = taskName;
    }

    public String getTaskName()
    {
        return taskName;
    }
    public void setLineListJson(String lineListJson)
    {
        this.lineListJson = lineListJson;
    }

    public String getLineListJson()
    {
        return lineListJson;
    }
    public void setNodeListJson(String nodeListJson)
    {
        this.nodeListJson = nodeListJson;
    }

    public String getNodeListJson()
    {
        return nodeListJson;
    }
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("taskId", getTaskId())
                .append("taskStatus", getTaskStatus())
                .append("taskProgress", getTaskProgress())
                .append("taskVersion", getTaskVersion())
                .append("taskName", getTaskName())
                .append("lineListJson", getLineListJson())
                .append("nodeListJson", getNodeListJson())
                .append("userId", getUserId())
                .toString();
    }
}