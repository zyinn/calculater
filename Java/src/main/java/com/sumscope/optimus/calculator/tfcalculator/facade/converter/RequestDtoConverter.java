package com.sumscope.optimus.calculator.tfcalculator.facade.converter;

import com.sumscope.optimus.calculator.planalysis.commons.CalculatorExceptionType;
import com.sumscope.optimus.calculator.planalysis.commons.util.NumberUtil;
import com.sumscope.optimus.calculator.planalysis.model.dto.FutureContractDto;
import com.sumscope.optimus.calculator.planalysis.model.dto.RepoRateDto;
import com.sumscope.optimus.calculator.planalysis.model.qdp.BondYieldWithKeyValuePair;
import com.sumscope.optimus.calculator.shared.dao.CalculationDao;
import com.sumscope.optimus.calculator.shared.enums.BondPriceType;
import com.sumscope.optimus.calculator.shared.enums.YieldTypeEnum;
import com.sumscope.optimus.calculator.shared.gatewayinvoke.CDHFuturePriceGateway;
import com.sumscope.optimus.calculator.shared.model.dbmodel.BondInfo;
import com.sumscope.optimus.calculator.shared.model.dbmodel.FutureContract;
import com.sumscope.optimus.calculator.shared.model.dbmodel.ScheduledBondInfo;
import com.sumscope.optimus.calculator.shared.model.dto.BondConvertionFactorDto;
import com.sumscope.optimus.calculator.shared.model.dto.BondInfoDto;
import com.sumscope.optimus.calculator.shared.model.dto.BondPriceDto;
import com.sumscope.optimus.calculator.shared.model.dto.BondPrimaryKeyDto;
import com.sumscope.optimus.calculator.shared.service.CalculationDataService;
import com.sumscope.optimus.calculator.tfcalculator.commons.enums.CalculationTarget;
import com.sumscope.optimus.calculator.tfcalculator.commons.enums.TargetBonds;
import com.sumscope.optimus.calculator.tfcalculator.model.dbmodel.AbstractCalculationRequest;
import com.sumscope.optimus.calculator.tfcalculator.model.dbmodel.CalculationRequest;
import com.sumscope.optimus.calculator.tfcalculator.model.dbmodel.CalculationRequestForInit;
import com.sumscope.optimus.calculator.tfcalculator.model.dbmodel.CalculationRequestWithBondYieldType;
import com.sumscope.optimus.calculator.tfcalculator.model.dto.*;
import com.sumscope.optimus.calculator.tfcalculator.utils.JsonNodeUtils;
import com.sumscope.optimus.calculator.tfcalculator.utils.ValidateUtils;
import com.sumscope.optimus.commons.cachemanagement.annotation.CacheMe;
import com.sumscope.optimus.commons.exceptions.BusinessRuntimeException;
import com.sumscope.optimus.commons.exceptions.BusinessRuntimeExceptionType;
import com.sumscope.optimus.commons.util.JsonUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 根据前端传入的json字符串生成对应的RequestDto类实例。主要使用自动工具生成类实例。
 * <p>
 * 然而计算目标也决定了前端传递的参数类型，直接使用json转换工具自动转换请求字符串时，无法确定calculationMainResult的实际实例类型。因此需要使用convertCalculationmainRequestDto方法根据前端返回的计算目标编程实现转换对应目标类型。
 * <p>
 * 生成的Dto再根据所处场景，由本类调用相应的服务方法获取足够的信息参数生成用于QDP计算的请求对象。在金融计算器服务场景中，本类是实现主要的业务逻辑的功能模块之一。
 */
@Service
public class RequestDtoConverter{

    @Autowired
    private CalculationDataService calculationDataService;

    @Autowired
    private CDHFuturePriceGateway cdhFuturePriceGateway;

    @Autowired
    private CalculationDao calculationDao;
    @Autowired
    private CalculationRequestValidator calculationRequestValidator;

    @Autowired
    private FutureInitResquestValidator futureInitResquestValidator;

    @Autowired
    private GetFuturePriceRequestValidator getFuturePriceRequestValidator;

    @Autowired
    private  BondChangedRequestValidator bondChangedRequestValidator;
    private static final String LISTEDMARKET = "CIB";
    /**
     * 前端传回的主要请求Dto应放在请求的特殊字符串，比如“calculationMainRequest_Web”中，以支持编程方式生成合适的实例。MainRequestDto
     * 先获取计算目标，再根据计算目标调用Json自动转换工具生成对应的类实例。
     * <p>
     * 计算目标和Java类型的对应关系是：
     * IRR_BASE:IRRMainRequestDto
     * BOND_PRICE:BPMainRequestDto
     * FUTURE_THEORETICAL_PRICE:FTPMainRequestDto
     * FUTURE_ANALYSIS:FSAMainRequestDto
     */
    private CalculationMainRequest convertCalculationMainRequestDto(String jsonString) {
        return new IRRMainRequestDto();
    }

    /**
     * 转换json字符串为GetYieldRequestDto
     */
    private GetYieldRequestDto convertToGetYieldRequestDto(String jsonSting) {
        //校验参数操作
        String jsonString = JsonUtil.writeValueAsString(JsonNodeUtils.getJsonNode(jsonSting).get("GetYieldRequestDto"));
        return JsonUtil.readValue(jsonString, GetYieldRequestDto.class);
    }

    /**
     * 转换json字符串为BondChangedRequestDto
     */
    private BondChangedRequestDto convertToBondChangedRequestDto(String jsonSting) {
        String jsonString = JsonUtil.writeValueAsString(JsonNodeUtils.getJsonNode(jsonSting).get("bondChangedRequestDto"));
        return JsonUtil.readValue(jsonString, BondChangedRequestDto.class);
    }

    /**
     * 转换json字符串为FutureInitRequestDto
     */
    public FutureInitRequestDto convertToFutureInitRequestDto(String jsonSting) {
        String jsonString = JsonUtil.writeValueAsString(JsonNodeUtils.getJsonNode(jsonSting).get("futureInitRequestDto"));
        return JsonUtil.readValue(jsonString, FutureInitRequestDto.class);
    }

    /**
     * ScheduledBondInfo 转 BondInfo
     */
    private BondInfo scheduledBondsToBondInfo(ScheduledBondInfo scheduledBondInfo) {
        BondInfo bondInfo = new BondInfo();
        bondInfo.setBondCode(scheduledBondInfo.getId());
        bondInfo.setFixedCoupon(scheduledBondInfo.getCouponRate());
        bondInfo.setPaymentFrequency(scheduledBondInfo.getCouponType());
        bondInfo.setBondCode(scheduledBondInfo.getId());
        BeanUtils.copyProperties(scheduledBondInfo, bondInfo);
        return bondInfo;

    }

