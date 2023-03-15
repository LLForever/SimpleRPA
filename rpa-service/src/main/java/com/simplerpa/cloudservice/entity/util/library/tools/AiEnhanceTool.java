package com.simplerpa.cloudservice.entity.util.library.tools;

import com.alibaba.fastjson.JSONObject;
import com.simplerpa.cloudservice.entity.util.DictionaryUtil;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class AiEnhanceTool {
    private static final String IMAGE_FLAG = "imageByte", TYPE = "type", SCHEMA = "schema";
    public static final String TABLE_OCR = "TABLE_OCR", OCR = "OCR", KEY_EXT = "KEY_WORD_EXTRA";
    public static final String NSGA3 = "NSGA3", NSGA3_TEST = "NSGA3-TEST";

    public static JSONObject getOcrResult(byte[] imageByURL) throws Exception{
        String url = DictionaryUtil.AI_SERVER_URL;

        CloseableHttpClient client = HttpClientBuilder.create().build();

        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(IMAGE_FLAG, imageByURL);
        jsonObject.put(TYPE, OCR);
        StringEntity entity = new StringEntity(jsonObject.toJSONString(), "UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);

        CloseableHttpResponse execute = client.execute(httpPost);
        HttpEntity httpEntity = execute.getEntity();
        String s = EntityUtils.toString(httpEntity);
        JSONObject json = JSONObject.parseObject(s);
        client.close();

        return json;
    }

    public static JSONObject getAiResult(byte[] imageByURL, String type) throws Exception{
        String url = DictionaryUtil.AI_SERVER_URL;

        CloseableHttpClient client = HttpClientBuilder.create().build();

        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(IMAGE_FLAG, imageByURL);
        jsonObject.put(TYPE, type);
        StringEntity entity = new StringEntity(jsonObject.toJSONString(), "UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);

        CloseableHttpResponse execute = client.execute(httpPost);
        HttpEntity httpEntity = execute.getEntity();
        String s = EntityUtils.toString(httpEntity);
        JSONObject json = JSONObject.parseObject(s);
        client.close();

        return json;
    }

    public static JSONObject getOcrResult(String imgURL) throws Exception {
        return getOcrResult(getImageByURL(imgURL));
    }

    public static JSONObject getAiResult(String imgURL, String type) throws Exception {
        return getAiResult(getImageByURL(imgURL), type);
    }

    public static JSONObject getAiResult(String imgURL, String type, List<String> list) throws Exception {
        return getAiResult(getImageByURL(imgURL), type, list);
    }

    public static JSONObject getAiResult(String type, Long[] ids, JSONObject mai, JSONObject mpj) throws Exception{
        String url = DictionaryUtil.AI_SERVER_URL;
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put(TYPE, type);
        jsonObject.put("ids", ids);
        jsonObject.put("mai", mai);
        jsonObject.put("mpj", mpj);

        StringEntity entity = new StringEntity(jsonObject.toJSONString(), "UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);

        CloseableHttpResponse execute = client.execute(httpPost);
        HttpEntity httpEntity = execute.getEntity();
        String s = EntityUtils.toString(httpEntity);
        JSONObject json = JSONObject.parseObject(s);
        client.close();

        return json;
    }

    public static JSONObject getAiResult(String type, int type_int) throws Exception{
        String url = DictionaryUtil.AI_SERVER_URL;
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put(TYPE, type);
        jsonObject.put("type_int", type_int);

        StringEntity entity = new StringEntity(jsonObject.toJSONString(), "UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);

        CloseableHttpResponse execute = client.execute(httpPost);
        HttpEntity httpEntity = execute.getEntity();
        String s = EntityUtils.toString(httpEntity);
        JSONObject json = JSONObject.parseObject(s);
        client.close();

        return json;
    }

    public static JSONObject getAiResult(byte[] imageByURL, String type, List<String> list) throws Exception{
        String url = DictionaryUtil.AI_SERVER_URL;

        CloseableHttpClient client = HttpClientBuilder.create().build();

        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(IMAGE_FLAG, imageByURL);
        jsonObject.put(TYPE, type);
        jsonObject.put(SCHEMA, list);
        StringEntity entity = new StringEntity(jsonObject.toJSONString(), "UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);

        CloseableHttpResponse execute = client.execute(httpPost);
        HttpEntity httpEntity = execute.getEntity();
        String s = EntityUtils.toString(httpEntity);
        JSONObject json = JSONObject.parseObject(s);
        client.close();

        return json;
    }

    public static byte[] getImageByURL(String str) throws Exception{
        URL u = new URL(str);
        BufferedImage image = ImageIO.read(u);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", outputStream);
        outputStream.flush();
        outputStream.close();
        return outputStream.toByteArray();
    }

    public static void main(String[] args) {
        ArrayList<Double> list = new ArrayList<>();
        ArrayList<ArrayList<Double> > lList = new ArrayList<>();
/*
        for (int type=11; type < 14; type++){
            for(int i=0; i<10; i++){
                try{
                    JSONObject aiResult = getAiResult(AiEnhanceTool.NSGA3_TEST, type);
                    String[] split = aiResult.getString("res").split(" ");
                    list.add(Double.parseDouble(split[split.length-1]) + Double.parseDouble(split[split.length-2]));
                    System.out.println(list);
                }catch (Exception e){

                }
            }
            System.out.println("Func:" + type);
            System.out.println("bestFit(mean): " + DictionaryUtil.getAvgByList(list));
            System.out.println("bestFit(std): " + DictionaryUtil.getStdDevByList(list));
            System.out.println("bestFit: " + DictionaryUtil.getMinByList(list));
            ArrayList<Double> tmp = new ArrayList<>();
            tmp.add(DictionaryUtil.getAvgByList(list));
            tmp.add(DictionaryUtil.getStdDevByList(list));
            tmp.add(DictionaryUtil.getMinByList(list));
            lList.add(tmp);
        }
        for (ArrayList<Double> item : lList){
            System.out.println(item);
        }

 */
        list = new ArrayList<Double>(Arrays.asList(89.047196, 61.10375, 82.102976, 119.123156, 73.157863, 51.018499, 53.825651, 55.022878, 24.313535, 67.19702, 133.539916, 154.576894, 142.604745, 144.218387, 180.19291, 129.437294, 138.800586, 108.352668, 143.954344, 159.556918));
        System.out.println("bestFit(mean): " + DictionaryUtil.getAvgByList(list));
        System.out.println("bestFit(std): " + DictionaryUtil.getStdDevByList(list));
        System.out.println("bestFit: " + DictionaryUtil.getMinByList(list));
    }
}
