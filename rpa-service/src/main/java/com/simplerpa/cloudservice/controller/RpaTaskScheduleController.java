package com.simplerpa.cloudservice.controller;

import java.util.Date;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
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
import com.simplerpa.cloudservice.entity.RpaTaskSchedule;
import com.simplerpa.cloudservice.service.IRpaTaskScheduleService;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.page.TableDataInfo;

/**
 * rpa任务计划详情Controller
 * 
 * @author ChenRui98
 * @date 2023-03-15
 */
@RestController
@RequestMapping("/RpaTaskSchedule")
public class RpaTaskScheduleController extends BaseController
{
    @Autowired
    private IRpaTaskScheduleService rpaTaskScheduleService;

    /**
     * 查询rpa任务计划详情列表
     */
    @GetMapping("/list")
    public TableDataInfo list(RpaTaskSchedule rpaTaskSchedule)
    {
        startPage();
        List<RpaTaskSchedule> list = rpaTaskScheduleService.selectRpaTaskScheduleList(rpaTaskSchedule);
        return getDataTable(list);
    }

    /**
     * 导出rpa任务计划详情列表
     */
    @Log(title = "rpa任务计划详情", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RpaTaskSchedule rpaTaskSchedule)
    {
        List<RpaTaskSchedule> list = rpaTaskScheduleService.selectRpaTaskScheduleList(rpaTaskSchedule);
        ExcelUtil<RpaTaskSchedule> util = new ExcelUtil<RpaTaskSchedule>(RpaTaskSchedule.class);
        util.exportExcel(response, list, "rpa任务计划详情数据");
    }

    /**
     * 获取rpa任务计划详情详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(rpaTaskScheduleService.selectRpaTaskScheduleById(id));
    }

    /**
     * 新增rpa任务计划详情
     */
    @Log(title = "rpa任务计划详情", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RpaTaskSchedule rpaTaskSchedule)
    {
        rpaTaskSchedule.setCreationTime(new Date());
        return toAjax(rpaTaskScheduleService.insertRpaTaskSchedule(rpaTaskSchedule));
    }

    /**
     * 修改rpa任务计划详情
     */
    @Log(title = "rpa任务计划详情", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RpaTaskSchedule rpaTaskSchedule)
    {
        return toAjax(rpaTaskScheduleService.updateRpaTaskSchedule(rpaTaskSchedule));
    }

    /**
     * 删除rpa任务计划详情
     */
    @Log(title = "rpa任务计划详情", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(rpaTaskScheduleService.deleteRpaTaskScheduleByIds(ids));
    }
}
