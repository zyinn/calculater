package com.sumscope.optimus.calculator.tfcalculator.gatewayinvoke;

import com.sumscope.optimus.calculator.planalysis.commons.util.NumberUtil;
import com.sumscope.optimus.calculator.planalysis.model.qdp.*;
import com.sumscope.optimus.calculator.shared.enums.BondPriceType;
import com.sumscope.optimus.calculator.shared.enums.PaymenyFrequency;
import com.sumscope.optimus.calculator.shared.enums.YieldTypeEnum;
import com.sumscope.optimus.calculator.shared.model.dbmodel.BondInfo;
import com.sumscope.optimus.calculator.shared.model.dbmodel.FutureContract;
import com.sumscope.optimus.calculator.shared.model.dbmodel.ScheduledBondInfo;
import com.sumscope.optimus.calculator.shared.service.CalculationDataService;
import com.sumscope.optimus.calculator.tfcalculator.commons.enums.CalculationTarget;
import com.sumscope.optimus.calculator.tfcalculator.gatewayinvoke.dto.TFCalculatorQDPRequest;
import com.sumscope.optimus.calculator.tfcalculator.model.dbmodel.AbstractCalculationRequest;
import com.sumscope.optimus.calculator.tfcalculator.model.dbmodel.CalculationRequest;
import com.sumscope.optimus.calculator.tfcalculator.model.dbmodel.CalculationRequestForInit;
import com.sumscope.optimus.calculator.tfcalculator.model.dbmodel.CalculationRequestWithBondYieldType;
import com.sumscope.optimus.commons.exceptions.BusinessRuntimeException;
import com.sumscope.optimus.commons.exceptions.BusinessRuntimeExceptionType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * 接口转换程序。金融计算器从数据库及前端获取必要的数据，生成本地计算请求类型传入该程序。该程序转换本地计算请求为符合QDP计算要求的特定请求类型。
 */
@Component
public class TFCalculationQDPRequestConverter {

    @Autowired
    private CalculationDataService calculationDataService;

