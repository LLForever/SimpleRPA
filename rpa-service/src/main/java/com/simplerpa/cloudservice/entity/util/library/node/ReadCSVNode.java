package com.simplerpa.cloudservice.entity.util.library.node;

import com.simplerpa.cloudservice.entity.util.RpaTaskOutput;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;

public class ReadCSVNode implements IRpaTaskNode {
    private byte[] file; // 文件
    private String fileName;
    private RpaTaskOutput output; // 解析后的数据
    private String outputParamName; // 数据参数名称(用户自定义的output名称)

    @Override
    public RpaTaskOutput run(RpaTaskOutput input) throws Exception {
        return null;
    }
}