    private void setBondInfo(FutureInitRequestDto futureInitRequestDto, List<BondInfo> scheduledBonds, FutureContract futureContract) {
        //当前期货ID
        futureInitRequestDto.setFutureContract(futureContract);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //债卷信息Dto
        for (BondInfo bondInfo : scheduledBonds) {
            BondInfoDto bondInfoDto = new BondInfoDto();
            try {
                bondInfoDto.setTradeDate(simpleDateFormat.parse(bondInfo.getInterestStartDate()));
                bondInfoDto.setSettlementDate(simpleDateFormat.parse(bondInfo.getMaturityDate()));
            } catch (ParseException e) {
                throw new BusinessRuntimeException(CalculatorExceptionType.INPUT_PARAM_INVALID, "债劵起息日到息日转换错误"+e);
            }
            BeanUtils.copyProperties(bondInfo, bondInfoDto);
            futureInitRequestDto.setBondInfo(bondInfoDto);
            //收益率转换
//            BondPriceDto bondPriceDto = setBondPriceDto(bondInfoDto.getBondKey(), bondInfoDto.getListedMarket());
//            futureInitRequestDto.setBondPrice(bondPriceDto);
        }
    }

    //收益率转换
    private BondPriceDto setBondPriceDto(String bondKey, String listedMarket) {
        //listedMarket 默认值CIB
        BigDecimal ofrPrice = calculationDataService.getBondOfrLatestPrice(bondKey, listedMarket, true);
        //债卷价格，本类中仅取当前页面的债卷收益率
        BondPriceDto bondPriceDto = new BondPriceDto();
        BigDecimal yieldCdc=null;
       //设置收益率
        if(ofrPrice != null){
            bondPriceDto.setYield(ofrPrice);
        }else{
            yieldCdc=calculationDataService.getBondsCdcLatestPrice(bondKey, listedMarket);
            bondPriceDto.setYield(yieldCdc);
        }
        bondPriceDto.setYieldType(ofrPrice != null ? YieldTypeEnum.ofr : (yieldCdc!=null ? YieldTypeEnum.cdc: YieldTypeEnum.ofr));//默认ofr收益率 ofr没有的话取中债收益率
        return bondPriceDto;
    }


    /**
     * 转换json字符串为CalculationRequestDto
     */
    public CalculationRequestDto convertToCalculationRequestDto(String jsonSting) {
        String jsonString = JsonUtil.writeValueAsString(JsonNodeUtils.getJsonNode(jsonSting).get("calculationRequestDto"));
        return JsonUtil.readValue(jsonString, CalculationRequestDto.class);
    }

    /**
     * 转换json字符串为GetFuturePriceRequestDto
     */
    public GetFuturePriceRequestDto convertToGetFuturePriceRequestDto(String jsonSting) {
        String jsonString = JsonUtil.writeValueAsString(JsonNodeUtils.getJsonNode(jsonSting).get("GetFuturePriceRequestDto"));
        return JsonUtil.readValue(jsonString, GetFuturePriceRequestDto.class);
    }

    /**
     * 用户点击债券收益率那四个按钮触发
     *
     * @param jsonString
     * @return
     */
    public CalculationRequestWithBondYieldType convertGetYieldRequestDto(String jsonString) {
        CalculationRequestWithBondYieldType bondYieldType = new CalculationRequestWithBondYieldType();
        GetYieldRequestDto bondYieldRequest = convertToGetYieldRequestDto(jsonString);
        //验证
        checkDoGetBondYieldCalculation(bondYieldRequest);
        Map<String, FutureContract> map = calculationDataService.retrieveFutureContractByTfId();
        FutureContract futureContract = map.get(bondYieldRequest.getFutureContract()!=null? bondYieldRequest.getFutureContract().getTfId() : null);
        bondYieldRequest.setFutureContract(futureContract);
        if(bondYieldRequest.getCalculationMainRequest().getCalculationType().equals(CalculationTarget.FUTURE_ANALYSIS) ){
            double irr = JsonNodeUtils.getJsonNode(jsonString).get("MainRequestDto").get("irr").asDouble();
            bondYieldType.setIrr(NumberUtil.divide(new BigDecimal(irr),100));
        }
        BondInfo bondInfo = calculationDataService.getBondInfoByKeys(bondYieldRequest.getBondKey());
        bondYieldType.setBondPriceType(BondPriceType.YIELD);// TODO: 2016/9/5 暂时写死,等QDP接口完善好在删除.
        bondYieldType.setBond(bondInfo);
        bondYieldType.setFuturePrice(bondYieldRequest.getFuturePrice());
        bondYieldType.setCapitalCost(bondYieldRequest.getCapitalCost());
        bondYieldType.setFutureContract(bondYieldRequest.getFutureContract());
        bondYieldType.setCalculationTarget(bondYieldRequest.getCalculationMainRequest().getCalculationType());
        BigDecimal lastBondRate = null;
        lastBondRate = getBondRateAndBondYieldType(bondYieldType, bondYieldRequest, lastBondRate);
        if (lastBondRate == null) {
            throw new BusinessRuntimeException(BusinessRuntimeExceptionType.PARAMETER_INVALID, "今日暂无收益率，请选择其他收益率或输入收益率");
        } else {
            bondYieldType.setBondPrice(lastBondRate);
        }
        bondYieldType.setDistinguish("CalculationRequestWithBondYieldType");//设置标识 计算是CalculationRequest
        bondChangedRequestValidator.validateMaturityDate(bondYieldType);
        return bondYieldType;
    }

    private void checkDoGetBondYieldCalculation(GetYieldRequestDto bondYieldRequest){
        bondYieldRequest.setBondListedMarket(ValidateUtils.validate(bondYieldRequest.getBondListedMarket()));
        bondYieldRequest.setBondKey(ValidateUtils.validate(bondYieldRequest.getBondKey()));
    }

    /**
     * 获取债劵收益率 和 收益率类型
     *
     * @param bondYieldType
     * @param bondYieldRequest
     * @param lastBondRate
     * @return
     */
    private BigDecimal getBondRateAndBondYieldType(CalculationRequestWithBondYieldType bondYieldType, GetYieldRequestDto bondYieldRequest, BigDecimal lastBondRate) {
        switch (bondYieldRequest.getYieldType()) {
            case ofr:
                lastBondRate = calculationDataService.getBondOfrLatestPrice(bondYieldRequest.getBondKey(), bondYieldRequest.getBondListedMarket(), true);
                bondYieldType.setBondYieldType(YieldTypeEnum.ofr);

                if (lastBondRate == null) {
                    throw new BusinessRuntimeException(
                            BusinessRuntimeExceptionType.PARAMETER_INVALID, "今日无ofr，请选择其他收益率或输入收益率");
                }
                break;
            case bid:
                lastBondRate = calculationDataService.getBondBidLatestPrice(bondYieldRequest.getBondKey(), bondYieldRequest.getBondListedMarket(), true);
                bondYieldType.setBondYieldType(YieldTypeEnum.bid);
                if (lastBondRate == null) {
                throw new BusinessRuntimeException(
                        BusinessRuntimeExceptionType.PARAMETER_INVALID, "今日无bid，请选择其他收益率或输入收益率");
                }
                break;
            case deal:
                lastBondRate = calculationDataService.getBondDealLatestPrice(bondYieldRequest.getBondKey(), bondYieldRequest.getBondListedMarket());
                bondYieldType.setBondYieldType(YieldTypeEnum.deal);
                if (lastBondRate == null) {
                    throw new BusinessRuntimeException(
                            BusinessRuntimeExceptionType.PARAMETER_INVALID, "今日无成交，请选择其他收益率或输入收益率");
                }
                break;
            case cdc:
                bondYieldType.setBondYieldType(YieldTypeEnum.cdc);
                lastBondRate = calculationDataService.getBondsCdcLatestPrice(bondYieldRequest.getBondKey(), bondYieldRequest.getBondListedMarket());
        }
        return lastBondRate;
    }

