package com.simplerpa.cloudservice.service;

import java.util.List;
import com.simplerpa.cloudservice.entity.TaskManagerEntity;

/**
 * 任务管理Service接口
 * 
 * @author ChenRui98
 * @date 2023-03-22
 */
public interface ITaskManagerEntityService 
{
    /**
     * 查询任务管理
     * 
     * @param id 任务管理主键
     * @return 任务管理
     */
    public TaskManagerEntity selectTaskManagerEntityById(Long id);

    /**
     * 查询任务管理列表
     * 
     * @param taskManagerEntity 任务管理
     * @return 任务管理集合
     */
    public List<TaskManagerEntity> selectTaskManagerEntityList(TaskManagerEntity taskManagerEntity);

    /**
     * 新增任务管理
     * 
     * @param taskManagerEntity 任务管理
     * @return 结果
     */
    public int insertTaskManagerEntity(TaskManagerEntity taskManagerEntity);

    /**
     * 修改任务管理
     * 
     * @param taskManagerEntity 任务管理
     * @return 结果
     */
    public int updateTaskManagerEntity(TaskManagerEntity taskManagerEntity);

    /**
     * 批量删除任务管理
     * 
     * @param ids 需要删除的任务管理主键集合
     * @return 结果
     */
    public int deleteTaskManagerEntityByIds(Long[] ids);

    /**
     * 删除任务管理信息
     * 
     * @param id 任务管理主键
     * @return 结果
     */
    public int deleteTaskManagerEntityById(Long id);
}
