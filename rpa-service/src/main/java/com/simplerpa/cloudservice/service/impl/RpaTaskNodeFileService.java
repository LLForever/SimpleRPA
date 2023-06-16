package com.simplerpa.cloudservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.simplerpa.cloudservice.entity.RpaTaskNodeFile;
import com.simplerpa.cloudservice.mapper.RpaTaskNodeFileMapper;
import com.simplerpa.cloudservice.service.IRpaTaskNodeFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RpaTaskNodeFileService extends ServiceImpl<RpaTaskNodeFileMapper, RpaTaskNodeFile> implements IRpaTaskNodeFileService {
    @Autowired
    RpaTaskNodeFileMapper mapper;

    @Override
    public String get_screenshot(Long taskid, Long nodeid) {
        return mapper.get_screenshot(taskid, nodeid);
    }
}
