package com.simplerpa.cloudservice.utils.factory.other;

import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;
import com.simplerpa.cloudservice.entity.util.library.node.other.EndNode;
import com.simplerpa.cloudservice.utils.factory.RpaNodeFactory;

public class EndNodeFactory implements RpaNodeFactory {
    @Override
    public IRpaTaskNode getInstance() throws Exception {
        return new EndNode();
    }
}
