package com.simplerpa.cloudservice.entity.util.library.node.read;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.utils.StringUtils;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.RpaTaskOutput;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * @Description: TODO
 * @author: ChenRui98
 * @date: 2022年05月01日 15:08
 */

public class ReadExcelNode extends IRpaTaskNode {
    private byte[] file; // 文件
    private String fileName;
    private String sheetName, sheetNameBck, outputParamName; // 指定的表名(默认为第一张表)、数据参数名称(用户自定义的output名称)
    private Integer colNamePos; // 表头所在行
    private RpaTaskOutput output; // 解析后的数据
    private ArrayList<String> colNameList; // 表头信息

    public ReadExcelNode(TaskNodeDetail nodeDetail){
        output = null;
        colNameList = new ArrayList<>();
        sheetName = null;
        colNamePos = null;
        this.nodeDetail = nodeDetail;
    }

    @Override
    public RpaTaskOutput run(RpaTaskOutput input) throws Exception {
        if(file == null || outputParamName == null){
            throw new Exception(this.getClass().getName() + " : 缺少必要参数，执行失败！");
        }
        detectParamsValue(input);
        String originalFilename = getFileName();
        InputStream in = new ByteArrayInputStream(file);
        Workbook workbook = null;
        if(StringUtils.isNotEmpty(originalFilename)){
            if(originalFilename.endsWith(".xlsx")){
                workbook = new XSSFWorkbook(in);
            }else if(originalFilename.endsWith(".xls")){
                workbook = new HSSFWorkbook(in);
            }else{
                throw new Exception(this.getClass().getName() + " : 不是excel文件，执行失败！");
            }
        }else{
            throw new Exception(this.getClass().getName() + " : 缺少文件名，执行失败！");
        }


        // 获得表
        Sheet sheet = null;
        if(StringUtils.isNotEmpty(getSheetName())){
            sheet = workbook.getSheet(getSheetName());
        }else{
            sheet = workbook.getSheetAt(0);
        }

        // 读取表中的表头信息
        int i = 0;
        if(colNamePos != null){
            i = colNamePos;
        }
        Row headerRow = sheet.getRow(i);
        for(int j=0; j<headerRow.getPhysicalNumberOfCells(); j++){
            String stringCellValue = getCellValue(headerRow.getCell(j));
            if(StringUtils.isNotEmpty(stringCellValue)){
                colNameList.add(stringCellValue);
            }else{
                break;
            }
        }
        if(colNameList.isEmpty()){
            throw new Exception(this.getClass().getName() + " : 没有表头信息！");
        }

        // 读取表信息
        i++;
        for(; i<sheet.getPhysicalNumberOfRows(); i++){
            Row row = sheet.getRow(i);
            JSONObject jsonObject = new JSONObject();
            boolean isAllNull = true;
            for(int j=0; j<colNameList.size(); j++){
                String stringCellValue = getCellValue(row.getCell(j));
                if(StringUtils.isNotEmpty(stringCellValue)){
                    isAllNull = false;
                }
                jsonObject.put(colNameList.get(j), stringCellValue);
            }
            if(isAllNull){
                continue;
            }
            addOutput(jsonObject);
        }
        return output;
    }

    private String getCellValue(Cell cell){
        try{
            return cell.getStringCellValue();
        }catch (Exception e1){
            try{
                return cell.getBooleanCellValue()? "true" : "false";
            }catch (Exception e2){
                try {
                    if(DateUtil.isCellDateFormatted(cell)){
                        Date dateCellValue = cell.getDateCellValue();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        return simpleDateFormat.format(dateCellValue);
                    }
                    return String.valueOf(cell.getNumericCellValue());
                }catch (Exception e3){
                    return null;
                }
            }
        }
    }

    @Override
    public void detectParamsValue(RpaTaskOutput input) throws Exception {
        sheetName = transformParams(sheetName, sheetNameBck, input);
    }

    @Override
    public void clearRpaOutput() {
        output = new RpaTaskOutput();
    }

    private void addOutput(JSONObject jsonObject){
        if(output == null){
            output = new RpaTaskOutput();
        }
        output.addObject(outputParamName, jsonObject);
    }

    private boolean judgeExcelEdition(String fileName){
        return !fileName.matches("^.+\\.(?i)(xls)$");
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public String getOutputParamName() {
        return outputParamName;
    }

    public void setOutputParamName(String outputParamName) {
        this.outputParamName = outputParamName;
    }

    public Integer getColNamePos() {
        return colNamePos;
    }

    public void setColNamePos(Integer colNamePos) {
        this.colNamePos = colNamePos;
    }

    public RpaTaskOutput getOutput() {
        return output;
    }

    public ArrayList<String> getColNameList() {
        return colNameList;
    }

    public void setColNameList(ArrayList<String> colNameList) {
        this.colNameList = colNameList;
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

    public String getSheetNameBck() {
        return sheetNameBck;
    }

    public void setSheetNameBck(String sheetNameBck) {
        this.sheetNameBck = sheetNameBck;
    }
}
