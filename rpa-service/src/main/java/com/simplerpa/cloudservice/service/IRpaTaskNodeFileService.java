package com.simplerpa.cloudservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.simplerpa.cloudservice.entity.RpaTaskNodeFile;

public interface IRpaTaskNodeFileService extends IService<RpaTaskNodeFile> {
    public String get_screenshot(Long taskid, Long nodeid);
}
