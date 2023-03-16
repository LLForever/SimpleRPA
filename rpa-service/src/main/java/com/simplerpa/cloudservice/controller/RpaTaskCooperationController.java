package com.simplerpa.cloudservice.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.system.api.model.LoginUser;
import com.simplerpa.cloudservice.entity.TaskDetail;
import com.simplerpa.cloudservice.entity.VO.TaskDetailVO;
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
import com.simplerpa.cloudservice.entity.RpaTaskCooperation;
import com.simplerpa.cloudservice.service.IRpaTaskCooperationService;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.page.TableDataInfo;

/**
 * RPA任务协作Controller
 * 
 * @author ChenRui98
 * @date 2023-03-15
 */
@RestController
@RequestMapping("/RpaTaskCooperation")
public class RpaTaskCooperationController extends BaseController
{
    @Autowired
    private IRpaTaskCooperationService rpaTaskCooperationService;

    /**
     * 查询RPA任务协作列表
     */
    @GetMapping("/list")
    public TableDataInfo list(RpaTaskCooperation rpaTaskCooperation)
    {
        startPage();
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (StringUtils.isNotNull(loginUser)) {
            Long userid = loginUser.getUserid();
            if(userid != null){
                rpaTaskCooperation.setUserId(userid);
                List<RpaTaskCooperation> list = rpaTaskCooperationService.selectRpaTaskCooperationList(rpaTaskCooperation);
                return getDataTable(list);
            }
        }
        return getDataTable(null);
    }

    @GetMapping("/enable_list")
    public TableDataInfo enable_list()
    {
        startPage();
        List<RpaTaskCooperation> list = rpaTaskCooperationService.getAllEnableRpaTaskCooperationTask();
        return getDataTable(list);
    }

    @GetMapping("/getTaskDetail/{id}")
    public TaskDetailVO getTaskDetail(@PathVariable("id") Long id)
    {
        TaskDetail rpaTaskDetailByTaskId = rpaTaskCooperationService.getRpaTaskDetailByTaskId(id);
        return new TaskDetailVO(rpaTaskDetailByTaskId);
    }

    /**
     * 导出RPA任务协作列表
     */
    @Log(title = "RPA任务协作", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RpaTaskCooperation rpaTaskCooperation)
    {
        List<RpaTaskCooperation> list = rpaTaskCooperationService.selectRpaTaskCooperationList(rpaTaskCooperation);
        ExcelUtil<RpaTaskCooperation> util = new ExcelUtil<RpaTaskCooperation>(RpaTaskCooperation.class);
        util.exportExcel(response, list, "RPA任务协作数据");
    }

    /**
     * 获取RPA任务协作详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(rpaTaskCooperationService.selectRpaTaskCooperationById(id));
    }

    /**
     * 新增RPA任务协作
     */
    @Log(title = "RPA任务协作", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RpaTaskCooperation rpaTaskCooperation)
    {
        if(rpaTaskCooperation.getTaskId() == null){
            return AjaxResult.error("缺少TaskId");
        }
        TaskDetail rpaTaskDetailByTaskId = rpaTaskCooperationService.getRpaTaskDetailByTaskId(rpaTaskCooperation.getTaskId());
        if(rpaTaskDetailByTaskId == null){
            return AjaxResult.error("任务ID出错！找不到任务");
        }
        rpaTaskCooperation = new RpaTaskCooperation();
        rpaTaskCooperation.setTaskId(rpaTaskDetailByTaskId.getTaskId());
        rpaTaskCooperation.setTaskName(rpaTaskDetailByTaskId.getTaskName());
        rpaTaskCooperation.setImportTime(new Date());
        rpaTaskCooperation.setUserId(rpaTaskDetailByTaskId.getUserId());
        return toAjax(rpaTaskCooperationService.insertRpaTaskCooperation(rpaTaskCooperation));
    }

    /**
     * 修改RPA任务协作
     */
    @Log(title = "RPA任务协作", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RpaTaskCooperation rpaTaskCooperation)
    {
        return toAjax(rpaTaskCooperationService.updateRpaTaskCooperation(rpaTaskCooperation));
    }

    @PostMapping("/change_status")
    public AjaxResult change_status(@RequestBody RpaTaskCooperation rpaTaskCooperation){
        if(rpaTaskCooperation.getCoEnable() == 1){
            rpaTaskCooperation.setCoEnable(0);
        }else{
            rpaTaskCooperation.setCoEnable(1);
        }
        int i = rpaTaskCooperationService.updateRpaTaskCooperation(rpaTaskCooperation);
        if(i < 1){
            return AjaxResult.error("更新失败！");
        }
        return AjaxResult.success("更新成功");
    }

    /**
     * 删除RPA任务协作
     */
    @Log(title = "RPA任务协作", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(rpaTaskCooperationService.deleteRpaTaskCooperationByIds(ids));
    }
}
