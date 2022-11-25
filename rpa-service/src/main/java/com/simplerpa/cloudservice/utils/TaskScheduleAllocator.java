package com.simplerpa.cloudservice.utils;

import com.alibaba.fastjson.JSONObject;
import com.simplerpa.cloudservice.controller.TaskScheduleController;
import com.simplerpa.cloudservice.entity.TaskDetail;
import com.simplerpa.cloudservice.entity.TaskLineDetail;
import com.simplerpa.cloudservice.entity.VO.TaskDetailVO;
import com.simplerpa.cloudservice.entity.util.DictionaryUtil;
import com.simplerpa.cloudservice.entity.util.library.tools.AiEnhanceTool;
import com.simplerpa.cloudservice.utils.gentic.Population;
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
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class TaskScheduleAllocator {
    private static final String SERVER_HOST = "http://192.168.103.";
    private static final String END_POINT = "12022";
    private static final String CMD_TAIL = "/panel-task/run_task", GET_COST_TAIL = "/schedule/get_cost";
    private static String[] serverList;
    private static final AtomicInteger pos = new AtomicInteger();
    private static final ArrayList<TaskDetailVO> waitingQueue;
    public static final ArrayList<String> machineName;
    public static final ArrayList<Double> opt_pso_list, pso_list, gene_list;

    private static String schedule_type;

    static {
        opt_pso_list = new ArrayList<>();
        pso_list = new ArrayList<>();
        gene_list = new ArrayList<>();

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
//        String msg = "*************************\n" + schedule_type + " send a task to : \n" + url + "\n" + vo.getTaskId() + " " + vo.getTaskName() + "\n" + "*************************";
//        System.out.println(msg);
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
//        System.out.println("*************RESULT****************");
//        System.out.println(res);
//        System.out.println("*************RESULT****************");
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

    private void LiziqunSchedule(ArrayList<TaskDetailVO> scheduleTaskList, JSONObject machinesCostInfo) throws Exception {
        Long[] ids = new Long[scheduleTaskList.size()];
        for (int i=0; i < scheduleTaskList.size(); i++){
            ids[i] = scheduleTaskList.get(i).getId();
        }
        double[] x_low = new double[ids.length], x_up = new double[ids.length];
        for(int i=0; i<ids.length; i++){
            x_low[i] = -4;
            x_up[i] = 4;
        }
        JSONObject performanceJSON = TaskScheduleController.getPerformanceJSON();
        JSONObject run;

        System.out.println(performanceJSON);
        System.out.println(machinesCostInfo);
        System.out.println(Arrays.toString(ids));

        long startTime = System.currentTimeMillis(), endTime;
        PSO pso = new PSO(x_low, x_up, machinesCostInfo, performanceJSON, ids, true);
        run = pso.run();
        endTime = System.currentTimeMillis();
        double[] best_sovs = run.getObject("best_sov", double[].class);
        System.out.println("******************************** Std PSO ********************************");
        System.out.println("time consume: " + (endTime-startTime));
        System.out.println(Arrays.toString(best_sovs));
        System.out.println(run.getDouble("best_fit"));
        System.out.println("******************************** END ********************************");

        if(ids.length > 1) {
            pso_list.add(run.getDouble("best_fit"));
        }

        startTime = System.currentTimeMillis();
        pso = new PSO(x_low, x_up, machinesCostInfo, performanceJSON, ids, false);
        run = pso.run();
        endTime = System.currentTimeMillis();
        best_sovs = run.getObject("best_sov", double[].class);
        System.out.println("******************************** Optimized PSO ********************************");
        System.out.println("time consume: " + (endTime-startTime));
        System.out.println(Arrays.toString(best_sovs));
        System.out.println(run.getDouble("best_fit"));
        System.out.println("******************************** END ********************************");

        if(ids.length > 1){
            opt_pso_list.add(run.getDouble("best_fit"));

            testAlgorithmCost(machinesCostInfo, performanceJSON, ids);

            System.out.println("********************** RESULT **********************");
            System.out.println("pso_list : " + pso_list);
            System.out.println(DictionaryUtil.getAvgByList(pso_list) + " " + DictionaryUtil.getStdDevByList(pso_list));
            System.out.println("optimized_pso : " + opt_pso_list);
            System.out.println(DictionaryUtil.getAvgByList(opt_pso_list) + " " + DictionaryUtil.getStdDevByList(opt_pso_list));
            System.out.println("gene: " + gene_list);
            System.out.println(DictionaryUtil.getAvgByList(gene_list) + " " + DictionaryUtil.getStdDevByList(gene_list));
            System.out.println("********************** END **********************");
        }

//        startTime = System.currentTimeMillis();
//        Population population = new Population(machinesCostInfo, performanceJSON, ids);
//        run = population.run(500);
//        endTime = System.currentTimeMillis();
//        best_sovs = run.getObject("best_sov", double[].class);
//        System.out.println("******************************** GENE ********************************");
//        System.out.println("time consume: " + (endTime-startTime));
//        System.out.println(Arrays.toString(best_sovs));
//        System.out.println(run.getDouble("best_fit"));
//        System.out.println("******************************** END ********************************");

//        for (int i=0; i < scheduleTaskList.size(); i++){
//            int machine_id = (int) Math.abs(best_sovs[i]);
//            if(machine_id > 3){
//                machine_id = 3;
//            }
//            SendTask(scheduleTaskList.get(i), machine_id);
//        }
    }

    private void testAlgorithmCost(JSONObject machinesCostInfo, JSONObject performanceJSON, Long[] ids){
        long startTime = System.currentTimeMillis(), endTime;
        Population population = new Population(machinesCostInfo, performanceJSON, ids);
        JSONObject run = population.run(500);
        endTime = System.currentTimeMillis();
        double[] best_sovs = run.getObject("best_sov", double[].class);
        System.out.println("******************************** GENE ********************************");
        System.out.println("time consume: " + (endTime-startTime));
        System.out.println(Arrays.toString(best_sovs));
        System.out.println(run.getDouble("best_fit"));
        System.out.println("******************************** END ********************************");

        try {
            startTime = System.currentTimeMillis();
            JSONObject aiResult = AiEnhanceTool.getAiResult(AiEnhanceTool.NSGA3, ids, machinesCostInfo, performanceJSON);
            endTime = System.currentTimeMillis();
            String[] split = aiResult.getString("res").split(" ");
            System.out.println("******************************** NSGA-III ********************************");
            System.out.println("time consume: " + (endTime-startTime));
            System.out.println(Arrays.toString(Arrays.copyOf(split, split.length - 2)));
            System.out.println(Double.parseDouble(split[split.length-1]) + Double.parseDouble(split[split.length-2]));
            System.out.println("******************************** END ********************************");

            if(ids.length > 1) {
                gene_list.add(Double.parseDouble(split[split.length-1]) + Double.parseDouble(split[split.length-2]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        double[] machine_list = new double[ids.length];
        for (int i=0; i<ids.length; i++){
            double machine_id = pos.get() % serverList.length;
            pos.incrementAndGet();
            machine_list[i] = machine_id;
        }
        double schedule = Schedule(machine_list, ids, machinesCostInfo, performanceJSON);
        System.out.println("******************************** LUNXUN ********************************");
        System.out.println(schedule);
        System.out.println("******************************** END ********************************");

        for (int z=0; z<ids.length; z++){
            Double minCost = 1e50;
            int min_i = 0;
            for (int i =0; i<machineName.size(); i++){
                String name = machineName.get(i);
                Double mCost = machinesCostInfo.getJSONObject(name).getDouble(TaskCostCountUtil.COST_VAL);
                if(minCost - mCost > 1e-10){
                    minCost = mCost;
                    min_i = i;
                }
            }
            String name = machineName.get(min_i);
            machinesCostInfo.getJSONObject(name).put(TaskCostCountUtil.COST_VAL, minCost + TaskCostCountUtil.getSumCostById(ids[z]));
            machine_list[z] = min_i;
        }

        schedule = Schedule(machine_list, ids, machinesCostInfo, performanceJSON);
        System.out.println("******************************** TANXIN ********************************");
        System.out.println(schedule);
        System.out.println("******************************** END ********************************");
    }

    private double Schedule(double[] var, Long[] ids, JSONObject machinesAllocateInfo, JSONObject machinePerformanceJSON){
        double[][] sumList = new double[4][3];
        double sum;
        for(int i=0; i<4; i++){
            for (int j=0; j<3; j++){
                sumList[i][j] = 0.0;
            }
        }
        double F = 1.0, G = 1.0, T = 0.0;
        for(int i=0; i<ids.length; i++){
            int targetMachine = (int) var[i];
            if(targetMachine > 3){
                targetMachine = 3;
            }
            List<Double> costListByTaskId = TaskCostCountUtil.getCostListByTaskId(ids[i]);
            sumList[targetMachine][0] += costListByTaskId.get(0); // cpu
            sumList[targetMachine][1] += costListByTaskId.get(1); // mem
            sumList[targetMachine][2] += costListByTaskId.get(2); // net
            T += costListByTaskId.get(3);
        }
        for(int i=0; i<4; i++){
            String machineName = TaskScheduleAllocator.machineName.get(i);
            double[] machineCostList = machinesAllocateInfo.getJSONObject(machineName).getObject(TaskCostCountUtil.LIST, double[].class);
            sumList[i][0] += machineCostList[0];
            sumList[i][1] += machineCostList[1];
            sumList[i][2] += machineCostList[2];
            T += machineCostList[3];
        }
        for(int i=0; i<4; i++){
            sumList[i][1] = TaskCostCountUtil.getMemCost(i, sumList[i][1]);
            double Nc, Nm, Nn;
            double Rc, Rm, Rn;
            Nc = machinePerformanceJSON.getJSONObject(TaskScheduleAllocator.machineName.get(i)).getDouble("cpu");
            Nm = machinePerformanceJSON.getJSONObject(TaskScheduleAllocator.machineName.get(i)).getDouble("mem");
            Nn = machinePerformanceJSON.getJSONObject(TaskScheduleAllocator.machineName.get(i)).getDouble("net");
            Rc = 100-Nc;
            Rm = 100-Nm;
            Rn = 100-Nn;
            sumList[i][0] = DictionaryUtil.checkValueAndChange(sumList[i][0]);
            sumList[i][1] = DictionaryUtil.checkValueAndChange(sumList[i][1]);
            sumList[i][2] = DictionaryUtil.checkValueAndChange(sumList[i][2]);
            F += Math.sqrt((sumList[i][0]/Rc)*(sumList[i][0]/Rc) + (sumList[i][1]/Rm)*(sumList[i][1]/Rm) + (sumList[i][2]/Rn)*(sumList[i][2]/Rn));
            G += Math.sqrt((sumList[i][0] + Nc)*(sumList[i][0] + Nc) + (sumList[i][1] + Nm)*(sumList[i][1] + Nm) + (sumList[i][2] + Nn)*(sumList[i][2] + Nn));
        }
        sum = DictionaryUtil.F_VAL*F + DictionaryUtil.G_VAL*G + DictionaryUtil.T_VAL*T;
        return sum;
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
            if(scheduleTaskList.isEmpty()){
                return;
            }
            JSONObject machinesCostInfo = getMachinesCostInfo();
            if("tanxin".equals(schedule_type)){
                TanXinSchedule(scheduleTaskList, machinesCostInfo);
            }else if("liziqun".equals(schedule_type)){
                LiziqunSchedule(scheduleTaskList, machinesCostInfo);
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
