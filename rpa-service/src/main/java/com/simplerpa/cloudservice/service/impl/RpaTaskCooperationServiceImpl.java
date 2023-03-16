package com.simplerpa.cloudservice.service.impl;

import java.util.List;

import com.simplerpa.cloudservice.entity.TaskDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.simplerpa.cloudservice.mapper.RpaTaskCooperationMapper;
import com.simplerpa.cloudservice.entity.RpaTaskCooperation;
import com.simplerpa.cloudservice.service.IRpaTaskCooperationService;

/**
 * RPA任务协作Service业务层处理
 * 
 * @author ChenRui98
 * @date 2023-03-15
 */
@Service
public class RpaTaskCooperationServiceImpl implements IRpaTaskCooperationService 
{
    @Autowired
    private RpaTaskCooperationMapper rpaTaskCooperationMapper;

    /**
     * 查询RPA任务协作
     * 
     * @param id RPA任务协作主键
     * @return RPA任务协作
     */
    @Override
    public RpaTaskCooperation selectRpaTaskCooperationById(Long id)
    {
        return rpaTaskCooperationMapper.selectRpaTaskCooperationById(id);
    }

    @Override
    public TaskDetail getRpaTaskDetailByTaskId(Long id) {
        return rpaTaskCooperationMapper.getRpaTaskDetailByTaskId(id);
    }

    @Override
    public List<RpaTaskCooperation> getAllEnableRpaTaskCooperationTask() {
        return rpaTaskCooperationMapper.getAllEnableRpaTaskCooperationTask();
    }

    /**
     * 查询RPA任务协作列表
     * 
     * @param rpaTaskCooperation RPA任务协作
     * @return RPA任务协作
     */
    @Override
    public List<RpaTaskCooperation> selectRpaTaskCooperationList(RpaTaskCooperation rpaTaskCooperation)
    {
        return rpaTaskCooperationMapper.selectRpaTaskCooperationList(rpaTaskCooperation);
    }

    /**
     * 新增RPA任务协作
     * 
     * @param rpaTaskCooperation RPA任务协作
     * @return 结果
     */
    @Override
    public int insertRpaTaskCooperation(RpaTaskCooperation rpaTaskCooperation)
    {
        return rpaTaskCooperationMapper.insertRpaTaskCooperation(rpaTaskCooperation);
    }

    /**
     * 修改RPA任务协作
     * 
     * @param rpaTaskCooperation RPA任务协作
     * @return 结果
     */
    @Override
    public int updateRpaTaskCooperation(RpaTaskCooperation rpaTaskCooperation)
    {
        return rpaTaskCooperationMapper.updateRpaTaskCooperation(rpaTaskCooperation);
    }

    /**
     * 批量删除RPA任务协作
     * 
     * @param ids 需要删除的RPA任务协作主键
     * @return 结果
     */
    @Override
    public int deleteRpaTaskCooperationByIds(Long[] ids)
    {
        return rpaTaskCooperationMapper.deleteRpaTaskCooperationByIds(ids);
    }

    /**
     * 删除RPA任务协作信息
     * 
     * @param id RPA任务协作主键
     * @return 结果
     */
    @Override
    public int deleteRpaTaskCooperationById(Long id)
    {
        return rpaTaskCooperationMapper.deleteRpaTaskCooperationById(id);
    }
}
