package com.sumscope.optimus.calculator.tfcalculator.facade;

import com.sumscope.optimus.calculator.tfcalculator.model.dto.CalculationResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * TF计算器应用Facade接口。提供了web页面分场景调用计算服务的方法集合。
 */
public interface TFCalculatorFacade {

    /**
     * 当TF计算器页面打开时第一次执行的初始化计算方法。除了获取默认期货及对应的可交割卷等计算结果，还给出了资金成本等列表信息
     * <p>
     * 返回前端对象：InitialResponsesDto
     */
     void initPage(HttpServletRequest request, HttpServletResponse response);

    /**
     * 触发该计算方法的场景有两个:
     * 1. 用户修改了当前的期货合约
     * 2. 用户改变了债卷窗口
     * 该方法根据用户当前的不同债卷窗口和计算目标进行QDP计算并返回结果。
     * 若债卷窗口在可交割卷或者未发国债时，需要先获取可交割卷，未发国债列表进行计算。
     * 在自选卷和虚拟卷窗口时，若用户设置了债卷信息则进行计算并返回结果，反之则直接返回未计算的数值。
     * <p>
     * 前端请求对象：FutureInitRequestDto
     * 返回响应对象：FutureInitResponseDto
     */
     void doFutureInitCalculation(HttpServletRequest httpRequest, HttpServletResponse httpResponse,String futureInit);

    /**
     * 触发该方法进行计算的场景为：
     * 用户在IRR或者债卷价格计算目标窗口下试图获取最新期货价格进行计算
     * 当前所选的债卷机器相关信息需要一并传递，否则将不进行QDP计算直接返回空计算结果
     * <p>
     * <p>
     * 前端请求对象：GetFuturePriceRequestDto
     * 返回响应对象：GetFuturePriceResponseDto
     */
     void doGetFuturePriceCalculation(HttpServletRequest httpRequest, HttpServletResponse httpResponse,String futurePrice);

    /**
     * 该方法触发场景为：
     * 当用户在可交割卷或者未发国债窗口改变了当前选择的债卷代码时触发计算。
     * 在可交割卷场景设置转换因子并直接返回，未发国债窗口这需要从QDP结果中获取转换因子
     * <p>
     * <p>
     * 前端请求对象：BondChangedRequestDto
     * 返回响应对象：BondChangedResponseDto
     */
     void doBondChangedCalculation(HttpServletRequest httpRequest, HttpServletResponse httpResponse,String changBond);

    /**
     * 触发场景为：
     * 用户在IRR，期货理论价格及期货情景分析界面，同时在可交割卷，自选卷窗口按4个债卷价格类型按钮时触发计算。
     * 该方法获取当前债卷的最新收益率（类型根据对应的债卷收益率类型按钮决定，在OFR、BID及成交类型没有最新收益率时取中债收益率）进行QDP计算。
     * <p>
     * 前端请求对象：GetYieldRequestDto
     * 返回响应对象：GetYieldResponseDto
     */
     void doGetBondYieldCalculation(HttpServletRequest httpRequest, HttpServletResponse httpResponse,String bondYield);

    /**
     * 触发场景为：
     * 所有其他特定场景之外，对于输入控件的修改都进行计算的触发。
     * <p>
     * 前端请求对象：CalculationRequestDto
     * 返回响应对象：CalculationResponseDto
     */
     void doCalculation(HttpServletRequest httpRequest, HttpServletResponse httpResponse,String doCalculation);

    /**
     * 获取所有资金成本列表
     */
     void retrieveRepoRates(HttpServletRequest request, HttpServletResponse response);

    /**
     * 根据输入前缀获取固息债卷列表
     */
     void findFixInterestBondsByPrefix(HttpServletRequest request, HttpServletResponse response,String inputParams);

    /**
     * excle导出
     * @param request
     * @param response
     * @param calculationResult
     */
     void excelExport(HttpServletRequest request, HttpServletResponse response, List<CalculationResult> calculationResult);

}
