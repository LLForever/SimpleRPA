package com.simplerpa.cloudservice.service;

import java.util.List;
import com.simplerpa.cloudservice.entity.RpaTaskSchedule;

/**
 * rpa任务计划详情Service接口
 * 
 * @author ChenRui98
 * @date 2023-03-15
 */
public interface IRpaTaskScheduleService 
{
    /**
     * 查询rpa任务计划详情
     * 
     * @param id rpa任务计划详情主键
     * @return rpa任务计划详情
     */
    public RpaTaskSchedule selectRpaTaskScheduleById(Long id);

    /**
     * 查询rpa任务计划详情列表
     * 
     * @param rpaTaskSchedule rpa任务计划详情
     * @return rpa任务计划详情集合
     */
    public List<RpaTaskSchedule> selectRpaTaskScheduleList(RpaTaskSchedule rpaTaskSchedule);

    /**
     * 新增rpa任务计划详情
     * 
     * @param rpaTaskSchedule rpa任务计划详情
     * @return 结果
     */
    public int insertRpaTaskSchedule(RpaTaskSchedule rpaTaskSchedule);

    /**
     * 修改rpa任务计划详情
     * 
     * @param rpaTaskSchedule rpa任务计划详情
     * @return 结果
     */
    public int updateRpaTaskSchedule(RpaTaskSchedule rpaTaskSchedule);

    /**
     * 批量删除rpa任务计划详情
     * 
     * @param ids 需要删除的rpa任务计划详情主键集合
     * @return 结果
     */
    public int deleteRpaTaskScheduleByIds(Long[] ids);

    /**
     * 删除rpa任务计划详情信息
     * 
     * @param id rpa任务计划详情主键
     * @return 结果
     */
    public int deleteRpaTaskScheduleById(Long id);
}
