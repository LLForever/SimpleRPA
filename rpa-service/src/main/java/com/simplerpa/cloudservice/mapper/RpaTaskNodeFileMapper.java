package com.simplerpa.cloudservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.simplerpa.cloudservice.entity.RpaTaskNodeFile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RpaTaskNodeFileMapper extends BaseMapper<RpaTaskNodeFile> {
    public String get_screenshot(@Param("taskid") Long taskid, @Param("nodeid") Long nodeid);
}
