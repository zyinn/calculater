package com.sumscope.optimus.calculator.planalysis.gateway;


import com.sumscope.optimus.calculator.planalysis.model.dto.RepoRateDto;
import com.sumscope.optimus.tfcalculator.planalysis.gateway.model.HolidayInfo;

import java.util.List;

/**
 * Created by fan.bai on 2016/5/5.
 * 获取RepoRate列表及其最新价格
 */
public interface RepoRateGatewayService {
    /**
     * @return 所有的RepoRates及其价格
     */
    List<RepoRateDto> retrieveRepoRatesWithPrice();

    /**
     *获取上一个
     */
    List<HolidayInfo> retrieveHolidayInfo();
}