    /**
     * 将系统产生的本地请求转换为QDP计算要求的请求类型
     */
    public TFCalculatorQDPRequest convertCalculationRequest(FutureContract futureContract,BondInfo bondInfo, AbstractCalculationRequest calculationRequest) {
        TFCalculatorQDPRequest tFCalculatorQDPRequest=new TFCalculatorQDPRequest();
        List<BondInfo> singleBond = new ArrayList<>();
        List<FixedRateBondInfo> fix = new ArrayList<>();
        tFCalculatorQDPRequest.setFundingRate(NumberUtil.divide(calculationRequest.getCapitalCost(),100));//资金成本
        tFCalculatorQDPRequest.setFuturesPrice(calculationRequest.getFuturePrice());//期货价格
        List<BondInfo> deliverableBonds=new ArrayList<>();
        List<ScheduledBondInfo> scheduledBondInfos =new ArrayList<>();
        if("CalculationRequestForInit".equals(calculationRequest.getDistinguish())){
            CalculationRequestForInit calculationRequestForInit=new CalculationRequestForInit();
            BeanUtils.copyProperties(calculationRequest,calculationRequestForInit);
            deliverableBonds = calculationRequestForInit.getSelectableBonds();//获取可交割卷
            tFCalculatorQDPRequest.setDeliverableBondYtm(getKeyValuePair(calculationRequestForInit.getBondYieldWithKeyValuePair()));

            scheduledBondInfos = calculationRequestForInit.getScheduledBondInfos();//获取未发国债的可交割卷
            fix = getFixedRateBondInfos(deliverableBonds,scheduledBondInfos);

            if(scheduledBondInfos!=null && scheduledBondInfos.size()>0){
                calculationRequest.setBondPriceType(BondPriceType.YIELD);
            }
            calculationRequestForInit.setBondYieldType(calculationRequestForInit.getBondYieldType()!=null? calculationRequestForInit.getBondYieldType():YieldTypeEnum.cdc);
        }else if("CalculationRequestWithBondYieldType".equals(calculationRequest.getDistinguish())){
            CalculationRequestWithBondYieldType calculationRequestWithBondYieldType=new CalculationRequestWithBondYieldType();
            BeanUtils.copyProperties(calculationRequest,calculationRequestWithBondYieldType);
        }else{
            CalculationRequest CalculationRequest=new CalculationRequest();
            BeanUtils.copyProperties(calculationRequest,CalculationRequest);
        }
        //在IRR/基差 下 ，如果bondInfo为空，这把整个债劵列表传到QDP中计算，否则只计算其中一个bondInfo债劵
        if(bondInfo!=null&&bondInfo.getBondCode()!=null){
            fix = getBondInfoByBondPriceType(bondInfo, calculationRequest, tFCalculatorQDPRequest, singleBond);
        }
        tFCalculatorQDPRequest.setMaturityMoveDay(1);//到期日
        //PriceRequest计算目标 Irr计算Irr/FairQuote计算期货价格/UnderlyingFairQuote计算可交割券价格/MktQuote计算期货理论价格
        tFCalculatorQDPRequest.setPricingRequest(calculationRequest.getCalculationTarget().getDisplayName());
        setBondIrrOrNetOrBasisRate(bondInfo, calculationRequest, tFCalculatorQDPRequest, deliverableBonds);
        setTradeInfo(futureContract, tFCalculatorQDPRequest, fix);
        checkData(tFCalculatorQDPRequest);
        return tFCalculatorQDPRequest;
    }
    private void checkData( TFCalculatorQDPRequest tFCalculatorQDPRequest){
        if(tFCalculatorQDPRequest.getTradeInfo().getDeliverableBondInfos()==null || tFCalculatorQDPRequest.getTradeInfo().getDeliverableBondInfos().size()<=0){
            throw new BusinessRuntimeException(BusinessRuntimeExceptionType.DATABASE_ERROR, "该合约下暂无债劵！");
        }
    }
    /**
     * 设置tradeInfo信息
     * @param futureContract
     * @param tFCalculatorQDPRequest
     * @param fix
     */
    private void setTradeInfo(FutureContract futureContract, TFCalculatorQDPRequest tFCalculatorQDPRequest, List<FixedRateBondInfo> fix) {
        TradeInfo tradeInfo=new TradeInfo();
        tradeInfo.setValuationParamters(new ValuationParamter());
        SimpleDateFormat formats = new SimpleDateFormat("yyyy-MM-dd");
        tradeInfo.setStartDate(formats.format(futureContract.getTradingDate()));
        tradeInfo.setMaturityDate(formats.format(futureContract.getDeliveryDate()));
        tradeInfo.setTradeId(futureContract.getTfId());
        tradeInfo.setDeliverableBondInfos(fix);
        tFCalculatorQDPRequest.setTradeInfo(tradeInfo);
    }
    private List<KeyValuePair> getKeyValuePair(List<BondYieldWithKeyValuePair> bondYieldList){
        List<KeyValuePair> list = new ArrayList<>();
        if(bondYieldList!=null && bondYieldList.size()>0){
            for(BondYieldWithKeyValuePair bondYield: bondYieldList){
                KeyValuePair keyValuePair=new KeyValuePair();
                BeanUtils.copyProperties(bondYield,keyValuePair);
                list.add(keyValuePair);
            }
        }
        return list;
    }
    /**
     * 设置债劵的Irr 或者 基差、净基差
     * @param bondInfo
     * @param calculationRequest
     * @param tFCalculatorQDPRequest
     * @param deliverableBonds
     */
    private void setBondIrrOrNetOrBasisRate(BondInfo bondInfo, AbstractCalculationRequest calculationRequest, TFCalculatorQDPRequest tFCalculatorQDPRequest, List<BondInfo> deliverableBonds) {
        if(calculationRequest.getIrr()!=null){
            tFCalculatorQDPRequest.setIrrRate(calculationRequest.getIrr());//Irr
        }else if(calculationRequest.getBasis()!=null){
            List<KeyValuePair> keyValueList = getBondBasisOrIrrOrNetBasis(calculationRequest, deliverableBonds,bondInfo);
            tFCalculatorQDPRequest.setDeliverableBondBasis(keyValueList);//交割债券净基差
        }else if(calculationRequest.getNetBasis()!=null){
            List<KeyValuePair> keyValueList = getBondBasisOrIrrOrNetBasis(calculationRequest, deliverableBonds,bondInfo);
            tFCalculatorQDPRequest.setDeliverableBondNetBasis(keyValueList);//交割债券基差
        }else{
            tFCalculatorQDPRequest.setIrrRate(null);//Irr
        }
    }

