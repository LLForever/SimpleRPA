package com.simplerpa.cloudservice.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.simplerpa.cloudservice.mapper.TaskManagerEntityMapper;
import com.simplerpa.cloudservice.entity.TaskManagerEntity;
import com.simplerpa.cloudservice.service.ITaskManagerEntityService;

/**
 * 任务管理Service业务层处理
 * 
 * @author ChenRui98
 * @date 2023-03-22
 */
@Service
public class TaskManagerEntityServiceImpl implements ITaskManagerEntityService 
{
    @Autowired
    private TaskManagerEntityMapper taskManagerEntityMapper;

    /**
     * 查询任务管理
     * 
     * @param id 任务管理主键
     * @return 任务管理
     */
    @Override
    public TaskManagerEntity selectTaskManagerEntityById(Long id)
    {
        return taskManagerEntityMapper.selectTaskManagerEntityById(id);
    }

    /**
     * 查询任务管理列表
     * 
     * @param taskManagerEntity 任务管理
     * @return 任务管理
     */
    @Override
    public List<TaskManagerEntity> selectTaskManagerEntityList(TaskManagerEntity taskManagerEntity)
    {
        return taskManagerEntityMapper.selectTaskManagerEntityList(taskManagerEntity);
    }

    /**
     * 新增任务管理
     * 
     * @param taskManagerEntity 任务管理
     * @return 结果
     */
    @Override
    public int insertTaskManagerEntity(TaskManagerEntity taskManagerEntity)
    {
        return taskManagerEntityMapper.insertTaskManagerEntity(taskManagerEntity);
    }

    /**
     * 修改任务管理
     * 
     * @param taskManagerEntity 任务管理
     * @return 结果
     */
    @Override
    public int updateTaskManagerEntity(TaskManagerEntity taskManagerEntity)
    {
        return taskManagerEntityMapper.updateTaskManagerEntity(taskManagerEntity);
    }

    /**
     * 批量删除任务管理
     * 
     * @param ids 需要删除的任务管理主键
     * @return 结果
     */
    @Override
    public int deleteTaskManagerEntityByIds(Long[] ids)
    {
        return taskManagerEntityMapper.deleteTaskManagerEntityByIds(ids);
    }

    /**
     * 删除任务管理信息
     * 
     * @param id 任务管理主键
     * @return 结果
     */
    @Override
    public int deleteTaskManagerEntityById(Long id)
    {
        return taskManagerEntityMapper.deleteTaskManagerEntityById(id);
    }
}
