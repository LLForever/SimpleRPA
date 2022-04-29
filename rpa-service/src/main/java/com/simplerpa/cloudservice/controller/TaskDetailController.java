package com.simplerpa.cloudservice.controller;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.system.api.model.LoginUser;
import com.simplerpa.cloudservice.entity.VO.TaskDetailVO;
import com.simplerpa.cloudservice.entity.util.DictionaryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import com.simplerpa.cloudservice.entity.TaskDetail;
import com.simplerpa.cloudservice.service.ITaskDetailService;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.page.TableDataInfo;

/**
 * rpa面板任务详情Controller
 * 
 * @author simpleRPA
 * @date 2022-04-20
 */
@RestController
@RequestMapping("/TaskDetail")
public class TaskDetailController extends BaseController
{
    @Autowired
    private ITaskDetailService taskDetailService;

    /**
     * 查询rpa面板任务详情列表
     */
    //@RequiresPermissions("rpa:TaskDetail:list")
    @GetMapping("/list")
    public TableDataInfo list(TaskDetail taskDetail)
    {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (StringUtils.isNotNull(loginUser)){
            Long userid = loginUser.getUserid();
            if(userid != null){
                startPage();
                taskDetail.setUserId(userid);
                List<TaskDetail> originList = taskDetailService.selectTaskDetailList(taskDetail);
                List<TaskDetailVO> list = new ArrayList<>();
                for(TaskDetail detail : originList){
                    list.add(new TaskDetailVO(detail));
                }
                return getDataTable(list);
            }
        }
        return getDataTable(new ArrayList<TaskDetail>());
    }

    /**
     * 导出rpa面板任务详情列表
     */
//    @RequiresPermissions("rpa:TaskDetail:export")
    @Log(title = "rpa面板任务详情", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TaskDetail taskDetail)
    {
        List<TaskDetail> list = taskDetailService.selectTaskDetailList(taskDetail);
        ExcelUtil<TaskDetail> util = new ExcelUtil<TaskDetail>(TaskDetail.class);
        util.exportExcel(response, list, "rpa面板任务详情数据");
    }

    /**
     * 获取rpa面板任务详情详细信息
     */
//    @RequiresPermissions("rpa:TaskDetail:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(taskDetailService.selectTaskDetailById(id));
    }

    /**
     * 新增rpa面板任务详情
     */
//    @RequiresPermissions("rpa:TaskDetail:add")
    @Log(title = "rpa面板任务详情", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TaskDetail taskDetail)
    {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (StringUtils.isNotNull(loginUser)){
            Long userid = loginUser.getUserid();
            if(userid != null){
                taskDetail.setTaskId((userid<<32) + (System.currentTimeMillis()/1000));
                taskDetail.setTaskVersion(System.currentTimeMillis());
                taskDetail.setUserId(userid);
                taskDetail.setTaskStatus(DictionaryUtil.TASK_STATUS_CREATED);
                return toAjax(taskDetailService.insertTaskDetail(taskDetail));
            }
        }
        return toAjax(false);
    }

    /**
     * 修改rpa面板任务详情
     */
//    @RequiresPermissions("rpa:TaskDetail:edit")
    @Log(title = "rpa面板任务详情", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TaskDetail taskDetail)
    {
        return toAjax(taskDetailService.updateTaskDetail(taskDetail));
    }

    /**
     * 删除rpa面板任务详情
     */
//    @RequiresPermissions("rpa:TaskDetail:remove")
    @Log(title = "rpa面板任务详情", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(taskDetailService.deleteTaskDetailByIds(ids));
    }
}
