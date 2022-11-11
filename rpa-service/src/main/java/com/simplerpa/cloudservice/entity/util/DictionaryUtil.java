package com.simplerpa.cloudservice.entity.util;

import org.apache.commons.lang3.StringUtils;

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
    public static final String CURRENT_LAYER = "_@CURRENT_LAYER@";

//    public static final String AI_SERVER_URL = "http://192.168.103.99:10000/aienhance";
    public static final String AI_SERVER_URL = "http://127.0.0.1:10000/aienhance";

    public static final String CPU_URL = "http://192.168.103.116:30090/api/v1/query?query={json}";
    public static final String CPU_URL_TAIL = "100*(1 - sum by (instance)(increase(node_cpu_seconds_total{mode=\"idle\"}[30s])) / sum by (instance)(increase(node_cpu_seconds_total[30s])))";
    public static final String MEM_URL = "http://192.168.103.116:30090/api/v1/query?query=(node_memory_MemTotal_bytes-(node_memory_MemFree_bytes%2Bnode_memory_Cached_bytes%2Bnode_memory_Buffers_bytes))%2Fnode_memory_MemTotal_bytes*100";
    public static final String NET_IO = "http://192.168.103.116:30090/api/v1/query?query={json}";
    public static final String NET_IO_TAIL = "sum(rate (container_network_receive_bytes_total{node=\"{node_name}\"}[1m]))/1024";

    public static String GetNetIO(String nodeName){
        return StringUtils.replace(NET_IO_TAIL, "{node_name}", nodeName);
    }
}
