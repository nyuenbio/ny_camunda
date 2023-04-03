package com.nyuen.camunda.utils;

import com.alibaba.fastjson.JSONArray;
import com.nyuen.camunda.domain.po.SampleLabInfo;
import com.nyuen.camunda.domain.vo.SampleRow;
import com.nyuen.camunda.domain.vo.SampleRowAndCell;
import com.nyuen.camunda.result.Result;
import com.nyuen.camunda.result.ResultFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
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
                return ResultFactory.buildFailResult("第"+(rIndex+1)+"行第一列样本编号不能为空！");
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


    public static void exportExcel(HttpServletResponse response, String[] header, List<SampleLabInfo> content,
                                   String fileName, String sheetName) throws Exception{
        fileName = fileName + ".xlsx";
        Workbook wb = new SXSSFWorkbook(1000);
        Sheet sheet = wb.createSheet(sheetName);
        Row row = sheet.createRow( 0);
        // 行高
        row.setHeight((short) 700);
        // 列宽
        for (int i = 0; i < header.length; i++) {
            sheet.setColumnWidth(i, 20 * 256);
        }
        for (int i = 0; i < header.length; i++) {
            org.apache.poi.ss.usermodel.Cell cell = row.createCell(i);
            cell.setCellValue(header[i]);
            cell.setCellStyle(HeaderStyle(wb));
        }
        //{"样本编号", "产品名称", "孔位", "孔位编号", "ASSAY编号","创建时间","备注","样本类型", "医院名称"}
        for (int i = 0; i < content.size(); i++) {
            row = sheet.createRow((int) i + 1);
            row.setHeight((short) 500);
            org.apache.poi.ss.usermodel.Cell cell0 = row.createCell(0);
            cell0.setCellValue(content.get(i).getSampleInfo());
            cell0.setCellStyle(contentStyle(wb));
            org.apache.poi.ss.usermodel.Cell cell1 = row.createCell(1);
            cell1.setCellValue(content.get(i).getProductName());
            cell1.setCellStyle(contentStyle(wb));
            org.apache.poi.ss.usermodel.Cell cell2 = row.createCell(2);
            cell2.setCellValue(content.get(i).getHoleNum());
            cell2.setCellStyle(contentStyle(wb));
            org.apache.poi.ss.usermodel.Cell cell3 = row.createCell(3);
            cell3.setCellValue(content.get(i).getHoleCode());
            cell3.setCellStyle(contentStyle(wb));
            org.apache.poi.ss.usermodel.Cell cell4 = row.createCell(4);
            cell4.setCellValue(content.get(i).getAssayCode());
            cell4.setCellStyle(contentStyle(wb));
            org.apache.poi.ss.usermodel.Cell cell5 = row.createCell(5);
            cell5.setCellValue(DateUtil.DateToString(content.get(i).getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
            cell5.setCellStyle(contentStyle(wb));
            org.apache.poi.ss.usermodel.Cell cell6 = row.createCell(6);
            cell6.setCellValue(content.get(i).getRemark());
            cell6.setCellStyle(contentStyle(wb));
            org.apache.poi.ss.usermodel.Cell cell7 = row.createCell(7);
            cell7.setCellValue(content.get(i).getSampleType());
            cell7.setCellStyle(contentStyle(wb));
            org.apache.poi.ss.usermodel.Cell cell8 = row.createCell(8);
            cell8.setCellValue(content.get(i).getHospitalName());
            cell8.setCellStyle(contentStyle(wb));
        }
//        if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
//            title = new String(title.getBytes("UTF-8"), "ISO8859-1"); // firefox浏览器
//        } else if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
//            title = URLEncoder.encode(title, "UTF-8");// IE浏览器
//        } else if (request.getHeader("User-Agent").toUpperCase().indexOf("CHROME") > 0) {
//            title = new String(title.getBytes("UTF-8"), "ISO8859-1");// 谷歌
//        }
        fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), "ISO8859-1");
        response.reset();
        response.setContentType("application/octet-stream; charset=utf-8");
        //response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        wb.write(response.getOutputStream());
        response.getOutputStream().close();
    }

    /**
     * 表头样式
     */
    private static CellStyle HeaderStyle(Workbook wb){
        Font font = wb.createFont();
        font.setFontName("宋体");
        font.setBold(true);
        font.setFontHeightInPoints((short) 11);
        CellStyle cellStyle = commonStyle(wb);
        cellStyle.setFont(font);
        return cellStyle;
    }

    /**
     * 内容样式
     */
    private static CellStyle contentStyle(Workbook wb){
        Font font = wb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 10);
        CellStyle cellStyle = commonStyle(wb);
        cellStyle.setFont(font);
        return cellStyle;
    }

    /**
     * 公共样式
     */
    private static CellStyle commonStyle(Workbook wb){
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setWrapText(true);// 自动换行
        return style;
    }


}
