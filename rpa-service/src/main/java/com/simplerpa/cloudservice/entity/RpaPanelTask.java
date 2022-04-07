package com.simplerpa.cloudservice.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @Description: TODO
 * @author: ChenRui98
 * @date: 2022年04月07日 21:26
 */

@Data
public class RpaPanelTask {
    Long taskId;
    Byte taskProgress;
    Boolean taskStatus;
    Long taskVersion;
    Long oldTaskVersion;
    JSONObject lineListJson;
    JSONObject nodeListJson;
    Integer userId;
}
