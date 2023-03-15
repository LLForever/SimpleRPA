package com.simplerpa.cloudservice.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.simplerpa.cloudservice.mapper.RpaTaskScheduleMapper;
import com.simplerpa.cloudservice.entity.RpaTaskSchedule;
import com.simplerpa.cloudservice.service.IRpaTaskScheduleService;

/**
 * rpa任务计划详情Service业务层处理
 * 
 * @author ChenRui98
 * @date 2023-03-15
 */
@Service
public class RpaTaskScheduleServiceImpl implements IRpaTaskScheduleService 
{
    @Autowired
    private RpaTaskScheduleMapper rpaTaskScheduleMapper;

    /**
     * 查询rpa任务计划详情
     * 
     * @param id rpa任务计划详情主键
     * @return rpa任务计划详情
     */
    @Override
    public RpaTaskSchedule selectRpaTaskScheduleById(Long id)
    {
        return rpaTaskScheduleMapper.selectRpaTaskScheduleById(id);
    }

    /**
     * 查询rpa任务计划详情列表
     * 
     * @param rpaTaskSchedule rpa任务计划详情
     * @return rpa任务计划详情
     */
    @Override
    public List<RpaTaskSchedule> selectRpaTaskScheduleList(RpaTaskSchedule rpaTaskSchedule)
    {
        return rpaTaskScheduleMapper.selectRpaTaskScheduleList(rpaTaskSchedule);
    }

    /**
     * 新增rpa任务计划详情
     * 
     * @param rpaTaskSchedule rpa任务计划详情
     * @return 结果
     */
    @Override
    public int insertRpaTaskSchedule(RpaTaskSchedule rpaTaskSchedule)
    {
        return rpaTaskScheduleMapper.insertRpaTaskSchedule(rpaTaskSchedule);
    }

    /**
     * 修改rpa任务计划详情
     * 
     * @param rpaTaskSchedule rpa任务计划详情
     * @return 结果
     */
    @Override
    public int updateRpaTaskSchedule(RpaTaskSchedule rpaTaskSchedule)
    {
        return rpaTaskScheduleMapper.updateRpaTaskSchedule(rpaTaskSchedule);
    }

    /**
     * 批量删除rpa任务计划详情
     * 
     * @param ids 需要删除的rpa任务计划详情主键
     * @return 结果
     */
    @Override
    public int deleteRpaTaskScheduleByIds(Long[] ids)
    {
        return rpaTaskScheduleMapper.deleteRpaTaskScheduleByIds(ids);
    }

    /**
     * 删除rpa任务计划详情信息
     * 
     * @param id rpa任务计划详情主键
     * @return 结果
     */
    @Override
    public int deleteRpaTaskScheduleById(Long id)
    {
        return rpaTaskScheduleMapper.deleteRpaTaskScheduleById(id);
    }
}
