package com.simplerpa.cloudservice.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.system.api.model.LoginUser;
import com.simplerpa.cloudservice.entity.TaskDetail;
import com.simplerpa.cloudservice.entity.VO.TaskDetailVO;
import com.simplerpa.cloudservice.entity.util.DictionaryUtil;
import com.simplerpa.cloudservice.service.ITaskDetailService;
import com.simplerpa.cloudservice.utils.TaskScheduleAllocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Objects;

@RestController
@RequestMapping("/schedule")
public class TaskScheduleController extends BaseController {

    @Autowired
    ITaskDetailService taskDetailService;

    @GetMapping("/exec/{id}")
    public void startExecTask(@PathVariable("id") Long id){
        TaskDetail taskDetail = taskDetailService.selectTaskDetailById(id);
        TaskDetailVO taskDetailVO = new TaskDetailVO(taskDetail);
        LoginUser loginUser = SecurityUtils.getLoginUser();
        loginUser = new LoginUser();
        if (loginUser != null) {
            Long userid = loginUser.getUserid();
            userid = 1L;
            if (userid != null && Objects.equals(taskDetailVO.getUserId(), userid)) {
//                ThreadPoolSingleton.getInstance().submit(new RpaTaskExecutor(taskDetailVO));
                TaskScheduleAllocator allocator = new TaskScheduleAllocator();
                try {
                    allocator.AllocateTask(taskDetailVO);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @GetMapping("/get_perform")
    public JSONObject getPerformance(){
        JSONObject res = new JSONObject();
        // not completed
        JSONObject memPerform = getMEMPerform();
        JSONObject cpuPerform = getCPUPerform();
        JSONObject netioPerform = getNETIOPerform();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("mem", memPerform.get("master"));
        jsonObject.put("cpu", cpuPerform.get("master"));
        jsonObject.put("net", netioPerform.get("master"));
        res.put("master", jsonObject);

        jsonObject = new JSONObject();
        jsonObject.put("mem", memPerform.get("node1"));
        jsonObject.put("cpu", cpuPerform.get("node1"));
        jsonObject.put("net", netioPerform.get("node1"));
        res.put("node1", jsonObject);

        jsonObject = new JSONObject();
        jsonObject.put("mem", memPerform.get("node2"));
        jsonObject.put("cpu", cpuPerform.get("node2"));
        jsonObject.put("net", netioPerform.get("node2"));
        res.put("node2", jsonObject);

        jsonObject = new JSONObject();
        jsonObject.put("mem", memPerform.get("node3"));
        jsonObject.put("cpu", cpuPerform.get("node3"));
        jsonObject.put("net", netioPerform.get("node3"));
        res.put("node3", jsonObject);

        return res;
    }

    private JSONObject getCPUPerform(){
        JSONObject systemInfo = TaskScheduleAllocator.getSystemInfo(DictionaryUtil.CPU_URL, DictionaryUtil.CPU_URL_TAIL);
        JSONObject res = new JSONObject();
        JSONArray jsonArray = systemInfo.getJSONObject("data").getJSONArray("result");
        for (int i=0; i<jsonArray.size(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String instance = jsonObject.getJSONObject("metric").getString("instance");
            ArrayList<String> arrayList = (ArrayList<String>) jsonObject.get("value");
            double v = Double.parseDouble(arrayList.get(1));
            res.put(instance, v);
        }
        return res;
    }

    private JSONObject getMEMPerform(){
        JSONObject systemInfo = TaskScheduleAllocator.getSystemInfo(DictionaryUtil.MEM_URL);
        JSONObject res = new JSONObject();
        JSONArray jsonArray = systemInfo.getJSONObject("data").getJSONArray("result");
        for (int i=0; i<jsonArray.size(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String instance = jsonObject.getJSONObject("metric").getString("instance");
            String str = jsonObject.getJSONArray("value").get(1).toString();
            double v = Double.parseDouble(str);
            res.put(instance, v);
        }
        return res;
    }

    private JSONObject getNETIOPerform(){
        JSONObject res = new JSONObject();
        JSONObject master = TaskScheduleAllocator.getSystemInfo(DictionaryUtil.NET_IO, DictionaryUtil.GetNetIO("master"));
        JSONObject node1 = TaskScheduleAllocator.getSystemInfo(DictionaryUtil.NET_IO, DictionaryUtil.GetNetIO("node1"));
        JSONObject node2 = TaskScheduleAllocator.getSystemInfo(DictionaryUtil.NET_IO, DictionaryUtil.GetNetIO("node2"));
        JSONObject node3 = TaskScheduleAllocator.getSystemInfo(DictionaryUtil.NET_IO, DictionaryUtil.GetNetIO("node3"));
        res.put("master", getNetSpeed(master));
        res.put("node1", getNetSpeed(node1));
        res.put("node2", getNetSpeed(node2));
        res.put("node3", getNetSpeed(node3));
        return res;
    }

    private static Double getNetSpeed(JSONObject jsonObject){
        if(jsonObject == null){
            return 100.0;
        }
        Object o = jsonObject.getJSONObject("data").getJSONArray("result").getJSONObject(0).get("value");
        ArrayList<String> doubles = (ArrayList<String>) o;
        return Double.parseDouble(doubles.get(1))/1024;
    }
}