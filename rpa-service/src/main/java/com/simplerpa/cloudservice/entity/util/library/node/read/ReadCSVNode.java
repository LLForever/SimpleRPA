package com.simplerpa.cloudservice.entity.util.library.node.read;

import com.alibaba.fastjson.JSONObject;
import com.csvreader.CsvReader;
import com.ruoyi.common.core.utils.StringUtils;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.RpaTaskOutput;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class ReadCSVNode extends IRpaTaskNode {
    private byte[] file; // 文件
    private String fileName;
    private RpaTaskOutput output; // 解析后的数据
    private String outputParamName; // 数据参数名称(用户自定义的output名称)
    private char DELIMITER = ' ';

    public ReadCSVNode(TaskNodeDetail taskNodeDetail){
        file = null;
        fileName = null;
        outputParamName = null;
        this.nodeDetail = taskNodeDetail;
    }

    private void setDELIMITER() throws Exception{
        InputStreamReader inputStreamReader = new InputStreamReader(new ByteArrayInputStream(file));
        BufferedReader reader = new BufferedReader(inputStreamReader);
        String str = reader.readLine();
        for(int i=0; i<str.length(); i++){
            if(str.charAt(i) == ','){
                DELIMITER = ',';
                return;
            }
            if(str.charAt(i) == '，'){
                DELIMITER = '，';
                return;
            }
            if(str.charAt(i) == ' '){
                DELIMITER = ' ';
                return;
            }
        }
    }

    @Override
    public RpaTaskOutput run(RpaTaskOutput input) throws Exception {
        if(file == null || outputParamName == null){
            throw new Exception(this.getClass().getName() + " : 缺少必要参数，执行失败！");
        }
        if(StringUtils.isNotEmpty(getFileName())){
            if(getFileName().endsWith(".csv")){
                InputStream in = new ByteArrayInputStream(file);
                CsvReader csvReader = null;
                try{
                    setDELIMITER();
                    csvReader = new CsvReader(in, DELIMITER, Charset.defaultCharset());
                    String[] headers = null;
                    while(csvReader.readRecord()){
                        String[] values = csvReader.getValues();
                        if(headers == null){
                            headers = values;
                            continue;
                        }
                        JSONObject jsonObject = new JSONObject();
                        for(int i=0; i<headers.length; i++){
                            jsonObject.put(headers[i], values[i]);
                        }
                        addOutput(jsonObject);
                    }
                    return output;
                }catch (Exception e){
                    throw new Exception(this.getClass().getName() + " : csv文件读取失败！");
                }finally {
                    if(csvReader != null){
                        csvReader.close();
                    }
                }
            }else{
                throw new Exception(this.getClass().getName() + " : 不是csv文件，执行失败！");
            }
        }else{
            throw new Exception(this.getClass().getName() + " : 缺少文件名，执行失败！");
        }
    }

    private void addOutput(JSONObject jsonObject){
        if(output == null){
            output = new RpaTaskOutput();
        }
        output.addObject(outputParamName, jsonObject);
    }

    public String getFileName() {
        return fileName;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOutputParamName() {
        return outputParamName;
    }

    public void setOutputParamName(String outputParamName) {
        this.outputParamName = outputParamName;
    }
}
