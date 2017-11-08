package com.sumscope.optimus.calculator.planalysis.facade;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by simon.mao on 2016/4/26.
 */
public interface BasisAnalysisFacade {
    /**
     * 用户第一次进入界面触发该接口，获取所有期货合约及默认期货的所有可交割卷，计算CTD并按CTD进行盈亏分析
     * @param request Request
     * @param response Response
     * @return Json字符串华的ResponseInititialBasisDto对象
     */
    void initialRequest(HttpServletRequest request, HttpServletResponse response);
    /**
     * 用户传入期货合约，第一次进入界面触发该接口，获取所有期货合约及默认期货的所有可交割卷，计算CTD并按CTD进行盈亏分析
     * @param params tfId
     * @param request Request
     * @param response Response
     * @return Json字符串华的ResponseDto对象
     */
    void initialRequestWithTfId(String params, HttpServletRequest request, HttpServletResponse response);

    /**
     * 用户获取最新期货价格触发该接口，获取最新期货价格并重新进行盈亏分析计算
     * @param params json化的RequestDto对象，
     * @param request Request
     * @param response Response
     * @return Json字符串华的ResponseDto对象
     */
    void futurePriceUpdate(String params, HttpServletRequest request, HttpServletResponse response);
    /**
     * 用户获取某债券后，点击特定收益率按钮，获取最新收益率，并根据最新收益率进行盈亏分析计算
     * @param params json化的RequestDto对象，该对象中yieldType表明获取何种最新收益率，有关现卷收益率的字段可置空
     * @param request Request
     * @param response Response
     * @return Json字符串华的ResponseDto对象
     */
    void yieldTypeUpdate(String params, HttpServletRequest request, HttpServletResponse response);
    /**
     * 用户修改参数触发该接口，进行新的盈亏分析
     * @param params json化的RequestDto对象
     * @param request Request
     * @param response Response
     * @return Json字符串华的ResponseDto对象
     */
    void paramUpdate(String params, HttpServletRequest request, HttpServletResponse response);

    /**
     * 根据用户输入的4个债券前缀，查询所有对应的固息债并返回前端。前端根据用户选择再触发yieldTypeUpdate（）进行盈亏计算
     * @param params 4字符的前缀，比如 “1500”
     * @param request Request
     * @param response Response
     * @return Json字符串华的List<BondPrimaryKeyDto>对象
     */
    void findBondsByNamePrefix(String params,HttpServletRequest request, HttpServletResponse response );

    /**
     * 浏览器请求时，会先OPTIONS请求，不能带参数
     * @param request
     * @param response
     */
    void initialRequestOptions(HttpServletRequest request, HttpServletResponse response );

    void futureContractUpdateOptions(HttpServletRequest request, HttpServletResponse response );

    void futurePriceUpdateOptions(HttpServletRequest request, HttpServletResponse response );

    void yieldTypeUpdateOptions(HttpServletRequest request, HttpServletResponse response );

    void paramUpdateOptions(HttpServletRequest request, HttpServletResponse response );

    void findBondsByNamePrefixOptions(HttpServletRequest request, HttpServletResponse response );
}
