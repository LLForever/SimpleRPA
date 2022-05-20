package com.simplerpa.cloudservice.service.impl;

import java.util.List;

import com.simplerpa.cloudservice.entity.util.DictionaryUtil;
import com.simplerpa.cloudservice.mapper.TaskDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.simplerpa.cloudservice.entity.TaskDetail;
import com.simplerpa.cloudservice.service.ITaskDetailService;

/**
 * rpa面板任务详情Service业务层处理
 * 
 * @author simpleRPA
 * @date 2022-04-20
 */
@Service
public class TaskDetailServiceImpl implements ITaskDetailService 
{
    @Autowired
    private TaskDetailMapper taskDetailMapper;

    /**
     * 查询rpa面板任务详情
     * 
     * @param id rpa面板任务详情主键
     * @return rpa面板任务详情
     */
    @Override
    public TaskDetail selectTaskDetailById(Long id)
    {
        return taskDetailMapper.selectTaskDetailById(id);
    }

    /**
     * 查询rpa面板任务详情列表
     * 
     * @param taskDetail rpa面板任务详情
     * @return rpa面板任务详情
     */
    @Override
    public List<TaskDetail> selectTaskDetailList(TaskDetail taskDetail)
    {
        return taskDetailMapper.selectTaskDetailList(taskDetail);
    }

    /**
     * 新增rpa面板任务详情
     * 
     * @param taskDetail rpa面板任务详情
     * @return 结果
     */
    @Override
    public int insertTaskDetail(TaskDetail taskDetail)
    {
        return taskDetailMapper.insertTaskDetail(taskDetail);
    }

    /**
     * 修改rpa面板任务详情
     * 
     * @param taskDetail rpa面板任务详情
     * @return 结果
     */
    @Override
    public int updateTaskDetail(TaskDetail taskDetail)
    {
        return taskDetailMapper.updateTaskDetail(taskDetail);
    }

    @Override
    public Boolean uploadTaskDetail(TaskDetail taskDetail){
        return taskDetailMapper.uploadTaskDetail(taskDetail) > 0;
    }

    @Override
    public TaskDetail getTaskDetailByTaskIdAndUserId(TaskDetail taskDetail){
        return taskDetailMapper.getTaskDetailByTaskIdAndUserId(taskDetail);
    }

    /**
     * 批量删除rpa面板任务详情
     * 
     * @param ids 需要删除的rpa面板任务详情主键
     * @return 结果
     */
    @Override
    public int deleteTaskDetailByIds(Long[] ids)
    {
        return taskDetailMapper.deleteTaskDetailByIds(ids);
    }

    /**
     * 删除rpa面板任务详情信息
     * 
     * @param id rpa面板任务详情主键
     * @return 结果
     */
    @Override
    public int deleteTaskDetailById(Long id)
    {
        return taskDetailMapper.deleteTaskDetailById(id);
    }

    @Override
    public TaskDetail findTaskDetailByTaskId(Long id) {
        return taskDetailMapper.findTaskDetailByTaskId(id);
    }

    @Override
    public Boolean changeRpaTaskStatus(String status, Long taskId, Long userId) {
        if(DictionaryUtil.taskStatusAvailable(status)){
            int i = taskDetailMapper.changeRpaTaskStatus(status, taskId, userId);
            return i > 0;
        }
        return false;
    }


}
