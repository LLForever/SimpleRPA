package com.simplerpa.cloudservice.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.simplerpa.cloudservice.mapper.RpaTaskLogMapper;
import com.simplerpa.cloudservice.entity.RpaTaskLog;
import com.simplerpa.cloudservice.service.IRpaTaskLogService;

/**
 * RPA日志表Service业务层处理
 * 
 * @author ChenRui98
 * @date 2023-03-16
 */
@Service
public class RpaTaskLogServiceImpl implements IRpaTaskLogService 
{
    @Autowired
    private RpaTaskLogMapper rpaTaskLogMapper;

    /**
     * 查询RPA日志表
     * 
     * @param id RPA日志表主键
     * @return RPA日志表
     */
    @Override
    public RpaTaskLog selectRpaTaskLogById(Long id)
    {
        return rpaTaskLogMapper.selectRpaTaskLogById(id);
    }

    /**
     * 查询RPA日志表列表
     * 
     * @param rpaTaskLog RPA日志表
     * @return RPA日志表
     */
    @Override
    public List<RpaTaskLog> selectRpaTaskLogList(RpaTaskLog rpaTaskLog)
    {
        return rpaTaskLogMapper.selectRpaTaskLogList(rpaTaskLog);
    }

    /**
     * 新增RPA日志表
     * 
     * @param rpaTaskLog RPA日志表
     * @return 结果
     */
    @Override
    public int insertRpaTaskLog(RpaTaskLog rpaTaskLog)
    {
        return rpaTaskLogMapper.insertRpaTaskLog(rpaTaskLog);
    }

    /**
     * 修改RPA日志表
     * 
     * @param rpaTaskLog RPA日志表
     * @return 结果
     */
    @Override
    public int updateRpaTaskLog(RpaTaskLog rpaTaskLog)
    {
        return rpaTaskLogMapper.updateRpaTaskLog(rpaTaskLog);
    }

    /**
     * 批量删除RPA日志表
     * 
     * @param ids 需要删除的RPA日志表主键
     * @return 结果
     */
    @Override
    public int deleteRpaTaskLogByIds(Long[] ids)
    {
        return rpaTaskLogMapper.deleteRpaTaskLogByIds(ids);
    }

    /**
     * 删除RPA日志表信息
     * 
     * @param id RPA日志表主键
     * @return 结果
     */
    @Override
    public int deleteRpaTaskLogById(Long id)
    {
        return rpaTaskLogMapper.deleteRpaTaskLogById(id);
    }
}
