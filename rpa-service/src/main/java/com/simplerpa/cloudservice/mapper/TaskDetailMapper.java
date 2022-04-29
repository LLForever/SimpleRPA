package com.simplerpa.cloudservice.mapper;

import java.util.List;
import com.simplerpa.cloudservice.entity.TaskDetail;

/**
 * rpa面板任务详情Mapper接口
 * 
 * @author simpleRPA
 * @date 2022-04-20
 */
public interface TaskDetailMapper 
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

    public int uploadTaskDetail(TaskDetail taskDetail);

    /**
     * 删除rpa面板任务详情
     * 
     * @param id rpa面板任务详情主键
     * @return 结果
     */
    public int deleteTaskDetailById(Long id);

    /**
     * 批量删除rpa面板任务详情
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTaskDetailByIds(Long[] ids);

    public TaskDetail findTaskDetailByTaskId(Long id);
}