    /**
     * 设置 债劵的净价/全价/收益率/
     * @param bondInfo
     * @param calculationRequest
     * @param tFCalculatorQDPRequest
     * @param singleBond
     * @return
     */
    private List<FixedRateBondInfo> getBondInfoByBondPriceType(BondInfo bondInfo, AbstractCalculationRequest calculationRequest, TFCalculatorQDPRequest tFCalculatorQDPRequest,
        List<BondInfo> singleBond) {
        List<FixedRateBondInfo> fix = new ArrayList<>();
        if(calculationRequest.getBondPriceType()== BondPriceType.FULL_PRICE){//全价 //净价
            List<KeyValuePair> keyValuePairs = new ArrayList<>();
            getBondNetOrFullPrice(bondInfo, calculationRequest, keyValuePairs);
            fix = getRateBondInfoWithNetOrFull(bondInfo, singleBond);
            tFCalculatorQDPRequest.setDeliverableBondDirtyPrices(keyValuePairs);
        }
        if(calculationRequest.getBondPriceType()== BondPriceType.NET_PRICE){
            List<KeyValuePair> keyValuePairs = new ArrayList<>();
            getBondNetOrFullPrice(bondInfo, calculationRequest, keyValuePairs);
            fix = getRateBondInfoWithNetOrFull(bondInfo, singleBond);
            tFCalculatorQDPRequest.setDeliverableBondCleanPrices(keyValuePairs);
        }
        if(calculationRequest.getBondPriceType()==BondPriceType.YIELD) {//收益率
            List<KeyValuePair> keyValuePairs = new ArrayList<>();
            KeyValuePair keyValuePair = new KeyValuePair();
            keyValuePair.setKey(bondInfo.getBondCode());
            keyValuePair.setValue(calculationRequest.getBondPrice() != null ? NumberUtil.divide(calculationRequest.getBondPrice(), 100) : null);
            keyValuePairs.add(keyValuePair);
            keyValuePairs.remove(calculationRequest.getBondPrice() == null ? keyValuePair : null);
            fix = getRateBondInfoWithNetOrFull(bondInfo, singleBond);
            tFCalculatorQDPRequest.setDeliverableBondYtm((keyValuePairs == null || keyValuePairs.size() == 0) ? null : keyValuePairs);
            if (CalculationTarget.IRR_BASE == calculationRequest.getCalculationTarget() && (tFCalculatorQDPRequest == null
                    || (tFCalculatorQDPRequest.getDeliverableBondYtm() == null|| tFCalculatorQDPRequest.getDeliverableBondYtm().size() <= 0))) {
                throw new BusinessRuntimeException(BusinessRuntimeExceptionType.DATABASE_ERROR, "今日暂无收益率，请选择其他收益率或输入收益率");
            }
        }
        return fix;
    }

    /**
     * 在净价全价下设置债劵列表 转FixedRateBondInfo列表
     * @param bondInfo
     * @param singleBond
     * @return
     */
    private List<FixedRateBondInfo> getRateBondInfoWithNetOrFull(BondInfo bondInfo, List<BondInfo> singleBond) {
        singleBond.add(bondInfo);
        return getFixedRateBondInfos(singleBond,null);
    }

    /**
     * 判断是净价还是全价 转 对应的keyValuePair列表
     * @param bondInfo
     * @param calculationRequest
     * @param keyValuePairs
     */
    private void getBondNetOrFullPrice(BondInfo bondInfo, AbstractCalculationRequest calculationRequest, List<KeyValuePair> keyValuePairs) {
        KeyValuePair keyValuePair = new KeyValuePair();
        if(calculationRequest.getBondPrice()!=null){
            String code=bondInfo.getBondCode();
            keyValuePair.setKey(code);
            keyValuePair.setValue(calculationRequest.getBondPrice());
            keyValuePairs.add(keyValuePair);
        }
    }

    /**
     * 得到对应的Irr/基差/净基差的key - value 列表
     * @param calculationRequest
     * @param deliverableBonds
     * @param bondInfos
     * @return
     */
    private List<KeyValuePair> getBondBasisOrIrrOrNetBasis(AbstractCalculationRequest calculationRequest, List<BondInfo> deliverableBonds,BondInfo bondInfos) {
        List<KeyValuePair> keyValueList=new ArrayList<>();
        if(deliverableBonds!=null&&deliverableBonds.size()>0){
            List<BondInfo> bondInfoWithoutPrice = new ArrayList<BondInfo>();
            for(BondInfo bondInfo:deliverableBonds){
                BigDecimal price =calculationDataService.getBondOfrLatestPrice(bondInfo.getBondKey(), bondInfo.getListedMarket(), true);
                if (price == null&&!"".equals(price)) {
                    bondInfoWithoutPrice.add(bondInfo);
                } else {
                    irrKeyValue(calculationRequest, bondInfo, keyValueList);
                }
            }
            deliverableBonds.removeAll(bondInfoWithoutPrice);
        }else{
            irrKeyValue(calculationRequest, bondInfos, keyValueList);
        }
        return keyValueList;
    }

