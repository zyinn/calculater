package com.sumscope.optimus.calculator.tfcalculator.gatewayinvoke;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * 定时删除excel文件任务
 * Created by xuejian.sun on 20169/7.
 */
@Service
@Configurable
@EnableScheduling
@PropertySource("classpath:application.yml")
public class ApplicationTimeService {
    @Autowired
    private ExcelManagementService excel;

//   @Scheduled(cron="${application.excelRemove.schedule}")
    public void removeExcel(){
        excel.removeExcelFile();
    }
}
