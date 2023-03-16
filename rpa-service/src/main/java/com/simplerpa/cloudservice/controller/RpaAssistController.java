package com.simplerpa.cloudservice.controller;

import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.web.page.TableDataInfo;
import com.simplerpa.cloudservice.entity.TaskDetail;
import com.simplerpa.cloudservice.entity.VO.MostErrorInfoVO;
import com.simplerpa.cloudservice.entity.VO.RecentlyExecLogVO;
import com.simplerpa.cloudservice.entity.VO.SuccessRateVO;
import com.simplerpa.cloudservice.service.IRpaTaskNodeExecLogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/assist")
public class RpaAssistController extends BaseController {
    @Autowired
    IRpaTaskNodeExecLogsService execLogsService;

    @Autowired
    TaskDetailController controller;

    @GetMapping("/rec_list")
    public TableDataInfo rec_list(){
        TaskDetail taskDetail = new TaskDetail();
        taskDetail.setTaskStatus("error");
        return controller.list(taskDetail);
    }

    @GetMapping("/query/{id}")
    public AjaxResult startExecTask(@PathVariable("id") Long id){
        SuccessRateVO successRate = execLogsService.getSuccessRate(id);
        return AjaxResult.success(successRate);
    }

    @GetMapping("/query_most_error/{id}")
    public AjaxResult query_most_error(@PathVariable("id") Long id){
        ArrayList<MostErrorInfoVO> mostErrorInfo = execLogsService.getMostErrorInfo(id);
        return AjaxResult.success(mostErrorInfo);
    }

    @GetMapping("/get_recently_exec_log/{id}")
    public AjaxResult getRecentlyExecLog(@PathVariable("id") Long id){
        RecentlyExecLogVO recentlyExecLog = execLogsService.getRecentlyExecLog(id);
        return AjaxResult.success(recentlyExecLog);
    }
}
