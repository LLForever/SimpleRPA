package com.simplerpa.cloudservice.entity.VO;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.utils.bean.BeanUtils;
import com.simplerpa.cloudservice.entity.TaskDetail;
import com.simplerpa.cloudservice.entity.TaskLineDetail;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;


/**
 * @Description: TODO
 * @author: ChenRui98
 * @date: 2022年04月07日 21:26
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class TaskDetailVO extends TaskDetail {
    private Long oldTaskVersion;
    private List<TaskLineDetail> lineList;
    private List<TaskNodeDetail> nodeList;

    public TaskDetailVO(TaskDetail taskDetail){
        BeanUtils.copyBeanProp(this, taskDetail);
        oldTaskVersion = this.getTaskVersion();
        if(StringUtils.isNotEmpty(this.getLineListJson())){
            setLineList(JSONObject.parseArray(this.getLineListJson(), TaskLineDetail.class));
        }
        if(StringUtils.isNotEmpty(this.getNodeListJson())) {
            setNodeList(JSONObject.parseArray(this.getNodeListJson(), TaskNodeDetail.class));
        }
    }

    public TaskDetailVO(){ super(); }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("taskId", getTaskId())
                .append("taskStatus", getTaskStatus())
                .append("taskProgress", getTaskProgress())
                .append("taskVersion", getTaskVersion())
                .append("lineListJson", getLineListJson())
                .append("nodeListJson", getNodeListJson())
                .append("userId", getUserId())
                .append("oldTaskVersion", getOldTaskVersion())
                .toString();
    }
}
