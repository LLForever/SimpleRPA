package com.simplerpa.cloudservice.utils;

import com.alibaba.fastjson.JSONObject;
import com.simplerpa.cloudservice.controller.TaskScheduleController;
import com.simplerpa.cloudservice.entity.TaskLineDetail;
import com.simplerpa.cloudservice.entity.VO.TaskDetailVO;
import com.simplerpa.cloudservice.entity.util.DictionaryUtil;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskScheduleAllocator {
    private static final String SERVER_HOST = "http://192.168.103.";
    private static final String END_POINT = "12022";
    private static final String CMD_TAIL = "/panel-task/run_task", GET_COST_TAIL = "/schedule/get_cost";
    private static String[] serverList;
    private static final AtomicInteger pos = new AtomicInteger();
    private static final ArrayList<TaskDetailVO> waitingQueue;
    private static final ArrayList<String> machineName;

    private static String schedule_type;

    static {
        waitingQueue = new ArrayList<>();
        pos.set(0);
        machineName = new ArrayList<>(Arrays.asList("master", "node1", "node2", "node3"));
    }

    public void initParams(Integer num, String[] ips){
        serverList = new String[num];
        for(int i=0; i<num; i++){
            serverList[i] = SERVER_HOST + ips[i] + ":" + END_POINT;
        }
    }

    public TaskScheduleAllocator(){
        String[] ips = {"116", "117", "118", "99"};
        initParams(ips.length, ips);
    }

    private void SendTask(TaskDetailVO vo, int i) throws Exception{
        String url = serverList[i] + CMD_TAIL;
        vo.setTaskProgress(999.0);
        String msg = "*************************\n" + schedule_type + " send a task to : \n" + url + "\n" + vo.getTaskId() + " " + vo.getTaskName() + "\n" + "*************************";
        System.out.println(msg);
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(vo);
        StringEntity entity = new StringEntity(jsonObject.toJSONString(), "UTF-8");
        entity.setContentType("application/form-data");
        httpPost.setEntity(entity);

        CloseableHttpResponse execute = client.execute(httpPost);
        HttpEntity httpEntity = execute.getEntity();
        String res = EntityUtils.toString(httpEntity);
        client.close();
        System.out.println("*************RESULT****************");
        System.out.println(res);
        System.out.println("*************RESULT****************");
    }

    private void LunXunSchedule(ArrayList<TaskDetailVO> list) throws Exception {
        for (TaskDetailVO item : list){
            int i = pos.get() % serverList.length;
            pos.incrementAndGet();
            SendTask(item, i);
        }
    }

    private void TanXinSchedule(ArrayList<TaskDetailVO> list, JSONObject costInfo) throws Exception {
        for (TaskDetailVO item : list){
            Double minCost = 1e50;
            int min_i = 0;
            for (int i =0; i<machineName.size(); i++){
                String name = machineName.get(i);
                Double mCost = costInfo.getJSONObject(name).getDouble(TaskCostCountUtil.COST_VAL);
                if(minCost - mCost > 1e-10){
                    minCost = mCost;
                    min_i = i;
                }
            }
            SendTask(item, min_i);
            String name = machineName.get(min_i);
            costInfo.getJSONObject(name).put(TaskCostCountUtil.COST_VAL, minCost + TaskCostCountUtil.getSumCostById(item.getId()));
        }
    }

    public void AllocateTask(TaskDetailVO vo) throws Exception{
        ArrayList<TaskDetailVO> scheduleTaskList;
        synchronized (waitingQueue){
            waitingQueue.add(vo);
        }
        synchronized (TaskScheduleAllocator.class){
            synchronized (waitingQueue){
                scheduleTaskList = new ArrayList<>(waitingQueue);
                waitingQueue.clear();
            }
            JSONObject performanceJSON = TaskScheduleController.getPerformanceJSON();
            JSONObject machinesCostInfo = getMachinesCostInfo();
            if("tanxin".equals(schedule_type)){
                TanXinSchedule(scheduleTaskList, machinesCostInfo);
            }else if("liziqun".equals(schedule_type)){

            }else{
                LunXunSchedule(scheduleTaskList);
            }
        }
    }

    public static JSONObject getSystemInfo(String promQL, String tail) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            return restTemplate.getForObject(promQL, JSONObject.class, tail);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject sendGetRequest(String url){
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Content-Type", "application/json;charset=UTF-8");
        try {
            CloseableHttpResponse execute = client.execute(httpGet);
            HttpEntity httpEntity = execute.getEntity();
            String s = EntityUtils.toString(httpEntity);
            JSONObject json = JSONObject.parseObject(s);
            client.close();
            return json;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private JSONObject getMachinesCostInfo(){
        JSONObject res = new JSONObject();
        for(int i=0; i<serverList.length; i++){
            String url = serverList[i] + GET_COST_TAIL;
            JSONObject jsonObject = sendGetRequest(url);
            res.put(machineName.get(i), jsonObject);
        }
        return res;
    }

    public static String getSchedule_type() {
        return schedule_type;
    }

    public static void setSchedule_type(String schedule_type) {
        TaskScheduleAllocator.schedule_type = schedule_type;
    }
}
