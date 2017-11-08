package com.sumscope.optimus.calculator.planalysis.service;

import com.sumscope.optimus.calculator.planalysis.model.dto.BondPrimaryKeyDto;
import com.sumscope.optimus.calculator.planalysis.model.dto.RequestDto;
import com.sumscope.optimus.calculator.planalysis.model.dto.ResponseDto;
import com.sumscope.optimus.calculator.planalysis.model.dto.ResponseInititialBasisDto;

import java.util.List;

/**
 * Created by fan.bai on 2016/5/3.
 */
public interface BasisAnalysisService {
    /**
     * 初始化页面，返回资金成本列表，期货合约列表
     * @param params String
     * @return ResponseInititialBasisDto
     */
    ResponseInititialBasisDto inititialRequest(String params);

    /**
     * 当期货价格变动时，触发该接口，计算新的期货盈亏分析
     * @param requestDto RequestDto
     * @return ResponseDto
     */
    ResponseDto futureContractPriceUpdate(RequestDto requestDto);

    /**
     * 用户希望获取Ofr/Bid/成交/中债 最新价格时，触发该接口，进行新的盈亏分析计算
     * @param requestDto RequestDto
     * @return ResponseDto
     */
    ResponseDto yieldTypeUpdate(RequestDto requestDto);

    /**
     * 当用户修改其他业务数据时，触发该接口，进行新的盈亏分析计算
     * @param requestDto RequestDto
     * @return ResponseDto
     */
    ResponseDto paramUpdate(RequestDto requestDto);

    /**
     * 根据输入的4字符前缀查询所有对应债券以及最新成交收益率
     * @param namePrifix 4字符前缀，比如"1500"
     * @return 所有对应债券code机器最新成交收益率
     */
    List<BondPrimaryKeyDto> findBondsByNamePrifix(String namePrifix);

}
