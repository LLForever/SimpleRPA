package com.simplerpa.cloudservice.utils.factory;

import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;
import com.simplerpa.cloudservice.entity.util.library.node.StartNode;

/**
 * @Description: TODO
 * @author: ChenRui98
 * @date: 2022年05月01日 14:40
 */

public class StartNodeFactory implements RpaNodeFactory {
    @Override
    public IRpaTaskNode getInstance() {
        return new StartNode();
    }
}