    private BigDecimal getBondsCdcLatestPrice(CalculationRequestWithBondYieldType bondYieldType, GetYieldRequestDto bondYieldRequest) {
        BigDecimal lastBondRate;
        lastBondRate = calculationDataService.getBondsCdcLatestPrice(bondYieldRequest.getBondKey(), bondYieldRequest.getBondListedMarket());
        bondYieldType.setBondYieldType(YieldTypeEnum.cdc);
        return lastBondRate;
    }

    /**
     * 债劵改变RequestDto
     * @param jsonString
     * @return
     */
    public CalculationRequestWithBondYieldType convertBondChangedRequestDto(String jsonString) {
        CalculationRequestWithBondYieldType calculationRequest = new CalculationRequestWithBondYieldType();
        BondChangedRequestDto bondRequestDto = convertToBondChangedRequestDto(jsonString);
        Map<String, FutureContract> map = calculationDataService.retrieveFutureContractByTfId();
        //验证
        checkBondChangedRequestDto(bondRequestDto);
        FutureContract futureContracts = map.get(bondRequestDto.getFutureContract()!=null? bondRequestDto.getFutureContract().getTfId() : null);
        bondRequestDto.getFutureContract().setTfKey(futureContracts.getTfKey());
        bondChangedRequestValidator.validateParameter(bondRequestDto);
        bondChangedRequestValidator.validateRequest(bondRequestDto.getCalculationMainRequest(),jsonString);
        FutureContract futureContract = bondRequestDto.getFutureContract();
        String tfKey = getFutureContractByID(futureContract.getTfId()).getTfKey();
        futureContract.setTfKey(tfKey);
        BondInfo bondInfo=new BondInfo();

        calculationRequest.setCapitalCost(new BigDecimal(ValidateUtils.validate(bondRequestDto.getCapitalCost().toString())));//资金成本
        calculationRequest.setFutureContract(futureContract);//期货合约
        bondRequestDto.getTargetBonds().setDisplayName(ValidateUtils.validate(bondRequestDto.getTargetBonds().getDisplayName()));
       if(bondRequestDto.getTargetBonds()==TargetBonds.SCHEDULED_BONDS){
           //根据bondId 查询未发国债的bond信息
           if(bondRequestDto.getBondId()==null){
               throw new BusinessRuntimeException(BusinessRuntimeExceptionType.PARAMETER_INVALID, "该未发国债债劵信息不正确，不能参与计算！");
           }
            ScheduledBondInfo scheduledBondById = calculationDataService.getScheduledBondById(bondRequestDto.getBondId());
            bondInfo = scheduledBondsToBondInfo(scheduledBondById);
            calculationRequest.setBondPriceType(BondPriceType.YIELD);
            calculationRequest.setBondPrice(scheduledBondById.getYield());
            calculationRequest.setBondYieldType(scheduledBondById.getYieldType());//债劵收益率类型
           if(scheduledBondById.getYield()==null){
               throw new BusinessRuntimeException(BusinessRuntimeExceptionType.PARAMETER_INVALID, "该未发国债债劵暂无收益率，请手动输入收益率再计算");
           }
           converterCalculationRequest(calculationRequest, bondRequestDto, bondRequestDto.getBondKey(), futureContract, jsonString,bondInfo);
        }else{
            setConversionFactorDto(calculationRequest, bondRequestDto, futureContract);
            CalculationTarget calculationType = bondRequestDto.getCalculationMainRequest().getCalculationType();
            YieldTypeEnum yieldTypeEnum = converterCalculationRequest(calculationRequest, bondRequestDto, bondRequestDto.getBondKey(), futureContract, jsonString, bondInfo);
            if(calculationType==CalculationTarget.FUTURE_ANALYSIS || calculationType==CalculationTarget.FUTURE_THEORETICAL_PRICE){
                    BondPriceDto bondPriceDto = setBondPriceDto(bondRequestDto.getBondKey(), "CIB");//债劵价格
                    calculationRequest.setBondPriceType(BondPriceType.YIELD);
                    calculationRequest.setBondPrice(bondPriceDto.getYield());
                    calculationRequest.setBondYieldType(bondPriceDto.getYieldType());
             }else{
                calculationRequest.setBondYieldType(yieldTypeEnum);//债劵收益率类型bondPriceDto.getYieldType()
            }
        }
        calculationRequest.setDistinguish("CalculationRequestWithBondYieldType");//设置标识 计算是CalculationRequest
        if(bondRequestDto.getCalculationMainRequest().getCalculationType()==CalculationTarget.BOND_PRICE){
            calculationRequest.setBondPrice(null);
        }
        bondChangedRequestValidator.validateMaturityDate(calculationRequest);
        return calculationRequest;
    }

    private void checkBondChangedRequestDto( BondChangedRequestDto bondRequestDto){
        bondRequestDto.setBondKey(ValidateUtils.validate(bondRequestDto.getBondKey()));
        FutureContract futureContract = bondRequestDto.getFutureContract();
        ValidateUtils.validate(futureContract.getDeliveryDate().toString());
        MainRequestDto calculationMainRequest = bondRequestDto.getCalculationMainRequest();
        bondRequestDto.setListedMarket(ValidateUtils.validate(bondRequestDto.getListedMarket()));
        ValidateUtils.validate(calculationMainRequest.getCalculationType().getDisplayName());

    }
    /**
     * 根据用户选择的债券的信息,来确定是否是可交割卷,
     * 如果是可交割卷,转换因子信息从数据库中取出.否则由QDP接口提供
     * @param calculationRequest
     * @param bondRequestDto
     * @param futureContract
     */
    private void setConversionFactorDto(CalculationRequestWithBondYieldType calculationRequest, BondChangedRequestDto bondRequestDto, FutureContract futureContract) {
        List<BondInfo> deliverableBonds = calculationDataService.getDeliverableBonds(futureContract.getTfKey());//获取可交割券列表
        //取到所有的转换因子.
        List<BondConvertionFactorDto> conversionFactors = calculationDataService.getDeliverableBondConvertionFactor(futureContract.getTfKey());
        for (BondInfo bond: deliverableBonds) {
           if (bond.getBondKey().equals(bondRequestDto.getBondKey())) { //判断用户选择的债券是否是可交割卷
               for (BondConvertionFactorDto conversionFactor : conversionFactors) {
                   if (bondRequestDto.getBondKey().equals(conversionFactor.getBondKey())) {
                       calculationRequest.setConvertionFactorDto(conversionFactor);
                       break;
                   }
               }
           }
        }
    }

