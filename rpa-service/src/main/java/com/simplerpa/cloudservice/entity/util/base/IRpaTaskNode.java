package com.simplerpa.cloudservice.entity.util.base;

import com.simplerpa.cloudservice.entity.util.RpaTaskOutput;

public interface IRpaTaskNode {
    public RpaTaskOutput run(RpaTaskOutput input) throws Exception;
    public RpaTaskOutput getRpaTaskOutput();
}
