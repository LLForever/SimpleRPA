package com.simplerpa.cloudservice.entity;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Blob;

/**
 * @Description: TODO
 * @author: ChenRui98
 * @date: 2022年04月27日 17:19
 */

public class TaskNodeDetail {
    private String id;
    private String name;
    private String type;
    private String left;
    private String top;
    private String ico;
    private String state;
    private Long nodeVersion;
    private JSONObject params;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getNodeVersion() {
        return nodeVersion;
    }

    public void setNodeVersion(Long nodeVersion) {
        this.nodeVersion = nodeVersion;
    }

    public JSONObject getParams() {
        return params;
    }

    public void setParams(JSONObject params) {
        this.params = params;
    }
}