    /**
     * 期货init请求dto转换
     *
     * @param jsonString
     * @return
     */
    public CalculationRequestForInit convertFutureInitRequestDto(String jsonString) {
        CalculationRequestForInit calculationRequestForInit = new CalculationRequestForInit();
        FutureInitRequestDto futureInitRequestDto = convertToFutureInitRequestDto(jsonString);
        if (futureInitRequestDto == null) {
           throw new BusinessRuntimeException(BusinessRuntimeExceptionType.PARAMETER_INVALID, jsonString);
        }
        Map<String, FutureContract> map = calculationDataService.retrieveFutureContractByTfId();
        FutureContract futureContracts = map.get(futureInitRequestDto.getFutureContract()!=null? futureInitRequestDto.getFutureContract().getTfId() : null);
        futureInitRequestDto.getFutureContract().setTfKey(futureContracts.getTfKey());
        futureInitResquestValidator.validateFutureInitRequest(futureInitRequestDto.getCalculationMainRequest(),jsonString);
        //根据前端传回的期货主键获取期货信息
        FutureContract futureContract = getFutureContractByID(futureInitRequestDto.getFutureContract().getTfId());
        List<BondInfo> deliverableBonds = new ArrayList<>();
        List<BondConvertionFactorDto> conversionFactors = calculationDataService.getDeliverableBondConvertionFactor(futureContract.getTfKey());//根据期货key获取转换因子
        calculationRequestForInit.setDistinguish("CalculationRequestForInit");//设置标识 计算是calculationRequestForInit
        //计算目标 = 期货理论价格
        if (futureInitRequestDto.getCalculationMainRequest().getCalculationType().equals(CalculationTarget.FUTURE_THEORETICAL_PRICE)) {
            deliverableBonds = calculationDataService.getDeliverableBonds(futureContract.getTfKey());//根据期货key获取可交割券列表
            calculationRequestForInit.setCalculationTarget(CalculationTarget.FUTURE_THEORETICAL_PRICE);
            calculationRequestForInit.setCapitalCost(futureInitRequestDto.getCapitalCost());
            calculationRequestForInit.setFutureContract(futureContract);
            calculationRequestForInit.setSelectableBonds(deliverableBonds);
            calculationRequestForInit.setBondConvertionFactors(conversionFactors);
            calculationRequestForInit.setBondYieldWithKeyValuePair(getBondYieldWithKeyValuePair(deliverableBonds));
            return calculationRequestForInit;
        } else {
            //计算目标为 IRR/基差  债卷价格 期货场景分析时的requestDto组装
            if (futureInitRequestDto.getTargetBonds() == TargetBonds.DELIVABLE_BONDS){
                deliverableBonds = calculationDataService.getDeliverableBonds(futureContract.getTfKey());//根据期货key获取可交割券列表
            }
            doCalculationRequestDtoConverter(calculationRequestForInit, futureInitRequestDto, futureContract, deliverableBonds, conversionFactors);
            converterCalculationRequest(calculationRequestForInit, futureInitRequestDto, null, futureContract, jsonString,null);
            if(futureInitRequestDto.getTargetBonds()==TargetBonds.SCHEDULED_BONDS){
                calculationRequestForInit.setBond(null);}
            return calculationRequestForInit;
        }

    }

    /**
     * 获取收益率
     * @param deliverableBonds
     * @return
     */
    private List<BondYieldWithKeyValuePair> getBondYieldWithKeyValuePair(List<BondInfo> deliverableBonds){
        List<BondYieldWithKeyValuePair> list = new ArrayList<>();
        if(deliverableBonds!=null && deliverableBonds.size()>0){
            for(BondInfo deliverableBond: deliverableBonds){
                BondYieldWithKeyValuePair bondInfo = new BondYieldWithKeyValuePair();
                BigDecimal priceOfr =calculationDataService.getBondOfrLatestPrice(deliverableBond.getBondKey(), deliverableBond.getListedMarket(), true);
                if(priceOfr!=null){
                    bondInfo.setYieldTypeEnum(YieldTypeEnum.ofr);
                    bondInfo.setValue(NumberUtil.divide(priceOfr, 100));
                }else{
                    BigDecimal price=calculationDataService.getBondsCdcLatestPrice(deliverableBond.getBondKey(), deliverableBond.getListedMarket());
                    bondInfo.setYieldTypeEnum(YieldTypeEnum.cdc);
                    bondInfo.setValue(price!=null ? NumberUtil.divide(price, 100) : new BigDecimal(0));
                }
                bondInfo.setKey(deliverableBond.getBondCode());//.substring(0,code.indexOf(".IB"))
                list.add(bondInfo);
            }
        }
        return list;
    }

    private List<BondYieldWithKeyValuePair> getBondYieldWithKeyValuePairByScheduled(List<ScheduledBondInfo> scheduledBonds){
        List<BondYieldWithKeyValuePair> list = new ArrayList<>();
        if(scheduledBonds!=null && scheduledBonds.size()>0){
            for(ScheduledBondInfo scheduledBond: scheduledBonds){
                BondYieldWithKeyValuePair bondInfo = new BondYieldWithKeyValuePair();
                String bondKey = calculationDao.getBondKeyByTerm(scheduledBond.getDeadLine());
                BigDecimal bondDealLatestPrice = calculationDataService.getBondDealLatestPrice(bondKey, LISTEDMARKET);
                //未发国债的收益率默认取成交价
                if(bondDealLatestPrice!=null){
                    bondInfo.setYieldTypeEnum(YieldTypeEnum.deal);
                    bondInfo.setValue(NumberUtil.divide(bondDealLatestPrice, 100));
                }else{
                    BigDecimal cdc =calculationDataService.getBondsCdcLatestPrice(bondKey, LISTEDMARKET);
                    bondInfo.setYieldTypeEnum(YieldTypeEnum.cdc);
                    bondInfo.setValue(cdc!=null ? NumberUtil.divide(cdc, 100) : new BigDecimal(0));
                }
                bondInfo.setKey(scheduledBond.getId());
                list.add(bondInfo);
            }
        }
        return list;
    }

    /**
     * 计算目标为IRR/基差 债劵价格 期货场景分析
     *
     * @param calculationRequestForInit
     * @param futureInitRequestDto
     * @param futureContract
     * @param deliverableBonds
     * @param deliverableBondConvertionFactor
     */
    private void doCalculationRequestDtoConverter(CalculationRequestForInit calculationRequestForInit,FutureInitRequestDto futureInitRequestDto,
        FutureContract futureContract, List<BondInfo> deliverableBonds, List<BondConvertionFactorDto> deliverableBondConvertionFactor) {

        FutureContract futureContracts=new FutureContract();
        BeanUtils.copyProperties(futureContract,futureContracts);

        TargetBonds targetBonds = futureInitRequestDto.getTargetBonds();
        //设置期货合约和资金成本
        calculationRequestForInit.setCapitalCost(futureInitRequestDto.getCapitalCost());
        calculationRequestForInit.setFutureContract(getFutureContractByID(futureInitRequestDto.getFutureContract().getTfId()));

        //当前债卷窗口为可交割卷
        if (targetBonds == TargetBonds.DELIVABLE_BONDS) {
            calculationRequestForInit.setBondConvertionFactors(deliverableBondConvertionFactor);//转换因子
            //设置债卷信息Dto
            setBondInfo(futureInitRequestDto, deliverableBonds, futureContracts);
            calculationRequestForInit.setBondYieldWithKeyValuePair(getBondYieldWithKeyValuePair(deliverableBonds));
            calculationRequestForInit.setSelectableBonds(deliverableBonds);
        }

        //根据输入的期限获取对应的未发国债信息
        if (targetBonds == TargetBonds.SCHEDULED_BONDS) {
            if(futureContracts.getBondTerm()==0 ){
              futureContracts.setBondTerm(futureContracts.getTfId().contains("TF")==true? 5 : 10);
            }
            List<ScheduledBondInfo> scheduledBonds = calculationDataService.getScheduledBonds(futureContracts.getBondTerm());
            if(scheduledBonds==null || scheduledBonds.size()<=0){
                throw new BusinessRuntimeException(
                        BusinessRuntimeExceptionType.DATABASE_ERROR, "暂无未发国债债劵信息，如有疑问请联系管理员，谢谢配合！");
            }
            calculationRequestForInit.setBondYieldWithKeyValuePair(getBondYieldWithKeyValuePairByScheduled(scheduledBonds));
            calculationRequestForInit.setScheduledBondInfos(scheduledBonds);
        }

        //根据输入的期限获取对应的虚拟劵
        if (targetBonds == TargetBonds.VIRTUAL_BOND) {
            BondInfo bondInfo = new BondInfo();
            BeanUtils.copyProperties(futureInitRequestDto.getBondInfo(), bondInfo);
            calculationRequestForInit.setBond(bondInfo);//在虚拟劵中债券信息应设置全量
            calculationRequestForInit.setBondYieldType(futureInitRequestDto.getBondPrice()!=null?futureInitRequestDto.getBondPrice().getYieldType():null);
            calculationRequestForInit.setBondPriceType(futureInitRequestDto.getBondPrice().getYield()!=null?BondPriceType.YIELD:null);
            calculationRequestForInit.setBondPrice(futureInitRequestDto.getBondPrice()!=null?futureInitRequestDto.getBondPrice().getYield():null);
        }
    }

