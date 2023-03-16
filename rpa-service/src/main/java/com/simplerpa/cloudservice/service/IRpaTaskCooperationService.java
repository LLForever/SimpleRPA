package com.simplerpa.cloudservice.service;

import java.util.List;
import com.simplerpa.cloudservice.entity.RpaTaskCooperation;
import com.simplerpa.cloudservice.entity.TaskDetail;

/**
 * RPA任务协作Service接口
 * 
 * @author ChenRui98
 * @date 2023-03-15
 */
public interface IRpaTaskCooperationService 
{
    /**
     * 查询RPA任务协作
     * 
     * @param id RPA任务协作主键
     * @return RPA任务协作
     */
    public RpaTaskCooperation selectRpaTaskCooperationById(Long id);

    TaskDetail getRpaTaskDetailByTaskId(Long id);

    List<RpaTaskCooperation> getAllEnableRpaTaskCooperationTask();

    /**
     * 查询RPA任务协作列表
     * 
     * @param rpaTaskCooperation RPA任务协作
     * @return RPA任务协作集合
     */
    public List<RpaTaskCooperation> selectRpaTaskCooperationList(RpaTaskCooperation rpaTaskCooperation);

    /**
     * 新增RPA任务协作
     * 
     * @param rpaTaskCooperation RPA任务协作
     * @return 结果
     */
    public int insertRpaTaskCooperation(RpaTaskCooperation rpaTaskCooperation);

    /**
     * 修改RPA任务协作
     * 
     * @param rpaTaskCooperation RPA任务协作
     * @return 结果
     */
    public int updateRpaTaskCooperation(RpaTaskCooperation rpaTaskCooperation);

    /**
     * 批量删除RPA任务协作
     * 
     * @param ids 需要删除的RPA任务协作主键集合
     * @return 结果
     */
    public int deleteRpaTaskCooperationByIds(Long[] ids);

    /**
     * 删除RPA任务协作信息
     * 
     * @param id RPA任务协作主键
     * @return 结果
     */
    public int deleteRpaTaskCooperationById(Long id);
}
