package com.simplerpa.cloudservice.utils.factory;

import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;
import com.simplerpa.cloudservice.entity.util.library.node.ReadExcelNode;

public interface RpaNodeFactory {
    public IRpaTaskNode getInstance() throws Exception;

    public static RpaNodeFactory getFactory(TaskNodeDetail taskNodeDetail){
        switch(taskNodeDetail.getType()){
            case "start" : return new StartNodeFactory();
            case "end" : return null;
            case "read_excel" : return new ReadExcelNodeFactory(taskNodeDetail);
            case "read_txt" : return new ReadTxtNodeFactory(taskNodeDetail);
        }
        return null;
    }
}