    /**
     * IRR窗口/债卷价格/期货场景分析 三种tab情况下 给abstractCalculationRequest赋值
     * @param abstractCalculationRequest
     * @param abstractTFCalculatorRequestDto
     * @param bondKey
     * @param futureContract
     * @param jsonString
     * @param bondInfo
     */
    private YieldTypeEnum converterCalculationRequest(AbstractCalculationRequest abstractCalculationRequest, AbstractTFCalculatorRequestDto abstractTFCalculatorRequestDto, String bondKey, FutureContract futureContract, String jsonString, BondInfo bondInfo) {
        CalculationTarget calculationTargets = abstractTFCalculatorRequestDto.getCalculationMainRequest().getCalculationType();
        abstractCalculationRequest.setBond((bondKey != null && !"".equals(bondKey)) ? calculationDataService.getBondInfoByKeys(bondKey) : bondInfo);
        //当前在IRR窗口下
        if (CalculationTarget.IRR_BASE==calculationTargets) {
            String irrMainRequest = JsonUtil.writeValueAsString(JsonNodeUtils.getJsonNode(jsonString).get("IRRMainRequestDto"));
            IRRMainRequestDto irrMainRequestDto =irrMainRequest=="null"? null : JsonUtil.readValue(irrMainRequest, IRRMainRequestDto.class);
            abstractCalculationRequest.setFuturePrice((irrMainRequestDto==null || irrMainRequestDto.getFuturePrice()==null)?
                    cdhFuturePriceGateway.getLatestFuturePrice(futureContract.getTfId()) : irrMainRequestDto.getFuturePrice());
            abstractCalculationRequest.setCalculationTarget(CalculationTarget.IRR_BASE);
            //一般计算要传收益率参加计算
            return getBondPriceDto(abstractCalculationRequest, bondKey);
        }
        if (CalculationTarget.BOND_PRICE==calculationTargets) {
            //债卷价格 窗口下
            abstractCalculationRequest.setCalculationTarget(CalculationTarget.BOND_PRICE);
            String bpMainRequestDto = JsonUtil.writeValueAsString(JsonNodeUtils.getJsonNode(jsonString).get("BPMainRequestDto"));
            convertBPMainRequestDto(abstractCalculationRequest, futureContract, bpMainRequestDto);
            return getBondPriceDto(abstractCalculationRequest, bondKey);
        }
        if (CalculationTarget.FUTURE_ANALYSIS==calculationTargets) {
            //期货情景分析
            abstractCalculationRequest.setCalculationTarget(CalculationTarget.FUTURE_ANALYSIS);
            String fsaMainRequestDto = JsonUtil.writeValueAsString(JsonNodeUtils.getJsonNode(jsonString).get("FSAMainRequestDto"));
            FSAMainRequestDto fsaMainRequest = JsonUtil.readValue(fsaMainRequestDto, FSAMainRequestDto.class);
            fsaMainRequest.setNetBasis(fsaMainRequest.getNetBasis()!=null ? new BigDecimal(ValidateUtils.validate(fsaMainRequest.getNetBasis().toString())) : null);
            abstractCalculationRequest.setIrr(fsaMainRequest.getIrr()!=null ? NumberUtil.divide(fsaMainRequest.getIrr(),100) : null);
            return null;
        }
        if(CalculationTarget.FUTURE_THEORETICAL_PRICE==calculationTargets){
            abstractCalculationRequest.setCalculationTarget(CalculationTarget.FUTURE_THEORETICAL_PRICE);
            return null;
        }
        return null;
    }

    /**
     *  BPMainRequestDto 转 abstractCalculationRequest
     * @param abstractCalculationRequest
     * @param futureContract
     * @param bpMainRequestDto1
     */
    private void convertBPMainRequestDto(AbstractCalculationRequest abstractCalculationRequest, FutureContract futureContract, String bpMainRequestDto1) {
        BPMainRequestDto bpMainRequestDto = JsonUtil.readValue(bpMainRequestDto1, BPMainRequestDto.class);
        if(bpMainRequestDto.getIrr()==null&&bpMainRequestDto.getBasis()==null&&bpMainRequestDto.getNetBasis()==null){
            throw new BusinessRuntimeException(BusinessRuntimeExceptionType.PARAMETER_INVALID,"IRR、基差、净基差都为空。请手动输入或稍后再试!");
        }
        abstractCalculationRequest.setIrr(bpMainRequestDto.getIrr()!=null ? NumberUtil.divide(bpMainRequestDto.getIrr(), 100) : null);
        abstractCalculationRequest.setBasis(bpMainRequestDto.getBasis()!=null ? bpMainRequestDto.getBasis() : null);
        abstractCalculationRequest.setNetBasis(bpMainRequestDto.getNetBasis()!=null ? new BigDecimal(ValidateUtils.validate(bpMainRequestDto.getNetBasis().toString())): null);
        abstractCalculationRequest.setFuturePrice(bpMainRequestDto.getFuturePrice() != null ? bpMainRequestDto.getFuturePrice() : cdhFuturePriceGateway.getLatestFuturePrice(futureContract.getTfId()));
    }

