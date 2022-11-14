package com.simplerpa.cloudservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.simplerpa.cloudservice.entity.RpaTaskNodeExecLogs;
import com.simplerpa.cloudservice.mapper.RpaTaskNodeExecLogsMapper;
import com.simplerpa.cloudservice.service.IRpaTaskNodeExecLogsService;
import org.springframework.stereotype.Service;

@Service
public class RpaTaskNodeExecLogsServiceImpl extends ServiceImpl<RpaTaskNodeExecLogsMapper, RpaTaskNodeExecLogs> implements IRpaTaskNodeExecLogsService {
}
