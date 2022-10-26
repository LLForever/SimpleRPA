package com.simplerpa.cloudservice.service;

import java.util.List;
import com.simplerpa.cloudservice.entity.TaskDetail;

/**
 * rpa面板任务详情Service接口
 * 
 * @author simpleRPA
 * @date 2022-04-20
 */
public interface ITaskDetailService 
{
    /**
     * 查询rpa面板任务详情
     * 
     * @param id rpa面板任务详情主键
     * @return rpa面板任务详情
     */
    public TaskDetail selectTaskDetailById(Long id);

    /**
     * 查询rpa面板任务详情列表
     * 
     * @param taskDetail rpa面板任务详情
     * @return rpa面板任务详情集合
     */
    public List<TaskDetail> selectTaskDetailList(TaskDetail taskDetail);

    /**
     * 新增rpa面板任务详情
     * 
     * @param taskDetail rpa面板任务详情
     * @return 结果
     */
    public int insertTaskDetail(TaskDetail taskDetail);

    /**
     * 修改rpa面板任务详情
     * 
     * @param taskDetail rpa面板任务详情
     * @return 结果
     */
    public int updateTaskDetail(TaskDetail taskDetail);

    public Boolean uploadTaskDetail(TaskDetail taskDetail);

    public TaskDetail getTaskDetailByTaskIdAndUserId(TaskDetail taskDetail);

    /**
     * 批量删除rpa面板任务详情
     * 
     * @param ids 需要删除的rpa面板任务详情主键集合
     * @return 结果
     */
    public int deleteTaskDetailByIds(Long[] ids);

    /**
     * 删除rpa面板任务详情信息
     * 
     * @param id rpa面板任务详情主键
     * @return 结果
     */
    public int deleteTaskDetailById(Long id);

    /**
     * 通过taskId找详细信息
     * */
    public TaskDetail findTaskDetailByTaskId(Long id);

    /**
     * 修改RPA任务状态
     * @param status RPA状态字段
     * */
    public Boolean changeRpaTaskStatus(String status, Long taskId, Long userId);

    public Boolean saveImageInfo(Long taskId, String nodeId, byte[] img);
}
