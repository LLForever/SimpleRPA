package com.simplerpa.cloudservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.simplerpa.cloudservice.entity.RpaTaskNodeExecLogs;
import com.simplerpa.cloudservice.entity.VO.MostErrorInfoVO;
import com.simplerpa.cloudservice.entity.VO.RecentlyExecLogVO;
import com.simplerpa.cloudservice.entity.VO.SuccessRateVO;
import com.simplerpa.cloudservice.mapper.RpaTaskNodeExecLogsMapper;
import com.simplerpa.cloudservice.service.IRpaTaskNodeExecLogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class RpaTaskNodeExecLogsServiceImpl extends ServiceImpl<RpaTaskNodeExecLogsMapper, RpaTaskNodeExecLogs> implements IRpaTaskNodeExecLogsService {

    @Autowired
    RpaTaskNodeExecLogsMapper mapper;

    @Override
    public SuccessRateVO getSuccessRate(Long id) {
        return mapper.getSuccessRate(id);
    }

    @Override
    public ArrayList<MostErrorInfoVO> getMostErrorInfo(Long id) {
        return mapper.getMostErrorInfo(id);
    }

    @Override
    public RecentlyExecLogVO getRecentlyExecLog(Long id) {
        return mapper.getRecentlyExecLog(id);
    }
}
