package com.simplerpa.cloudservice.controller;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.utils.bean.BeanUtils;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.system.api.model.LoginUser;
import com.simplerpa.cloudservice.entity.TaskDetail;
import com.simplerpa.cloudservice.entity.VO.TaskDetailVO;
import com.simplerpa.cloudservice.entity.util.DictionaryUtil;
import com.simplerpa.cloudservice.entity.util.RpaTaskStructure;
import com.simplerpa.cloudservice.mapper.TaskDetailMapper;
import com.simplerpa.cloudservice.service.ITaskDetailService;
import com.simplerpa.cloudservice.utils.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Objects;

/**
 * @Description: TODO
 * @author: ChenRui98
 * @date: 2022年04月07日 21:34
 */

@RestController
@RequestMapping("/panel-task")
public class PanelTaskController extends BaseController {

    @Autowired
    ITaskDetailService taskDetailService;

    @Autowired
    TaskDetailMapper mapper;

    @PostMapping("/upload/detail")
    public AjaxResult uploadTaskDetailAndStore(@RequestBody TaskDetailVO taskDetailVO){
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (StringUtils.isNotNull(loginUser)) {
            Long userid = loginUser.getUserid();
            if (userid != null) {
                TaskDetail detail = new TaskDetail();
                detail.setUserId(userid);
                detail.setTaskId(taskDetailVO.getTaskId());
                detail.setTaskName(taskDetailVO.getTaskName());
                detail.setTaskVersion(System.currentTimeMillis());
                detail.setLineListJson(JSONObject.toJSONString(taskDetailVO.getLineList()));
                detail.setNodeListJson(JSONObject.toJSONString(taskDetailVO.getNodeList()));
                detail.setTaskCreationTime(new Date());
                Boolean aBoolean = taskDetailService.uploadTaskDetail(detail);
                return aBoolean? AjaxResult.success("保存成功") : AjaxResult.error("保存失败！");
            }
        }
        return AjaxResult.error("保存出错！请检查登陆状态与您的任务信息是否正常！");
    }

    @PostMapping("/run")
    @Transactional
    public AjaxResult runTask(@RequestBody TaskDetailVO taskDetailVO){
        if(taskDetailVO.getTaskId() == null){
            return AjaxResult.error("该任务的ID为空，无法运行！");
        }
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (StringUtils.isNotNull(loginUser)) {
            Long userid = loginUser.getUserid();
            if (userid != null && Objects.equals(taskDetailVO.getUserId(), userid)) {
                uploadTaskDetailAndStore(taskDetailVO);
                ThreadPoolSingleton.getInstance().submit(new RpaTaskExecutor(taskDetailVO));
                return AjaxResult.success("任务启动成功！正在运行...");
            }else{
                return AjaxResult.error("任务启动失败！您不是该任务的创建者！");
            }
        }
        return AjaxResult.error("运行出错！请检查登陆状态是否正常！");
    }

    @PostMapping("/run_task")
    @Transactional
    public AjaxResult run_task(@RequestBody TaskDetailVO taskDetailVO){
        if(taskDetailVO.getTaskId() == null){
            return AjaxResult.error("该任务的ID为空，无法运行！");
        }
        if (taskDetailVO.getTaskProgress() - 999 > 0) {
            int node = (int) taskDetailVO.getTaskProgress().doubleValue();
            node -= 1000;

            TaskDetail detail = new TaskDetail();
            detail.setId(taskDetailVO.getId());
//            detail.setTaskId(taskDetailVO.getTaskId());
//            detail.setLineListJson(JSONObject.toJSONString(taskDetailVO.getLineList()));
//            detail.setNodeListJson(JSONObject.toJSONString(taskDetailVO.getNodeList()));
            detail.setTaskCreationTime(new Date());
            detail.setParams(null);
            mapper.updateById(detail);

            TaskQueueAllocator.setNode(node);
//            ThreadPoolSingleton.getInstance().submit(new RpaTaskExecutor(taskDetailVO));
            TaskQueueAllocator.pushElement(taskDetailVO);
            return AjaxResult.success("任务启动成功！正在运行...");
        }
        return AjaxResult.error("运行出错！请检查登陆状态是否正常！");
    }

    @GetMapping("/getrm")
    public JSONObject getrm(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("isRunning", TaskQueueAllocator.isIsRunning());
        jsonObject.put("queue: ", TaskQueueAllocator.getQSize());
        jsonObject.put("rm", TaskCostCountUtil.getRm());
        return jsonObject;
    }
}
