package com.simplerpa.cloudservice.controller;

import com.ruoyi.common.core.utils.bean.BeanUtils;
import com.simplerpa.cloudservice.entity.TaskDetail;
import com.simplerpa.cloudservice.entity.VO.TaskDetailVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: TODO
 * @author: ChenRui98
 * @date: 2022年04月07日 21:34
 */

@RestController
@RequestMapping("/panel-task")
public class PanelTaskController {

    @PostMapping("/upload/detail")
    public String test(){
        return "test success";
    }
}
