package com.nyuen.camunda.utils;

import com.alibaba.fastjson.JSONArray;
import com.nyuen.camunda.domain.vo.SampleRow;
import com.nyuen.camunda.domain.vo.SampleRowAndCell;
import com.nyuen.camunda.result.Result;
import com.nyuen.camunda.result.ResultFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

/**
 * 处理Excel工具类
 *
 * @author chengjl
 * @description 处理Excel工具类
 * @date 2022/11/4
 */
public class ExcelUtil {

    public static Result dealDataByExcel(MultipartFile multipartFile) throws IOException, InvalidFormatException {
        // 判断文件类型是否是Excel
        String excelName = multipartFile.getOriginalFilename();
        if(null == excelName){
            return ResultFactory.buildFailResult("文件名称不能为空！");
        }
        String suffix = excelName.substring(excelName.indexOf(".")+1);
        if(!"xls".equals(suffix) && !"xlsx".equals(suffix)) {
            return ResultFactory.buildFailResult("文件类型错误，仅限excel文件");
        }
        Workbook wb;
        //根据文件后缀（xls/xlsx）进行判断
        File file = multipartFileToFile(multipartFile);
        if ( "xls".equals(suffix)){
            FileInputStream fis =  new FileInputStream(file);  //文件流对象
            wb = new HSSFWorkbook(fis);
        }else {
            wb = new XSSFWorkbook(file);
        }
        //开始解析
        Sheet sheet = wb.getSheetAt(0);     //读取sheet 0

        int firstRowIndex = sheet.getFirstRowNum()+1;   //第一行是列名，所以不读
        int lastRowIndex = sheet.getLastRowNum();
        //System.out.println("firstRowIndex: "+firstRowIndex);
        //System.out.println("lastRowIndex: "+lastRowIndex);
        List<SampleRow> dataList = new ArrayList<>();
        for(int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {   //遍历行
            //System.out.println("rIndex: " + rIndex);
            List<String> rowCellList = new ArrayList<>();
            Row row = sheet.getRow(rIndex);
            if (row != null) {
                int firstCellIndex = row.getFirstCellNum();
                int lastCellIndex = row.getLastCellNum();
                // 又一个小细节  ==> firstCellIndex+1 第一列是样本编号
                for (int cIndex = firstCellIndex+1 ; cIndex < lastCellIndex; cIndex++) {   //遍历列
                    Cell cell = row.getCell(cIndex);
                    if (cell != null) {
                        //System.out.println(cell.toString());
                        cell.setCellType(CellType.STRING);
                        rowCellList.add(cell.toString().trim());
                    }
                }
            }
            if(row == null || row.getCell(0) == null){
                return ResultFactory.buildFailResult("第一列样本编号不能为空！");
            }
            row.getCell(0).setCellType(CellType.STRING);
            SampleRow sampleRow = new SampleRow(row.getCell(0).getStringCellValue().trim(), rowCellList);
            dataList.add(sampleRow);
        }
        return ResultFactory.buildSuccessResult(mergeDataBySampleInfo(dataList));
    }

    public static File multipartFileToFile(MultipartFile multipartFile) throws IOException {
        File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        InputStream ins=multipartFile.getInputStream();
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1){
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
            // springboot自动删除
            //file.deleteOnExit();//删除临时文件
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public static List<SampleRowAndCell> mergeDataBySampleInfo(List<SampleRow> dataList){
        List<SampleRowAndCell> sampleRowAndCellList = new ArrayList<>();
        //得到不重复的所有样本编号集合
        LinkedHashSet<String> sampleSet = new LinkedHashSet();
        for (SampleRow sampleRow : dataList) {
            sampleSet.add(sampleRow.getSampleInfo());
        }
        for (Object o : sampleSet) {
            String sampleInfo = o.toString();
            List<List<String>> sampleRowList = new ArrayList<>();
            for (SampleRow sampleRow : dataList) {
                if (sampleInfo.equals(sampleRow.getSampleInfo())) {
                    sampleRowList.add(sampleRow.getRowData());
                }
            }
            SampleRowAndCell sampleRowAndCell = new SampleRowAndCell(sampleInfo, sampleRowList);
            sampleRowAndCellList.add(sampleRowAndCell);
        }
        return sampleRowAndCellList;
    }

}
