package com.sumscope.optimus.calculator.tfcalculator.facade.converter;

import com.sumscope.optimus.calculator.tfcalculator.model.dto.CalculationResult;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 导出excel
 * Created by xuejian.sun on 2016/9/6.
 */
@Component
public class ExcelExport {
    private static final String tfTitle[] = {"期货合约","债券代码","交易日","交割日","期货价格","债券收益率( % )","债券净价","债券全价","转换因子","IRR( % )","资金成本( % )","持有期损益","发票价格","基差","净基差","期现价差","利差","交易日应计利息","交割日应计利息","期间付息","加权平均期间付息","试算时间"};
    private static final String tfTitles[] = {"期货合约","债券代码","交易日","交割日","期货价格","债券收益率( % )","T+1收益率( % )","债券净价","债券全价","转换因子","IRR( % )","资金成本( % )","持有期损益","发票价格","基差","净基差","期现价差","利差","交易日应计利息","交割日应计利息","期间付息","加权平均期间付息","试算时间"};

    public String excelExport(HttpServletRequest request,List<CalculationResult> calculationResult) {
        HSSFWorkbook workbook = getHssfWorkbook(calculationResult);
        try {
            File directory = new File("excel");//设定为当前文件夹
            //如果文件夹不存在则创建
            if  (!directory .exists()  && !directory .isDirectory()){
                directory .mkdir();
            }
            String excelPath=directory.getAbsolutePath();
//            String realPath = request.getServletContext().getRealPath("/Excel");
//            File excel = new File(realPath);
//            if(!excel.exists() && !excel.isDirectory()){
//                excel.mkdir();
//            }
            String sessionId = request.getSession().getId();
            String fileName = sessionId+"_"+System.currentTimeMillis();
            File file = new File(excelPath+"/"+fileName+".xls");
            OutputStream out = new FileOutputStream(file);
            workbook.write(out);
            out.close();
            return "Excel/"+fileName+".xls";
        } catch (Exception e) {
            e.printStackTrace();
            return "失败"+e.toString();
        }

    }

