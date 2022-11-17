package com.simplerpa.cloudservice.paperTest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.simplerpa.cloudservice.entity.util.library.tools.AiEnhanceTool;
import com.simplerpa.cloudservice.utils.gentic.Population;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class JustInTimeMain {

    public static void main(String[] args) {
        try {
//            testAiEnhanceTools();

//            generateCsvFiles();
            testGen();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void testGen(){
//        Population population = new Population(new JSONObject(), new JSONObject(), new Long[1]);
//        for(int i=0;i<500;i++){
//            population.reproduction();
//
//            System.out.println("第"+(i+1)+"代最优解:"+ population.getTopIndividual().calcFitness());
//        }
    }

    /**
     * not available
     * */
    public static void testAiEnhanceTools() throws Exception {
        String url = "https://img1.baidu.com/it/u=3704169818,578020351&fm=253&fmt=auto&app=138&f=JPEG?w=640&h=450";

//            String url = "https://s1.ax1x.com/2022/11/02/xHvKfJ.png";
//            String fapiaoUrl = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fss2.meipian.me%2Fusers%2F18784074%2F6ac8639a5f21443493b4aa0685450458.jpg%3Fmeipian-raw%2Fbucket%2Fivwen%2Fkey%2FdXNlcnMvMTg3ODQwNzQvNmFjODYzOWE1ZjIxNDQzNDkzYjRhYTA2ODU0NTA0NTguanBn%2Fsign%2Fe68651a55681b23ccb968ce32f4eb155.jpg&refer=http%3A%2F%2Fss2.meipian.me&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1670052461&t=94ea15524e5ae091b5806866eac8169d";

        String[] strArr = {"姓名", "证号", "电话", "邮箱"};
        List<String> list = Arrays.asList(strArr);
        JSONObject ocrResult = AiEnhanceTool.getAiResult(url, AiEnhanceTool.KEY_EXT, list);
        System.out.println(JSONObject.toJSONString(ocrResult));
        getAttributeValue(ocrResult.getJSONArray("res"));
        outputRes(ocrResult);
    }

    public static void generateCsvFiles() throws Exception {
        File cpufile = new File("C:\\Users\\TUF\\Desktop\\新建文件夹\\cpu.json");
        File memfile = new File("C:\\Users\\TUF\\Desktop\\新建文件夹\\mem.json");
        File netfile = new File("C:\\Users\\TUF\\Desktop\\新建文件夹\\network.json");
        HashMap<String, ArrayList<Double>> cpu = getHash(cpufile, "instance");
        HashMap<String, ArrayList<Double>> mem = getHash(memfile, "instance");
        HashMap<String, ArrayList<Double>> net = getHash(netfile, "node");
        writeFile(cpu, "C:\\Users\\TUF\\Desktop\\新建文件夹\\cpu_TANXIN.csv");
        writeFile(mem, "C:\\Users\\TUF\\Desktop\\新建文件夹\\mem_TANXIN.csv");
        writeFile(net, "C:\\Users\\TUF\\Desktop\\新建文件夹\\net_TANXIN.csv");
    }

    public static void writeFile(HashMap<String, ArrayList<Double>> cpu, String path) throws Exception {
        StringBuilder builder;
        builder = new StringBuilder();
        builder.append("node1").append(",")
                .append("node2").append(",")
                .append("node3").append(",")
                .append("node4").append(",")
                .append("\n");
        for (int i=0; i<cpu.get("master").size(); i++){
            Double master = cpu.get("master").get(i);
            Double node1 = cpu.get("node1").get(i);
            Double node2 = cpu.get("node2").get(i);
            Double node3 = cpu.get("node3").get(i);

            builder.append(master).append(",")
                    .append(node1).append(",")
                    .append(node2).append(",")
                    .append(node3).append(",")
                    .append("\n");
        }
        File newfile = new File(path);
        BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newfile),"UTF-8"));
        bufferedWriter.write(builder.toString());
        bufferedWriter.close();
    }

    public static HashMap<String, ArrayList<Double> > getHash(File cpufile, String machine_attr) throws IOException {
        Path path = cpufile.toPath();
        byte[] bytes = Files.readAllBytes(path);
        String jsonStr = new String(bytes);
        JSONArray jsonArr = JSONObject.parseArray(jsonStr);
        HashMap<String, ArrayList<Double> > cpuHashMap = new HashMap<>();
        for(int i=0; i<jsonArr.size(); i++){
            JSONObject jsonObject = jsonArr.getJSONObject(i);
            String k = jsonObject.getJSONObject("metric").getString(machine_attr);
            ArrayList<Double> v = new ArrayList<>();
            JSONArray values = jsonObject.getJSONArray("values");
            for (int j=0; j<values.size(); j++){
                JSONArray jsonArray = values.getJSONArray(j);
                Double aDouble = jsonArray.getDouble(1);
                v.add(aDouble);
            }
            cpuHashMap.put(k, v);
        }
        return cpuHashMap;
    }

    public static void outputRes(JSONObject ocrResult){
        ArrayList<String> list = new ArrayList<>();
        JSONArray res = ocrResult.getJSONArray("res");
        for(int i=0; i<res.size(); i++){
            JSONObject object = res.getObject(i, JSONObject.class);
            list.add(object.getString("text"));
        }
        System.out.println(Arrays.toString(list.toArray()));
    }

    private static void getAttributeValue(JSONArray jsonArray){
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        for (String key : jsonObject.keySet()){
            JSONArray jsonArray1 = jsonObject.getJSONArray(key);
            JSONObject jsonObject1 = jsonArray1.getJSONObject(0);
            JSONObject o = new JSONObject();
            o.put(key, jsonObject1.getString("text"));
            System.out.println(o);
        }
    }
}
