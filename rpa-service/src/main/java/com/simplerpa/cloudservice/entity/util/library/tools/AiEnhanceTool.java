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

public class AiEnhanceTool {
    private static final String IMAGE_FLAG = "imageByte", TYPE = "type";
    public static final String TABLE_OCR = "TABLE_OCR", OCR = "OCR";

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

    public static byte[] getImageByURL(String str) throws Exception{
        URL u = new URL(str);
        BufferedImage image = ImageIO.read(u);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", outputStream);
        outputStream.flush();
        outputStream.close();
        return outputStream.toByteArray();
    }
}