    /**
     * bond默认收益率
     * @param abstractCalculationRequest
     * @param bondKey
     */
    private YieldTypeEnum getBondPriceDto(AbstractCalculationRequest abstractCalculationRequest, String bondKey) {
        if(bondKey!=null &&!"".equals(bondKey)){
            BondPriceDto bondPriceDto = setBondPriceDto(bondKey, "CIB");
            abstractCalculationRequest.setBondPriceType(BondPriceType.YIELD);
            abstractCalculationRequest.setBondPrice(bondPriceDto.getYield());
            return bondPriceDto.getYieldType();
        }
        return null;
    }
    private void checkValidate(CalculationRequestDto calculationRequestDto ){
        calculationRequestDto.getBondInfo().setBondKey(ValidateUtils.validate(calculationRequestDto.getBondInfo().getBondKey()));
        calculationRequestDto.getBondInfo().setMaturityDate(ValidateUtils.validate(calculationRequestDto.getBondInfo().getMaturityDate()));
        calculationRequestDto.getBondInfo().setInterestStartDate(ValidateUtils.validate(calculationRequestDto.getBondInfo().getInterestStartDate()));
        //    calculationRequestDto.getBondInfo().setTradeDate(）;
    }
    /**
     * 转换web端发送的计算信息Dto至本地计算数据。注意，若是在虚拟卷窗口，债卷的信息应从Web端发送的数据中拷贝，而不是从数据库中获取。
     */
    public CalculationRequest convertCalculationRequestDto(String jsonString) {
        CalculationRequest calculationRequest = new CalculationRequest();
        CalculationRequestDto calculationRequestDto = convertToCalculationRequestDto(jsonString);
        //因为前端页面没有传对应的期货合约TfKey
        // 而用JsonUtil.readValue会给期货合约信息赋值为null的tfKey
        //一旦出错，缓存的期货合约tfKey也就一直为null,防止此现象发生，先根据tfId查询出期货合约然后再赋值
        Map<String, FutureContract> map = calculationDataService.retrieveFutureContractByTfId();
        String id=calculationRequestDto.getFutureContract().getTfId();
        FutureContract futureContract = map.get(id);
        calculationRequestDto.getFutureContract().setTfKey(futureContract.getTfKey());
        //验证
        checkValidate(calculationRequestDto);
        calculationRequestValidator.validateParameter(calculationRequestDto);
        calculationRequestValidator.validateRequest(calculationRequestDto.getCalculationMainRequest(),jsonString);
        String calculationTarget = convertCalculationUtils(calculationRequest, calculationRequestDto);
        calculationRequestValidator.validateCapitalCostDto(calculationRequestDto.getCapitalCost());
        calculationRequest.setCapitalCost(calculationRequestDto.getCapitalCost());

        //当前在IRR窗口下
        if (CalculationTarget.IRR_BASE.getCalculationTargetName().equals(calculationTarget)) {
            String irrMainRequestDto = JsonUtil.writeValueAsString(JsonNodeUtils.getJsonNode(jsonString).get("IRRMainRequestDto"));
            IRRMainRequestDto irrMainRequestDtos = JsonUtil.readValue(irrMainRequestDto, IRRMainRequestDto.class);
            calculationRequestValidator.validateIRRMainRequestDto(irrMainRequestDtos);
            calculationRequest.setFuturePrice(irrMainRequestDtos.getFuturePrice());
        } else if (CalculationTarget.BOND_PRICE.getCalculationTargetName().equals(calculationTarget)) {
            String bpMainRequestDto = JsonUtil.writeValueAsString(JsonNodeUtils.getJsonNode(jsonString).get("BPMainRequestDto"));
            BPMainRequestDto bpMainRequestDtos = JsonUtil.readValue(bpMainRequestDto, BPMainRequestDto.class);
            calculationRequestValidator.validateBPMainRequestDto(bpMainRequestDtos);
            calculationRequest.setFuturePrice(bpMainRequestDtos.getFuturePrice());
            calculationRequest.setIrr(bpMainRequestDtos.getIrr()!=null?NumberUtil.divide(bpMainRequestDtos.getIrr(),100):null);
            calculationRequest.setBasis(bpMainRequestDtos.getBasis()!=null?bpMainRequestDtos.getBasis():null);
            calculationRequest.setNetBasis(bpMainRequestDtos.getNetBasis()!=null?bpMainRequestDtos.getNetBasis():null);

        } else if(CalculationTarget.FUTURE_ANALYSIS.getCalculationTargetName().equals(calculationTarget)){
            String fsaMainRequestDto = JsonUtil.writeValueAsString(JsonNodeUtils.getJsonNode(jsonString).get("FSAMainRequestDto"));
            FSAMainRequestDto fsaResult = JsonUtil.readValue(fsaMainRequestDto,FSAMainRequestDto.class);
            calculationRequestValidator.validateFSAMainRequestDto(fsaResult);
            calculationRequest.setIrr(fsaResult.getIrr()!=null?NumberUtil.divide(fsaResult.getIrr(),100):null);
            calculationRequest.setBasis(fsaResult.getBasis()!=null?fsaResult.getBasis():null);
            calculationRequest.setNetBasis(fsaResult.getNetBasis()!=null?fsaResult.getNetBasis():null);

        }
        if(calculationRequestDto.getCalculationMainRequest().getCalculationType()==CalculationTarget.BOND_PRICE){
            calculationRequest.setBondPrice(null);
        }
        calculationRequest.setDistinguish("CalculationRequest");//设置标识 计算是CalculationRequest
        return calculationRequest;
    }




    /**
     * 设置 一般计算时的 futureContract  setBondPrice calculationTarget BondInfo等参数
     * @param calculationRequest
     * @param calculationRequestDto
     * @return
     */
    private String convertCalculationUtils(CalculationRequest calculationRequest, CalculationRequestDto calculationRequestDto) {
        BeanUtils.copyProperties(calculationRequestDto, calculationRequest);
        String calculationTarget = calculationRequestDto.getCalculationMainRequest().getCalculationType().getCalculationTargetName();
        setCalculationRequestBond(calculationRequest, calculationRequestDto);//债劵信息
        setBondPriceByBondPriceType(calculationRequest, calculationRequestDto);//设置债劵价格信息
        calculationRequest.setCalculationTarget(calculationRequestDto.getCalculationMainRequest().getCalculationType());
        if((calculationRequestDto.getBondInfo().getBondCode()==null||"".equals(calculationRequestDto.getBondInfo().getBondCode()))
            &&(calculationRequestDto.getBondInfo().getBondKey()==null||"".equals(calculationRequestDto.getBondInfo().getBondKey()))){
            calculationRequestDto.getBondInfo().setBondCode(UUID.randomUUID().toString().replace("-", ""));
        }
        return calculationTarget;
    }

