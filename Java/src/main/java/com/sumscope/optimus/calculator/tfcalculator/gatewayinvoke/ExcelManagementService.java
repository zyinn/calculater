package com.sumscope.optimus.calculator.tfcalculator.gatewayinvoke;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * Created by xuejian.sun on 2016/9/7.
 * 直接将系统中excel存放临时文件位置的目录删除.
 *
 */
@Service
public class ExcelManagementService {

//    @Value("${application.excelFilePath}")
    private String excelFilePath;

    void removeExcelFile() {
        File directory = new File("excel");//设定为当前文件夹
        //如果文件夹不存在则创建
        if  (!directory .exists()  && !directory .isDirectory()){
            directory .mkdir();
        }
        String excelFilePath=directory.getAbsolutePath();

        File file = new File(excelFilePath);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files.length > 0) {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isFile()) {
                        files[i].delete();
                    }
                }
            } else {
                file.delete();
            }
        }
    }


}
