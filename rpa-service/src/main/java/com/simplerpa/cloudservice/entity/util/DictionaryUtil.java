package com.simplerpa.cloudservice.entity.util;

/**
 * @Description: TODO
 * @author: ChenRui98
 * @date: 2022年04月21日 14:37
 */

public class DictionaryUtil {
    // 任务状态
    public static String TASK_STATUS_CREATED = "created";
    public static String TASK_STATUS_COMPLETED = "completed";
    public static String TASK_STATUS_WARNING = "warning";
    public static String TASK_STATUS_STOP = "stop";
    public static String TASK_STATUS_ERROR = "error";

    // 任务运行时反馈信息
    public static Integer TASK_MESSAGE_OK = 200;
    public static Integer TASK_MESSAGE_STOP = -999;
    public static Integer TASK_MESSAGE_RUN_ERROR = 999;
    public static Integer TASK_MESSAGE_EXPLAIN_ERROR = 998;

    // 协同编辑时的节点变化信息
    public static Integer NODE_MESSAGE_DELETE = 0;
    public static Integer NODE_MESSAGE_CHANGE = 1;
    public static Integer NODE_MESSAGE_ADD = 2;
}
