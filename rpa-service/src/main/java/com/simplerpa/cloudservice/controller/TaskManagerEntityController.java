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
import com.simplerpa.cloudservice.entity.TaskManagerEntity;
import com.simplerpa.cloudservice.service.ITaskManagerEntityService;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.page.TableDataInfo;

/**
 * 任务管理Controller
 * 
 * @author ChenRui98
 * @date 2023-03-22
 */
@RestController
@RequestMapping("/TaskManagerEntity")
public class TaskManagerEntityController extends BaseController
{
    @Autowired
    private ITaskManagerEntityService taskManagerEntityService;

    /**
     * 查询任务管理列表
     */
    @RequiresPermissions("rpa:TaskManagerEntity:list")
    @GetMapping("/list")
    public TableDataInfo list(TaskManagerEntity taskManagerEntity)
    {
        startPage();
        List<TaskManagerEntity> list = taskManagerEntityService.selectTaskManagerEntityList(taskManagerEntity);
        return getDataTable(list);
    }

    /**
     * 导出任务管理列表
     */
    @RequiresPermissions("rpa:TaskManagerEntity:export")
    @Log(title = "任务管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TaskManagerEntity taskManagerEntity)
    {
        List<TaskManagerEntity> list = taskManagerEntityService.selectTaskManagerEntityList(taskManagerEntity);
        ExcelUtil<TaskManagerEntity> util = new ExcelUtil<TaskManagerEntity>(TaskManagerEntity.class);
        util.exportExcel(response, list, "任务管理数据");
    }

    /**
     * 获取任务管理详细信息
     */
    @RequiresPermissions("rpa:TaskManagerEntity:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(taskManagerEntityService.selectTaskManagerEntityById(id));
    }

    /**
     * 新增任务管理
     */
    @RequiresPermissions("rpa:TaskManagerEntity:add")
    @Log(title = "任务管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TaskManagerEntity taskManagerEntity)
    {
        return toAjax(taskManagerEntityService.insertTaskManagerEntity(taskManagerEntity));
    }

    /**
     * 修改任务管理
     */
    @RequiresPermissions("rpa:TaskManagerEntity:edit")
    @Log(title = "任务管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TaskManagerEntity taskManagerEntity)
    {
        return toAjax(taskManagerEntityService.updateTaskManagerEntity(taskManagerEntity));
    }

    /**
     * 删除任务管理
     */
    @RequiresPermissions("rpa:TaskManagerEntity:remove")
    @Log(title = "任务管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(taskManagerEntityService.deleteTaskManagerEntityByIds(ids));
    }
}
