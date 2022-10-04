package com.simplerpa.cloudservice.utils.factory;

import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;
import com.simplerpa.cloudservice.utils.factory.dataproc.*;
import com.simplerpa.cloudservice.utils.factory.other.*;
import com.simplerpa.cloudservice.utils.factory.read.ReadCSVNodeFactory;
import com.simplerpa.cloudservice.utils.factory.read.ReadExcelNodeFactory;
import com.simplerpa.cloudservice.utils.factory.read.ReadTxtNodeFactory;
import com.simplerpa.cloudservice.utils.factory.webpage.*;

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

            // id: 3X
            case "random" : return new RandomNodeFactory(taskNodeDetail);
            case "add_text": return new AddTextNodeFactory(taskNodeDetail);
            case "text_length": return new TextLengthNodeFactory(taskNodeDetail);
            case "replace_text": return new ReplaceTextNodeFactory(taskNodeDetail);
            case "obj_to_text": return new ObjToTextNodeFactory(taskNodeDetail);

            // id: 6X
            case "open_page" : return new OpenWebPageNodeFactory(taskNodeDetail);
            case "single_click" : return new SingleClickNodeFactory(taskNodeDetail);
            case "double_click" : return new DoubleClickNodeFactory(taskNodeDetail);
            case "mouse_hover" : return new MouseHoverNodeFactory(taskNodeDetail);
            case "write_input" : return new WriteInputNodeFactory(taskNodeDetail);
            case "drag_element" : return new DragElementNodeFactory(taskNodeDetail);
            case "get_element" : return new GetElementNodeFactory(taskNodeDetail);
            case "jump_web" : return new JumpWebNodeFactory(taskNodeDetail);
            case "element_content_getter" : return new ElementContentGetterNodeFactory(taskNodeDetail);
            case "get_screenshot": return new GetScreenshotNodeFactory(taskNodeDetail);

            // id: 7X
            case "sleep" : return new SleepNodeFactory(taskNodeDetail);
            case "email_send" : return new EmailSendNodeFactory(taskNodeDetail); // not completed
            case "system_time" : return new SystemTimeNodeFactory(taskNodeDetail);
            case "move_time" : break;
            case "date_to_timestamp" : return new DateToTimestampNodeFactory(taskNodeDetail);
            case "timestamp_to_date" : return new TimestampToDateNodeFactory(taskNodeDetail);

        }
        return null;
    }
}