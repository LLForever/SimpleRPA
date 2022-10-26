package com.simplerpa.cloudservice.entity.util;

/**
 * @Description: TODO
 * @author: ChenRui98
 * @date: 2022年04月21日 14:37
 */

public class DictionaryUtil {
    // 任务状态
    public static final String TASK_STATUS_CREATED = "created";
    public static final String TASK_STATUS_COMPLETED = "completed";
    public static final String TASK_STATUS_WARNING = "warning";
    public static final String TASK_STATUS_STOP = "stop";
    public static final String TASK_STATUS_ERROR = "error";
    public static final String TASK_STATUS_RUNNING = "running";

    public static Boolean taskStatusAvailable(String status){
        if(TASK_STATUS_CREATED.equals(status)){
            return true;
        }
        if(TASK_STATUS_COMPLETED.equals(status)){
            return true;
        }
        if(TASK_STATUS_WARNING.equals(status)){
            return true;
        }
        if(TASK_STATUS_STOP.equals(status)){
            return true;
        }
        if(TASK_STATUS_RUNNING.equals(status)){
            return true;
        }
        return TASK_STATUS_ERROR.equals(status);
    }

    // 连接建立成功时的信息
    public static final Integer CLIENT_CONNECTED = 102;

    // 任务运行时反馈信息
    public static final Integer TASK_MESSAGE_OK = 200;
    public static final Integer TASK_UPDATE_SCREENSHOT = 201;
    public static final Integer TASK_MESSAGE_STOP = -999;
    public static final Integer TASK_MESSAGE_RUN_ERROR = 999;
    public static final Integer TASK_MESSAGE_EXPLAIN_ERROR = 998;

    // 协同编辑时的节点变化信息
    public static final Integer NODE_MESSAGE_DELETE = 0;
    public static final Integer NODE_MESSAGE_CHANGE = 1;
    public static final Integer NODE_MESSAGE_ADD = 2;

    // 独立参数信息
    public static final String SINGLE_PARAM_FLAG = "_@SINGLE_PARAM_FLAG@";
    public static final String HTML_FLAG = "_@HTML_FLAG@";
    public static final String RPA_TASK_ID = "_@RPA_TASK_ID@";
    public static final String NO_MERGE_FLAG = "_@NO_MERGE_FLAG@";

    public static final String AI_SERVER_URL = "http://192.168.103.99:10000/aienhance";
}
