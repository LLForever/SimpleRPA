package com.simplerpa.cloudservice.controller;

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
import com.ruoyi.common.security.annotation.RequiresPermissions;
import com.simplerpa.cloudservice.entity.RpaTaskLog;
import com.simplerpa.cloudservice.service.IRpaTaskLogService;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.page.TableDataInfo;

/**
 * RPA日志表Controller
 * 
 * @author ChenRui98
 * @date 2023-03-16
 */
@RestController
@RequestMapping("/RpaTaskLog")
public class RpaTaskLogController extends BaseController
{
    @Autowired
    private IRpaTaskLogService rpaTaskLogService;

    /**
     * 查询RPA日志表列表
     */
    @RequiresPermissions("rpa:RpaTaskLog:list")
    @GetMapping("/list")
    public TableDataInfo list(RpaTaskLog rpaTaskLog)
    {
        startPage();
        List<RpaTaskLog> list = rpaTaskLogService.selectRpaTaskLogList(rpaTaskLog);
        return getDataTable(list);
    }

    /**
     * 导出RPA日志表列表
     */
    @RequiresPermissions("rpa:RpaTaskLog:export")
    @Log(title = "RPA日志表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RpaTaskLog rpaTaskLog)
    {
        List<RpaTaskLog> list = rpaTaskLogService.selectRpaTaskLogList(rpaTaskLog);
        ExcelUtil<RpaTaskLog> util = new ExcelUtil<RpaTaskLog>(RpaTaskLog.class);
        util.exportExcel(response, list, "RPA日志表数据");
    }

    /**
     * 获取RPA日志表详细信息
     */
    @RequiresPermissions("rpa:RpaTaskLog:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(rpaTaskLogService.selectRpaTaskLogById(id));
    }

    /**
     * 新增RPA日志表
     */
    @RequiresPermissions("rpa:RpaTaskLog:add")
    @Log(title = "RPA日志表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RpaTaskLog rpaTaskLog)
    {
        return toAjax(rpaTaskLogService.insertRpaTaskLog(rpaTaskLog));
    }

    /**
     * 修改RPA日志表
     */
    @RequiresPermissions("rpa:RpaTaskLog:edit")
    @Log(title = "RPA日志表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RpaTaskLog rpaTaskLog)
    {
        return toAjax(rpaTaskLogService.updateRpaTaskLog(rpaTaskLog));
    }

    /**
     * 删除RPA日志表
     */
    @RequiresPermissions("rpa:RpaTaskLog:remove")
    @Log(title = "RPA日志表", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(rpaTaskLogService.deleteRpaTaskLogByIds(ids));
    }
}
