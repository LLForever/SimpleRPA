package com.simplerpa.cloudservice.entity.util.library.node.read;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.utils.StringUtils;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.DictionaryUtil;
import com.simplerpa.cloudservice.entity.util.RpaTaskOutput;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

/**
 * @Description: TODO
 * @author: ChenRui98
 * @date: 2022年05月01日 16:14
 */

public class ReadTxtNode extends IRpaTaskNode {
    private RpaTaskOutput output; // 解析后的数据
    private byte[] file; // 文件
    private String fileName;
    private String outputParamName; // 数据参数名称(用户自定义的output名称)

    public ReadTxtNode(TaskNodeDetail nodeDetail){
        output = null;
        this.nodeDetail = nodeDetail;
    }

    @Override
    public RpaTaskOutput run(RpaTaskOutput input) throws Exception {
        if(file == null || outputParamName == null){
            throw new Exception(this.getClass().getName() + " : 缺少必要参数，执行失败！");
        }
        String originalFilename = getFileName();
        if(StringUtils.isNotEmpty(originalFilename)){
            if(originalFilename.endsWith(".txt")){
                InputStreamReader inputStreamReader = new InputStreamReader(new ByteArrayInputStream(file));
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String s = bufferedReader.readLine();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(DictionaryUtil.SINGLE_PARAM_FLAG, s);
                addOutput(jsonObject);
            }else{
                throw new Exception(this.getClass().getName() + " : 不是txt文件，执行失败！");
            }
        }else{
            throw new Exception(this.getClass().getName() + " : 缺少文件名，执行失败！");
        }
        return output;
    }

    private void addOutput(JSONObject jsonObject){
        if(output == null){
            output = new RpaTaskOutput();
        }
        output.addObject(outputParamName, jsonObject);
    }

    public RpaTaskOutput getOutput() {
        return output;
    }

    public String getOutputParamName() {
        return outputParamName;
    }

    public void setOutputParamName(String outputParamName) {
        this.outputParamName = outputParamName;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