    /**
     * 页面导出
     * @param data
     * @return
     */
    public HSSFWorkbook getHssfWorkbook(List<CalculationResult> data) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("金融计算器");
        // 生成一个样式
        HSSFCellStyle style = workbook.createCellStyle();
        setExcelStyle(style,workbook);
        sheet.setDefaultColumnWidth(16);
        HSSFRow row = sheet.createRow(0);
        row.setHeight((short) ((short)200*2));
        if(data!=null && data.size()>0){
            for(int i=0 ;i<data.size();i++){
                if(!"".equals(data.get(i).getBondRateByAddDay()) && data.get(i).getBondRateByAddDay()!=null){
                    setTitleByBondRate(data, tfTitles, sheet, style, row);
                }else{
                    setTitleByBondRate(data, tfTitles, sheet, style, row);
                }
            }
        }
        return workbook;
    }

    private void setTitleByBondRate(List<CalculationResult> data, String[] title, HSSFSheet sheet, HSSFCellStyle style, HSSFRow row) {
        for(int i=0;i<title.length;i++){
            HSSFCell cell = row.createCell(i);
            cell.setCellStyle(style);
            HSSFRichTextString text = new HSSFRichTextString(title[i]);
            cell.setCellValue(text);
        }
        for (int x=1;x<=data.size();x++) {
            HSSFRow rowx = sheet.createRow(x); //第0行为标题行,所以从1开始创建
            rowx.setHeight((short) ((short)200*2));
            for (int j = 0; j < title.length; j++) {
                HSSFCell cell = rowx.createCell(j);
                setCellValues(cell, j,x, data);
                cell.setCellStyle(style);
            }
        }
    }

    private void setExcelStyle(HSSFCellStyle style,HSSFWorkbook workbook){
        style.setFillForegroundColor(HSSFColor.WHITE.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 生成一个字体
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setFontHeightInPoints((short) 10);
     //   font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        style.setFont(font);
    }

    private void setCellValues(Cell cell,int j,int x,List<CalculationResult> data){
        if(!"".equals(data.get(0).getBondRateByAddDay()) && data.get(0).getBondRateByAddDay()!=null){
            switch (j){
                case 0:cell.setCellValue(data.get(x-1).getTfID());break;
                case 1:cell.setCellValue(data.get(x-1).getBondCode());break;
                case 2:cell.setCellValue(data.get(x-1).getTradingDate());break;
                case 3:cell.setCellValue(data.get(x-1).getDeliveryDate());break;
                case 4:cell.setCellValue(data.get(x-1).getFuturePrice());break;
                case 5:cell.setCellValue(data.get(x-1).getBondRate());break;
                case 6:
                    if(data.get(x-1).getBondRateByAddDay()==null){
                        cell.setCellValue(" - ");break;
                    }else{
                        cell.setCellValue(data.get(x-1).getBondRateByAddDay());break;
                    }
                case 7:cell.setCellValue(data.get(x-1).getBondNetPrice());break;
                case 8:cell.setCellValue(data.get(x-1).getBondFullPrice());break;
                case 9:cell.setCellValue(data.get(x-1).getConvertionFactor());break;
                case 10:cell.setCellValue(data.get(x-1).getIrr());break;
                case 11:cell.setCellValue(data.get(x-1).getCapitalCost());break;
                case 12:cell.setCellValue(data.get(x-1).getCarry());break;
                case 13:cell.setCellValue(data.get(x-1).getReceiptPrice());break;
                case 14:cell.setCellValue(data.get(x-1).getBasis());break;
                case 15:cell.setCellValue(data.get(x-1).getNetBasis());break;
                case 16:cell.setCellValue(data.get(x-1).getFutureSpotSpread());break;
                case 17:cell.setCellValue(data.get(x-1).getInterestsRateSpread());break;
                case 18:cell.setCellValue(data.get(x-1).getInterestOnSettlementDate());break;
                case 19:cell.setCellValue(data.get(x-1).getInterestOnTradingDate());break;
                case 20:cell.setCellValue(data.get(x-1).getInterestDuringHolding());break;
                case 21:cell.setCellValue(data.get(x-1).getWeightedAverageInterest());break;
                case 22:cell.setCellValue(data.get(x-1).getTrialTime());break;
            }
        }else{
            switch (j){
                case 0:cell.setCellValue(data.get(x-1).getTfID());break;
                case 1:cell.setCellValue(data.get(x-1).getBondCode());break;
                case 2:cell.setCellValue(data.get(x-1).getTradingDate());break;
                case 3:cell.setCellValue(data.get(x-1).getDeliveryDate());break;
                case 4:cell.setCellValue(data.get(x-1).getFuturePrice());break;
                case 5:cell.setCellValue(data.get(x-1).getBondRate());break;
                case 6:
                    if(data.get(x-1).getBondRateByAddDay()==null){
                        cell.setCellValue(" - ");break;
                    }else{
                        cell.setCellValue(data.get(x-1).getBondRateByAddDay());break;
                    }

                case 7:cell.setCellValue(data.get(x-1).getBondNetPrice());break;
                case 8:cell.setCellValue(data.get(x-1).getBondFullPrice());break;
                case 9:cell.setCellValue(data.get(x-1).getConvertionFactor());break;
                case 10:cell.setCellValue(data.get(x-1).getIrr());break;
                case 11:cell.setCellValue(data.get(x-1).getCapitalCost());break;
                case 12:cell.setCellValue(data.get(x-1).getCarry());break;
                case 13:cell.setCellValue(data.get(x-1).getReceiptPrice());break;
                case 14:cell.setCellValue(data.get(x-1).getBasis());break;
                case 15:cell.setCellValue(data.get(x-1).getNetBasis());break;
                case 16:cell.setCellValue(data.get(x-1).getFutureSpotSpread());break;
                case 17:cell.setCellValue(data.get(x-1).getInterestsRateSpread());break;
                case 18:cell.setCellValue(data.get(x-1).getInterestOnSettlementDate());break;
                case 19:cell.setCellValue(data.get(x-1).getInterestOnTradingDate());break;
                case 20:cell.setCellValue(data.get(x-1).getInterestDuringHolding());break;
                case 21:cell.setCellValue(data.get(x-1).getWeightedAverageInterest());break;
                case 22:cell.setCellValue(data.get(x-1).getTrialTime());break;
            }
        }

    }
}