    /**
     * 根据web端传的值 判断是基差、净基差还是Irr 从而转换成其对应的List<KeyValuePair>
     * @param calculationRequest
     * @param bondInfos
     * @param keyValueList
     */
    private void irrKeyValue(AbstractCalculationRequest calculationRequest, BondInfo bondInfos, List<KeyValuePair> keyValueList) {
        KeyValuePair setBondBasis=new KeyValuePair();
        String code=bondInfos.getBondCode();
        setBondBasis.setKey(code);
        if(calculationRequest.getBasis()!=null){
            setBondBasis.setValue(calculationRequest.getBasis());
        }else if(calculationRequest.getNetBasis()!=null){
            setBondBasis.setValue(calculationRequest.getNetBasis());
        }else{
            setBondBasis.setValue(null);
            keyValueList.remove(setBondBasis);
        }
        keyValueList.add(setBondBasis);
    }

    /**
     * 判断是未发国债列表还是 债劵列表并转换成 FixedRateBondInfo对象列表
     * @param bondInfo
     * @param scheduledBondInfos
     * @return
     */
    private List<FixedRateBondInfo> getFixedRateBondInfos(List<BondInfo> bondInfo,List<ScheduledBondInfo> scheduledBondInfos) {
        List<FixedRateBondInfo> deliverableBondInfos = new ArrayList<FixedRateBondInfo>();
        if(bondInfo!=null){
            convertFixedRateBondInfoByBondInfo(bondInfo, deliverableBondInfos);
        }
        if(scheduledBondInfos!=null){
            convertFixedRateBondInfoByScheduleBondInfo(scheduledBondInfos, deliverableBondInfos);
        }
        return deliverableBondInfos;
    }

    /**
     * 未发国债列表转 FixedRateBondInfo列表
     * @param scheduledBondInfos
     * @param deliverableBondInfos
     */
    private void convertFixedRateBondInfoByScheduleBondInfo(List<ScheduledBondInfo> scheduledBondInfos, List<FixedRateBondInfo> deliverableBondInfos) {
        for(ScheduledBondInfo bondInfos:scheduledBondInfos){
            FixedRateBondInfo fixedRateBondInfo = new FixedRateBondInfo();
            fixedRateBondInfo.setFixedCoupon(bondInfos.getCouponRate()!=null ? (new BigDecimal(0.01)).multiply(bondInfos.getCouponRate()): null);//除100
            fixedRateBondInfo.setTradeId(bondInfos.getId());
            fixedRateBondInfo.setMaturityDate(bondInfos.getMaturityDate());
            fixedRateBondInfo.setStartDate(bondInfos.getInterestStartDate());
            fixedRateBondInfo.setPaymentFrequency(bondInfos.getCouponType().getCalculatorEnumName());
            deliverableBondInfos.add(fixedRateBondInfo);
        }
    }

    /**
     * 债劵列表 转 FixedRateBondInfo列表
     * @param bondInfo
     * @param deliverableBondInfos
     */
    private void convertFixedRateBondInfoByBondInfo(List<BondInfo> bondInfo, List<FixedRateBondInfo> deliverableBondInfos) {
        for(BondInfo bondInfos:bondInfo){
            FixedRateBondInfo fixedRateBondInfo = new FixedRateBondInfo();
            fixedRateBondInfo.setFixedCoupon(bondInfos.getFixedCoupon()!=null?(new BigDecimal(0.01)).multiply(bondInfos.getFixedCoupon()):null);//除100
            fixedRateBondInfo.setTradeId(bondInfos.getBondCode());
            fixedRateBondInfo.setMaturityDate(bondInfos.getMaturityDate());
            fixedRateBondInfo.setStartDate(bondInfos.getInterestStartDate());
            fixedRateBondInfo.setPaymentFrequency(bondInfos.getPaymentFrequency()!=null?bondInfos.getPaymentFrequency().getCalculatorEnumName(): PaymenyFrequency.N.name());
            deliverableBondInfos.add(fixedRateBondInfo);
        }
    }


}
