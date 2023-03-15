package com.simplerpa.cloudservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.simplerpa.cloudservice.entity.RpaTaskNodeExecLogs;
import com.simplerpa.cloudservice.entity.VO.MostErrorInfoVO;
import com.simplerpa.cloudservice.entity.VO.RecentlyExecLogVO;
import com.simplerpa.cloudservice.entity.VO.SuccessRateVO;
import com.simplerpa.cloudservice.mapper.RpaTaskNodeExecLogsMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

public interface IRpaTaskNodeExecLogsService extends IService<RpaTaskNodeExecLogs> {
    SuccessRateVO getSuccessRate(Long id);
    ArrayList<MostErrorInfoVO> getMostErrorInfo(Long id);
    RecentlyExecLogVO getRecentlyExecLog(Long id);
}
