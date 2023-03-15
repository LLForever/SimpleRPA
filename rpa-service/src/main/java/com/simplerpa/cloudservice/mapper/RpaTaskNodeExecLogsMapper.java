package com.simplerpa.cloudservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.simplerpa.cloudservice.entity.RpaTaskNodeExecLogs;
import com.simplerpa.cloudservice.entity.VO.MostErrorInfoVO;
import com.simplerpa.cloudservice.entity.VO.RecentlyExecLogVO;
import com.simplerpa.cloudservice.entity.VO.SuccessRateVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface RpaTaskNodeExecLogsMapper extends BaseMapper<RpaTaskNodeExecLogs> {
    SuccessRateVO getSuccessRate(Long id);
    ArrayList<MostErrorInfoVO> getMostErrorInfo(Long id);
    RecentlyExecLogVO getRecentlyExecLog(Long id);
}