    /**
     * 设置calculationRequest 中的债劵信息
     * @param calculationRequest
     * @param calculationRequestDto
     */
    private void setCalculationRequestBond(CalculationRequest calculationRequest, CalculationRequestDto calculationRequestDto) {
        if (calculationRequestDto.getBondInfo().getBondKey() != null) {
            validateMaturityDate(calculationRequestDto);
            BondInfo bondInfoByKeys = calculationDataService.getBondInfoByKeys(calculationRequestDto.getBondInfo().getBondKey());
            if(calculationRequestDto.getBondInfo()!=null){
                String maturityDate = calculationRequestDto.getBondInfo().getMaturityDate();
                String interestStartDate = calculationRequestDto.getBondInfo().getInterestStartDate();
                BigDecimal fixedCoupon = calculationRequestDto.getBondInfo().getFixedCoupon();
                bondInfoByKeys.setMaturityDate(maturityDate!=null?maturityDate : bondInfoByKeys.getMaturityDate());
                bondInfoByKeys.setInterestStartDate(interestStartDate!=null ? interestStartDate :bondInfoByKeys.getInterestStartDate());
                bondInfoByKeys.setFixedCoupon(fixedCoupon!=null ? fixedCoupon : bondInfoByKeys.getFixedCoupon());
                bondInfoByKeys.setBondCode((bondInfoByKeys==null || bondInfoByKeys.getBondCode()==null) ? UUID.randomUUID().toString().replace("-", ""): bondInfoByKeys.getBondCode());
            }
            calculationRequest.setBond(bondInfoByKeys);
        } else {
            //此种情况为未发国债和虚拟券时的情况
            if(calculationRequestDto.getBondInfo().getInterestStartDate()!=null&&calculationRequestDto.getBondInfo().getMaturityDate()!=null
                    &&calculationRequestDto.getBondInfo().getPaymentFrequency()!=null&&calculationRequestDto.getBondInfo().getFixedCoupon()!=null){
                validateMaturityDate(calculationRequestDto);
                BondInfo bondInfo = new BondInfo();
                BeanUtils.copyProperties(calculationRequestDto.getBondInfo(), bondInfo);
                bondInfo.setBondCode((bondInfo.getBondCode()!=null&&!"".equals(bondInfo.getBondCode()))?bondInfo.getBondCode():UUID.randomUUID().toString().replace("-", ""));//
                calculationRequest.setBond(bondInfo);
            }else{
                validateMaturityDate(calculationRequestDto);
                throw new BusinessRuntimeException(BusinessRuntimeExceptionType.PARAMETER_INVALID,"起息日/到期日/付息频率/票息率等参数不能为空！");
            }
        }

    }

    /**
     * 验证 到期日时候 是否大于 期货的交割日
     * @param calculationRequestDto
     */
    private void validateMaturityDate(CalculationRequestDto calculationRequestDto) {
        SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
        if(calculationRequestDto.getBondInfo().getMaturityDate()!=null){
            try {
                long maturityDate = sf.parse(calculationRequestDto.getBondInfo().getMaturityDate()).getTime();
                long deliveryDate=calculationRequestDto.getFutureContract().getDeliveryDate().getTime();
                long interestStartDate=sf.parse(calculationRequestDto.getBondInfo().getInterestStartDate()).getTime();
                if(interestStartDate>maturityDate){
                    throw new BusinessRuntimeException(BusinessRuntimeExceptionType.PARAMETER_INVALID,"债劵的到期日需大于起息日！");
                }
                if(deliveryDate>maturityDate){
                    throw new BusinessRuntimeException(BusinessRuntimeExceptionType.PARAMETER_INVALID,"债劵的到期日需大于期货的交割日！");
                }
            } catch (ParseException e) {
                throw new BusinessRuntimeException(BusinessRuntimeExceptionType.PARAMETER_INVALID,"期日格式不正确，请修改后再次计算！");
            }
        }
    }

    /**
     * 根据债劵枚举值 设置债劵价格信息
     * @param calculationRequest
     * @param calculationRequestDto
     */
    private void setBondPriceByBondPriceType(CalculationRequest calculationRequest, CalculationRequestDto calculationRequestDto) {
        if(calculationRequestDto.getCalculationMainRequest().getCalculationType()!=CalculationTarget.BOND_PRICE ){
            calculationRequestValidator.validateBondPriceDto(calculationRequestDto);
        }
        calculationRequest.setYieldTypeEnum(calculationRequestDto.getBondPrice().getYieldType());
        if (calculationRequestDto.getBondPriceType() == BondPriceType.YIELD) {
            calculationRequest.setBondPrice(calculationRequestDto.getBondPrice().getYield());
            calculationRequest.setBondPriceType(BondPriceType.YIELD);
        } else if (calculationRequestDto.getBondPriceType() == BondPriceType.NET_PRICE) {
            calculationRequest.setBondPrice(calculationRequestDto.getBondPrice().getNetPrice());
            calculationRequest.setBondPriceType(BondPriceType.NET_PRICE);
        } else if (calculationRequestDto.getBondPriceType() == BondPriceType.FULL_PRICE) {
            calculationRequest.setBondPrice(calculationRequestDto.getBondPrice().getFullPrice());
            calculationRequest.setBondPriceType(BondPriceType.FULL_PRICE);
        } else {
            if(calculationRequestDto.getBondInfo().getBondKey()!=null&&!"".equals(calculationRequestDto.getBondInfo().getBondKey())){
                BigDecimal bondOfrLatestPrice = calculationDataService.getBondOfrLatestPrice(calculationRequestDto.getBondInfo().getBondKey(), calculationRequestDto.getBondInfo().getListedMarket(), true);
                calculationRequest.setBondPrice(bondOfrLatestPrice);
                calculationRequest.setBondPriceType(BondPriceType.YIELD);
            }else{
                //虚拟券情况
                calculationRequest.setBondPrice(calculationRequestDto.getBondPrice().getYield());
                calculationRequest.setBondPriceType(BondPriceType.YIELD);
            }
            if(calculationRequestDto.getBondPrice().getYield()!=null){
                calculationRequest.setBondPrice(calculationRequestDto.getBondPrice().getYield());
                calculationRequest.setBondPriceType(BondPriceType.YIELD);
            }
        }
        if(calculationRequest.getBondPrice()==null && calculationRequestDto.getBondPrice()!=null  && calculationRequestDto.getBondPrice().getYield()!=null){
            calculationRequest.setBondPrice(calculationRequestDto.getBondPrice().getYield());
            calculationRequest.setBondPriceType(BondPriceType.YIELD);
        }
    }

