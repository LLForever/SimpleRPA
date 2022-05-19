package com.simplerpa.cloudservice.utils.factory;

import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;
import com.simplerpa.cloudservice.entity.util.library.node.EndNode;

public class EndNodeFactory implements RpaNodeFactory{
    @Override
    public IRpaTaskNode getInstance() throws Exception {
        return new EndNode();
    }
}
