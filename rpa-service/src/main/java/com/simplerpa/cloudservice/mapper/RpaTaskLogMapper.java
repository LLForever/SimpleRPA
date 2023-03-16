package com.simplerpa.cloudservice.mapper;

import java.util.List;
import com.simplerpa.cloudservice.entity.RpaTaskLog;

/**
 * RPA日志表Mapper接口
 * 
 * @author ChenRui98
 * @date 2023-03-16
 */
public interface RpaTaskLogMapper 
{
    /**
     * 查询RPA日志表
     * 
     * @param id RPA日志表主键
     * @return RPA日志表
     */
    public RpaTaskLog selectRpaTaskLogById(Long id);

    /**
     * 查询RPA日志表列表
     * 
     * @param rpaTaskLog RPA日志表
     * @return RPA日志表集合
     */
    public List<RpaTaskLog> selectRpaTaskLogList(RpaTaskLog rpaTaskLog);

    /**
     * 新增RPA日志表
     * 
     * @param rpaTaskLog RPA日志表
     * @return 结果
     */
    public int insertRpaTaskLog(RpaTaskLog rpaTaskLog);

    /**
     * 修改RPA日志表
     * 
     * @param rpaTaskLog RPA日志表
     * @return 结果
     */
    public int updateRpaTaskLog(RpaTaskLog rpaTaskLog);

    /**
     * 删除RPA日志表
     * 
     * @param id RPA日志表主键
     * @return 结果
     */
    public int deleteRpaTaskLogById(Long id);

    /**
     * 批量删除RPA日志表
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRpaTaskLogByIds(Long[] ids);
}