    public CalculationRequest convertGetFuturePriceRequestDto(String jsonString) {
        CalculationRequest clr = new CalculationRequest();
        GetFuturePriceRequestDto getFuturePriceRequestDto = convertToGetFuturePriceRequestDto(jsonString);
        //验证
        getFuturePriceRequestDto.getBondInfo().setBondKey(ValidateUtils.validate(getFuturePriceRequestDto.getBondInfo().getBondKey()));
        getFuturePriceRequestDto.getBondInfo().setMaturityDate(ValidateUtils.validate(getFuturePriceRequestDto.getBondInfo().getMaturityDate()));
        Map<String, FutureContract> map = calculationDataService.retrieveFutureContractByTfId();
        FutureContract futureContracts = map.get(getFuturePriceRequestDto.getFutureContract()!=null? getFuturePriceRequestDto.getFutureContract().getTfId() : null);
        futureContracts.setTradingDate(getFuturePriceRequestDto.getFutureContract()!=null?getFuturePriceRequestDto.getFutureContract().getTradingDate():futureContracts.getTradingDate());
        futureContracts.setDeliveryDate(getFuturePriceRequestDto.getFutureContract()!=null?getFuturePriceRequestDto.getFutureContract().getDeliveryDate():futureContracts.getDeliveryDate());
        getFuturePriceRequestDto.setFutureContract(futureContracts);
        getFuturePriceRequestValidator.validateGetFuturePriceRequest(getFuturePriceRequestDto.getCalculationMainRequest(),jsonString);
        BondInfo bondInfo = null;
        //获取用户所选中的期货合约
        BigDecimal latestFuturePrice = cdhFuturePriceGateway.getLatestFuturePrice(getFuturePriceRequestDto.getFutureContract().getTfId()); //获取最后更新的期货价格
        if(getFuturePriceRequestDto.getTargetBonds().equals(TargetBonds.SCHEDULED_BONDS)){
            if(getFuturePriceRequestDto.getScheduleBondId()==null){
                throw new BusinessRuntimeException(
                        BusinessRuntimeExceptionType.DATABASE_ERROR, "暂无未发国债债劵信息，如有疑问请联系管理员，谢谢配合！");
            }
            ScheduledBondInfo scheduledBond = calculationDataService.getScheduledBondById(getFuturePriceRequestDto.getScheduleBondId());
            bondInfo = scheduledBondsToBondInfo(scheduledBond);
            bondInfo.setFixedCoupon(getFuturePriceRequestDto.getFixedCoupon());
        }else if(getFuturePriceRequestDto.getTargetBonds().equals(TargetBonds.VIRTUAL_BOND)){
             bondInfo =  getFuturePriceRequestDto.getBondInfo();
             bondInfo.setBondCode(UUID.randomUUID().toString().replace("-",""));
        }else {
            bondInfo = calculationDataService.getBondInfoByKeys(getFuturePriceRequestDto.getBondInfo().getBondKey()); //获取用户选中的债券信息.
        }

        if(getFuturePriceRequestDto.getCalculationMainRequest().getCalculationType().equals(CalculationTarget.BOND_PRICE)){
            String bpMainRequest = JsonUtil.writeValueAsString(JsonNodeUtils.getJsonNode(jsonString).get("BPMainRequestDto"));
            BPMainRequestDto bpMainRequestDto = JsonUtil.readValue(bpMainRequest, BPMainRequestDto.class);
            clr.setIrr(NumberUtil.divide(bpMainRequestDto.getIrr(),100));
        }
        clr.setFutureContract(getFuturePriceRequestDto.getFutureContract());
        clr.setBond(bondInfo);
        clr.setBondPrice(getFuturePriceRequestDto.getBondYield());
        clr.setCapitalCost(getFuturePriceRequestDto.getCapitalCost());
        clr.setFuturePrice(latestFuturePrice);
        clr.setBondPriceType(BondPriceType.YIELD);
        clr.setCalculationTarget(getFuturePriceRequestDto.getCalculationMainRequest().getCalculationType());
        clr.setDistinguish("CalculationRequest");//设置标识 计算是CalculationRequest
        return clr;
    }



    //转换 页面初始化请求需要的参数.
    public CalculationRequestForInit convertInitialPageRequest(FutureContract futureContract, BigDecimal defaultCapitalCost) {
        CalculationRequestForInit calculationRequestForInit = new CalculationRequestForInit();
        BigDecimal latestFuturePrice = cdhFuturePriceGateway.getLatestFuturePrice(futureContract.getTfId());
        List<BondInfo> deliverableBonds = calculationDataService.getDeliverableBonds(futureContract.getTfKey()); //获取期货合约的可交割卷.
        if(deliverableBonds==null ||deliverableBonds.size()==0){
            throw new BusinessRuntimeException(BusinessRuntimeExceptionType.OTHER,"没有任何债券信息,请联系管理员!");
        }
        List<BondConvertionFactorDto> conversionFactor = calculationDataService.getDeliverableBondConvertionFactor(futureContract.getTfKey());
        calculationRequestForInit.setFuturePrice(latestFuturePrice);
        calculationRequestForInit.setCapitalCost(defaultCapitalCost);
        calculationRequestForInit.setFutureContract(futureContract);
        calculationRequestForInit.setSelectableBonds(deliverableBonds);
        calculationRequestForInit.setBondConvertionFactors(conversionFactor);
        calculationRequestForInit.setCalculationTarget(CalculationTarget.IRR_BASE);
        calculationRequestForInit.setBondYieldWithKeyValuePair(getBondYieldWithKeyValuePair(deliverableBonds));
        calculationRequestForInit.setDistinguish("CalculationRequestForInit");//设置标识 计算是calculationRequestForInit
        return calculationRequestForInit;
    }


    /**
     * 根据期货ID获取期货信息
     */
    @CacheMe(timeout = 3600)
    public FutureContract getFutureContractByID(String tfId) {
        FutureContract futureContract = null;
        List<FutureContract> list = calculationDataService.retrieveFutureContracts();
        if (list != null && list.size() > 0) {
            futureContract = getFutureContractBasedOnID(tfId, list);
        }
        return futureContract;
    }

    /**
     * 获取默认期货数据列表
     */
    @CacheMe(timeout = 3600)
    public List<FutureContract> getDefaultFutureContract() {
        List<FutureContract> futureContracts = calculationDataService.retrieveFutureContracts();
        List<FutureContract>  fc=new ArrayList<>();
        fc.addAll(futureContracts);
        List<FutureContract> futureContract = getFutureContract(fc);
        return futureContract;
    }
    public  List<FutureContract> getFutureContract(List<FutureContract> futureContracts){
        List<FutureContract> list=new ArrayList<>();
        list.addAll(futureContracts);
        List<FutureContract> fc=new ArrayList<>();
        if(list!=null && list.size()>0){
            for (int i=0;i<list.size();i++) {
                if (list.get(i).getTfId()!=null && "TF".equals(list.get(i).getTfId().substring(0, 2))) {
                    fc.add(list.get(i));
                    list.remove(list.get(i));
                    i--;
                }
            }
            FutureContract futureContract = list.get(0);
            list.remove(futureContract);
            list.add(0,futureContract);
            list.addAll(1,fc);
        }
        return  list;
    }
    //获取页面初始化默认选中的期货合约 默认五年期国债
    public FutureContract getInitalFutureContract(List<FutureContract> futureContracts) {
        List<FutureContract> futureContract = getFutureContract(futureContracts);
        return futureContract!=null ? futureContract.get(0) : null;
    }

    @CacheMe(timeout = 3600)
    public BigDecimal getInitalRepoRateDto(List<RepoRateDto> repoRateDtos) {
        int index = 0;
        for (RepoRateDto repoRateDto : repoRateDtos) {
            if (repoRateDto.getCode().equals("FR007")) {
                repoRateDtos.remove(repoRateDtos.get(index));
                repoRateDtos.add(0, repoRateDto); //将默认的那个资金成本放在第一位
                return repoRateDtos.get(0).getPrice();
            }
            ++index;
        }
        return null;
    }

    /**
     * 根据输入的期货列表和目标期货的ID，从中选择对应的期货数据。
     */
    private FutureContract getFutureContractBasedOnID(String tfID, List<FutureContract> futureContracts) {
        for (FutureContract futureContract : futureContracts) {
            if (futureContract.getTfId().equals(tfID)) {
                return futureContract;
            }
        }
        return null;
    }

    public List<BondInfoDto> getBondInfosByUserInputParams(String inputParams) {
        List<BondInfoDto> bondInfoDtos = new ArrayList<>();
        List<BondPrimaryKeyDto> bondInfos = calculationDataService.findBondsByNamePrefix(inputParams);
        if (bondInfos != null && bondInfos.size() > 0) {
            for (BondPrimaryKeyDto bondInfo : bondInfos) {
                BondInfoDto bondInfoDto = new BondInfoDto();
                BeanUtils.copyProperties(bondInfo, bondInfoDto);
                bondInfoDtos.add(bondInfoDto);
            }
            return bondInfoDtos;
        }
        return new ArrayList<BondInfoDto>();
    }
}
