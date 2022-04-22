package com.simplerpa.cloudservice.entity.VO;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.utils.bean.BeanUtils;
import com.simplerpa.cloudservice.entity.TaskDetail;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @Description: TODO
 * @author: ChenRui98
 * @date: 2022年04月07日 21:26
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class TaskDetailVO extends TaskDetail {
    Long oldTaskVersion;

    public TaskDetailVO(TaskDetail taskDetail){
        BeanUtils.copyBeanProp(this, taskDetail);
        oldTaskVersion = this.getTaskVersion();
    }

    //public TaskDetailVO(){ super(); }

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
