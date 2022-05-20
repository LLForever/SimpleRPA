package com.simplerpa.cloudservice.utils.factory;

import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;
import com.simplerpa.cloudservice.entity.util.library.node.ReadExcelNode;

public interface RpaNodeFactory {
    public IRpaTaskNode getInstance() throws Exception;

    public static RpaNodeFactory getFactory(TaskNodeDetail taskNodeDetail){
        switch(taskNodeDetail.getType()){
            case "start" : return new StartNodeFactory();
            case "end" : return new EndNodeFactory();

            // id: 2X
            case "read_excel" : return new ReadExcelNodeFactory(taskNodeDetail);
            case "read_txt" : return new ReadTxtNodeFactory(taskNodeDetail);
            case "read_csv" : return new ReadCSVNodeFactory(taskNodeDetail);

            // id: 7X
            case "sleep" : return new SleepNodeFactory(taskNodeDetail);
            case "email_send" : return new EmailSendNodeFactory(taskNodeDetail); // not completed
            case "system_time" : return new SystemTimeNodeFactory(taskNodeDetail);
            case "move_time" : break;
            case "date_to_timestamp" : break;
            case "timestamp_to_date" : break;

        }
        return null;
    }
}