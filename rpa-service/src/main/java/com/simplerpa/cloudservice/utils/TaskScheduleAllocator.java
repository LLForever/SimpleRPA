package com.simplerpa.cloudservice.utils;

import com.alibaba.fastjson.JSONObject;
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
import java.util.concurrent.atomic.AtomicInteger;

public class TaskScheduleAllocator {
    String SERVER_HOST = "http://192.168.103.";
    String END_POINT = "12022";
    String CMD_TAIL = "/panel-task/run";
    String[] serverList;
    private static final AtomicInteger pos = new AtomicInteger();

    static {
        pos.set(0);
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

    private void LunXunAlgorithm(TaskDetailVO vo) throws Exception{
        int i = pos.get() % serverList.length;
        pos.incrementAndGet();
        String url = serverList[i] + CMD_TAIL;
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
    }

    public void AllocateTask(TaskDetailVO vo) throws Exception{
        LunXunAlgorithm(vo);
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

    public static JSONObject getSystemInfo(String url){
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
}
