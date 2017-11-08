package com.sumscope.optimus.calculator.planalysis.service;

import com.sumscope.optimus.calculator.planalysis.model.qdp.*;
import com.sumscope.optimus.calculator.planalysis.model.qdp.resp.QbTfScenarioResult;
import com.sumscope.optimus.calculator.shared.dao.CalculationDao;
import com.sumscope.optimus.calculator.shared.model.dbmodel.BondCDCValuationInfo;
import com.sumscope.optimus.calculator.tfcalculator.facade.converter.RequestDtoConverter;
import com.sumscope.optimus.calculator.tfcalculator.utils.ValidateUtils;
import com.sumscope.optimus.commons.cachemanagement.annotation.CacheMe;
import com.sumscope.optimus.commons.exceptions.BusinessRuntimeException;
import com.sumscope.optimus.commons.exceptions.BusinessRuntimeExceptionType;
import com.sumscope.optimus.commons.util.JsonUtil;
import com.sumscope.optimus.calculator.planalysis.commons.CalculatorExceptionType;
import com.sumscope.optimus.calculator.planalysis.commons.enums.YieldTypeEnum;
import com.sumscope.optimus.calculator.planalysis.commons.util.DateDifferentUtil;
import com.sumscope.optimus.calculator.planalysis.commons.util.NumberUtil;
import com.sumscope.optimus.calculator.planalysis.commons.util.SortUtil;
import com.sumscope.optimus.calculator.planalysis.dao.BasisAnalysisDao;
import com.sumscope.optimus.calculator.planalysis.gateway.RedisGatewayService;
import com.sumscope.optimus.calculator.planalysis.gateway.RepoRateGatewayService;
import com.sumscope.optimus.calculator.planalysis.gateway.RestService;
import com.sumscope.optimus.calculator.planalysis.model.dbmodel.BondInfo;
import com.sumscope.optimus.calculator.planalysis.model.dto.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.math.BigDecimal.ROUND_FLOOR;
import static java.math.BigDecimal.ROUND_HALF_UP;

/**
 * Created by simon.mao on 2016/4/27.
 * BasisAnalysisService 接口的实现类
 */
@Service
public class BasisAnalysisServiceImpl implements BasisAnalysisService {
    @Value("${calculator.url}")
    public String url;

    private static final String FR007 = "FR007";
    private static Logger logger = LogManager.getLogger(BasisAnalysisServiceImpl.class.getName());
    Date date1;
    @Autowired
    private BasisAnalysisDao basisAnalysisDao;
    @Autowired
    private RepoRateGatewayService repoRateGatewayService;
    @Autowired
    private RedisGatewayService redisGatewayService;
    @Autowired
    private RestService restService;
    @Autowired
    private CalculationDao calculationDao;

    @Override
    public ResponseInititialBasisDto inititialRequest(String params) {
        ResponseInititialBasisDto result = new ResponseInititialBasisDto();
        date1 = new Date(System.currentTimeMillis());
        getBasicData(result);
        // 获取期货合约
        FutureContractDto futureContractDto;
        if (params == null) {
            futureContractDto = result.getFutureContracts().get(0);
        } else {
            futureContractDto = getFutureContract(result.getFutureContracts(), params, "tfId");
        }
        // 获取指定期货合约的期货价格
        BigDecimal futurePrice = getFuturePrice(futureContractDto.getTfId());
        // 获取默认资金成本（FR007）
        BigDecimal capitalCost = result.getRepoRateDto().getPrice().setScale(3,ROUND_FLOOR);

        RequestDto requestDto = new RequestDto();
        requestDto.setFutureContract(futureContractDto);
        requestDto.setFuturePrice(futurePrice);
        requestDto.setCapitalCost(capitalCost);
        requestDto.setOpenPositionDate(futureContractDto.getStartDate());
        requestDto.setClosePositionDate(futureContractDto.getMaturityDate());
        ResponseDto responseDto = calculateWithDeliverableBonds(requestDto);
        responseDto.setYieldLastUpdateTime(DateDifferentUtil.parseDateToString(new Date()));
        responseDto.setFutureContract(futureContractDto);
        result.setResponseDto(responseDto);
        result.setOpenPositionDate(futureContractDto.getStartDate());
        result.setClosePositionDate(futureContractDto.getMaturityDate());
        return result;
    }

    @Override
    public ResponseDto futureContractPriceUpdate(RequestDto requestDto) {
        // 获取期货合约及最新价格
        getFutureContractDto(requestDto);
        BigDecimal futurePrice = getFuturePrice(requestDto.getFutureContract().getTfId());
        requestDto.setFuturePrice(futurePrice);
        return calculateWithCtdAndCurrentBonds(requestDto);
    }

    private void throwException(RequestDto requestDto, String info) {
        logger.info(requestDto.getBondKey() + " " + requestDto.getBondListedMarket() + " " + info);
        throw new BusinessRuntimeException(BusinessRuntimeExceptionType.PARAMETER_INVALID, info);
    }

    @Override
    public ResponseDto yieldTypeUpdate(RequestDto requestDto) {
        checkRequestDtoValidate(requestDto);
        clearBondPrice(requestDto);
        // 获取期货合约
        getFutureContractDto(requestDto);

        YieldTypeEnum yieldType = requestDto.getYieldType();
        BigDecimal latestYield = null;
        String bondKey = requestDto.getBondKey();
        String bondListedMarket = requestDto.getBondListedMarket();

        Boolean isCdcYieldLastUpdateTime = false;
        switch (yieldType) {
            case ofr:
                latestYield = calculationDao.getBondOfrLatestPrice(bondKey, bondListedMarket, false);
                if (latestYield == null) {
                    latestYield = getBondCDCYield(bondKey, bondListedMarket);
                    isCdcYieldLastUpdateTime = true;
                }
                break;
            case bid:
                latestYield = calculationDao.getBondBidLatestPrice(bondKey, bondListedMarket, false);
                if (latestYield == null) {
                    latestYield = getBondCDCYield(bondKey, bondListedMarket);
                    isCdcYieldLastUpdateTime = true;
                }
                break;
            case deal:
                latestYield = calculationDao.getBondDealLatestPrice(bondKey, bondListedMarket,false);
                if (latestYield == null) {
                    latestYield = getBondCDCYield(bondKey, bondListedMarket);
                    isCdcYieldLastUpdateTime = true;
                }
                break;
            case cdc:
                latestYield = getBondCDCYield(bondKey, bondListedMarket);
                break;
        }
        if (latestYield == null) {
            throwException(requestDto, yieldType + "最新收益率为null");
        }

        requestDto.setYield(latestYield);
        ResponseDto responseDto = calculateWithCtdAndCurrentBonds(requestDto);
        responseDto.setYieldLastUpdateTime(DateDifferentUtil.parseDateToString(new Date()));
        responseDto.setIsCdcYieldLastUpdateTime(isCdcYieldLastUpdateTime);
        return responseDto;
    }

    public BigDecimal getBondCDCYield(String bondKey, String bondListedMarket) {
        return calculationDao.getBondsCdcLatestPrice(bondKey,bondListedMarket).setScale(4,ROUND_HALF_UP);
    }

    private void clearBondPrice(RequestDto requestDto) {
        requestDto.setYield(null);
        requestDto.setBondNetPrice(null);
        requestDto.setBondFullPrice(null);
    }

    @Override
    public ResponseDto paramUpdate(RequestDto requestDto) {
        checkRequestDtoValidate(requestDto);
        // 获取期货合约
        getFutureContractDto(requestDto);
        return calculateWithCtdAndCurrentBonds(requestDto);
    }

    private void getFutureContractDto(RequestDto requestDto) {
        FutureContractDto futureContractDto = getFutureContract(basisAnalysisDao.retrieveFutureContracts(), requestDto.getFutureContract().getTfKey(), "tfKey");
        requestDto.setFutureContract(futureContractDto);
    }
    private void checkRequestDtoValidate(RequestDto requestDto){
        String startDate=requestDto.getFutureContract().getStartDate();
        if (startDate == null) {
            throw new BusinessRuntimeException(BusinessRuntimeExceptionType.PARAMETER_INVALID, "startDate不合法");
        }
        FutureContractDto futureContract=new FutureContractDto();
        BeanUtils.copyProperties(requestDto.getFutureContract(),futureContract);
        futureContract.setStartDate(ValidateUtils.validate(requestDto.getFutureContract().getStartDate()));
        requestDto.setFutureContract(futureContract);

        if (requestDto.getBondKey() == null) {
            throw new BusinessRuntimeException(BusinessRuntimeExceptionType.PARAMETER_INVALID, "startDate不合法");
        }
        requestDto.setCtdBondKey(ValidateUtils.validate(requestDto.getCtdBondKey()));
        requestDto.setBondKey(ValidateUtils.validate(requestDto.getBondKey()));
        requestDto.setBondListedMarket(ValidateUtils.validate(requestDto.getBondListedMarket()));
    }
    /**
     * 调用计算接口计算当前期货及其可交割卷的盈亏分析，需要计算CTD卷
     *
     * @param requestDto RequestDto
     * @return 盈亏分析数值
     */
    private ResponseDto calculateWithDeliverableBonds(RequestDto requestDto) {
        // 获取可交割卷进行盈亏分析计算

        List<BondInfo> deliverableBonds = getDeliverableBonds(requestDto.getFutureContract().getTfKey());

        if (deliverableBonds == null || deliverableBonds.size() == 0) {
//            当可交割卷无法取到时，直接返回空结果。因为这个情况应该不能出现。若出现保证前端页面能够继续使用
            return new ResponseDto();
        }
        //获取所选期货合约所对应的银行间市场的可交割券的Ofr卖价收益率
        List<KeyValuePair> keyValuePairs = getYieldByDeliverableBonds(deliverableBonds);

        return convertToQdp(requestDto, deliverableBonds, keyValuePairs);
    }

    private void getCtdBondKeyAndListedMarket(ResponseDto responseDto, List<BondInfo> deliverableBonds) {
        for (BondInfo bondInfo : deliverableBonds) {
            if (bondInfo.getBondCode().equals(responseDto.getCtdBondCode())) {
                responseDto.setCtdBondKey(bondInfo.getBondKey());
                responseDto.setCtdListedMarket(bondInfo.getListedMarket());
                responseDto.setCtdDuration(getBondCDCDuration(bondInfo.getBondKey(),bondInfo.getListedMarket()));
                break;
            }
        }
        for (BondInfo bondInfo : deliverableBonds) {
            if (bondInfo.getBondCode().equals(responseDto.getBondCode())) {
                responseDto.setBondKey(bondInfo.getBondKey());
                responseDto.setBondListedMarket(bondInfo.getListedMarket());
                responseDto.setCdcDuration(getBondCDCDuration(bondInfo.getBondKey(),bondInfo.getListedMarket()));
                break;
            }
        }
    }
    @CacheMe(timeout = 3600)
    private BigDecimal getBondCDCDuration(String bondKey, String bondListedMarket){
        //设置从数据库读取的期货久期数据
       BondCDCValuationInfo bondsCdcLatestInfo = calculationDao.getBondsCdcLatestInfo(bondKey, bondListedMarket);
        if(bondsCdcLatestInfo != null){
            return NumberUtil.roundHalfUp(bondsCdcLatestInfo.getModifiedDuration(), 4);
        }
        return null;
    }

    /**
     * 调用计算接口进行盈亏分析，所有参数应该已经都设置完毕
     *
     * @param requestDto RequestDto
     * @return ResponseDto
     */
    private ResponseDto calculateWithCtdAndCurrentBonds(RequestDto requestDto) {
        // 获取CTD卷相关信息：
        BondInfo ctdBondInfo = basisAnalysisDao.getBondInfo(requestDto.getCtdBondKey());
        // 获取现卷相关信息：
        BondInfo currentBondInfo = basisAnalysisDao.getBondInfo(requestDto.getBondKey());

        // 现卷到期日应晚于平常日
        checkBondMaturityDate(currentBondInfo.getMaturityDate(), requestDto.getClosePositionDate());

        return convertToQdp(requestDto, ctdBondInfo, currentBondInfo);
    }
    @CacheMe(timeout = 3600)
    private void checkBondMaturityDate(String maturityDate, String closePositionDate) {
        Date bondMaturityDate = DateDifferentUtil.parseNumericalDateString(maturityDate);
        Date closePDate = DateDifferentUtil.parseSimpleDateWithoutTimeString(closePositionDate);
        Calendar bondCalendar = Calendar.getInstance();
        bondCalendar.setTime(bondMaturityDate);
        Calendar calculationCanlendar = Calendar.getInstance();
        calculationCanlendar.setTime(closePDate);
        if (calculationCanlendar.getTimeInMillis() > bondCalendar.getTimeInMillis()) {
            SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd");
            String bondMDString = format.format(bondMaturityDate);
            throw new BusinessRuntimeException(CalculatorExceptionType.INPUT_PARAM_INVALID, "所选现卷到期日早于平仓日！请另选。当前现卷到期日为：" + bondMDString);
        }

    }

    private ResponseDto calculateAndGenerateResponse(QdpAnalysis qdp, RequestDto requestDto) {
        String json = JsonUtil.writeValueAsString(qdp);
//        System.out.println("sent:  " + json);
        String response = restService.sendData(json);
        if (response == null) {
            throw new BusinessRuntimeException(CalculatorExceptionType.INPUT_PARAM_INVALID, "输入的参数超出边界值，请重新输入。");
        }
//        System.out.println("receive:  " + response);
        QbTfScenarioResult qbTfScenarioResult = JsonUtil.readValue(response, QbTfScenarioResult.class);
        ResponseDto responseDto = convertToResponseDto(qbTfScenarioResult);

        int days = DateDifferentUtil.daysBetween(requestDto.getOpenPositionDate(), requestDto.getClosePositionDate());
        responseDto.setHoldingPeriod(days);
        responseDto.setFutureLastUpdateTime(DateDifferentUtil.parseDateToString(new Date()));
        responseDto.setFuturePrice(NumberUtil.roundHalfUp(requestDto.getFuturePrice(), 4));

        return responseDto;
    }

    private void getBasicData(ResponseInititialBasisDto result) {
        // 获取所有期货列表
        List<FutureContractDto> futureContracts = basisAnalysisDao.retrieveFutureContracts();
        List<FutureContractDto> initalFutureContract = getInitalFutureContract(futureContracts);
        result.setFutureContracts(initalFutureContract);
        // 获取所有资金成本数据
        List<RepoRateDto> repoRateTypes = getRepoRateTypes();
        result.setRepoRateTypes(repoRateTypes);
        // 获取默认资金成本
        RepoRateDto repoRateDto = getDefaultRepoRateDto(repoRateTypes);
        result.setRepoRateDto(repoRateDto);
    }

    //获取页面初始化默认选中的期货合约 默认五年期国债
    public List<FutureContractDto> getInitalFutureContract(List<FutureContractDto> futureContracts) {
        List<FutureContractDto> fcDto=new ArrayList<>();
        fcDto.addAll(futureContracts);
        List<FutureContractDto> fc=new ArrayList<>();
        if(fcDto!=null && fcDto.size()>0){
            for (int i=0;i<fcDto.size();i++) {
                if (fcDto.get(i).getTfId()!=null && "TF".equals(fcDto.get(i).getTfId().substring(0, 2))) {
                    fc.add(fcDto.get(i));
                    fcDto.remove(fcDto.get(i));
                    i--;
                }
            }
            FutureContractDto futureContractDto = fcDto.get(0);
            if(futureContractDto!=null){
                fcDto.remove(futureContractDto);
                fcDto.add(0,futureContractDto);
            }
            fcDto.addAll(1,fc);
        }
        return fcDto;
    }

    private ResponseDto convertToResponseDto(QbTfScenarioResult qbTfScenarioResult) {
        if (qbTfScenarioResult == null) {
            logger.error("计算器计算结果转换对象出错");
        }

        ResponseDto responseDto = new ResponseDto();
        responseDto.setCtdFullPrice(NumberUtil.roundHalfUp(qbTfScenarioResult.getCtdDirtyPrice(), 4));
        responseDto.setCtdBondCode(qbTfScenarioResult.getCtdId());
        responseDto.setCtdDuration(NumberUtil.roundHalfUp(qbTfScenarioResult.getCtdModifiedDuration(), 4));
        responseDto.setBasis(NumberUtil.roundHalfUp(qbTfScenarioResult.getFuturesBasis(), 4));
        responseDto.setIrr(NumberUtil.roundHalfUp(NumberUtil.multiply(qbTfScenarioResult.getFuturesIrr(), 100), 4));
        responseDto.setFutureNumber((qbTfScenarioResult.getFuturesLot()));
        responseDto.setNetBasis(NumberUtil.roundHalfUp(qbTfScenarioResult.getFuturesNetBasis(), 4));
        responseDto.setBondNetPrice(NumberUtil.roundHalfUp(qbTfScenarioResult.getTargetBondCleanPrice(), 4));
        responseDto.setConversionFactor(NumberUtil.roundHalfUp(qbTfScenarioResult.getTargetBondConversionFactor(), 4));
        responseDto.setBondFullPrice(NumberUtil.roundHalfUp(qbTfScenarioResult.getTargetBondDirtyPrice(), 4));
        responseDto.setBondCode(qbTfScenarioResult.getTargetBondId());
        responseDto.setCdcDuration(NumberUtil.roundHalfUp(qbTfScenarioResult.getTargetBondModifiedDuration(), 4));
        responseDto.setYield(NumberUtil.roundHalfUp(NumberUtil.multiply(qbTfScenarioResult.getTargetBondYtm(), 100), 4));
        responseDto.setHoldPnl(NumberUtil.roundHalfUp(qbTfScenarioResult.getHoldPnl(), 4));

        Map<String, ClosePositionDto> profitLossByYield = new TreeMap<String, ClosePositionDto>();
        for (KeyValuePair scenarioBasis : qbTfScenarioResult.getScenarioBasis()) {
            ClosePositionDto closePositionDto = new ClosePositionDto();
            closePositionDto.setYield(NumberUtil.roundHalfUp(NumberUtil.multiply(new BigDecimal(scenarioBasis.getKey()), 100), 4));
            closePositionDto.setBasis(NumberUtil.roundHalfUp(scenarioBasis.getValue(), 4));
            for (KeyValuePair scenarioIrr : qbTfScenarioResult.getScenarioIrr()) {
                if (scenarioIrr.getKey().equals(scenarioBasis.getKey())) {
                    closePositionDto.setIrr(NumberUtil.roundHalfUp(NumberUtil.multiply(scenarioIrr.getValue(), 100), 4));
                    break;
                }
            }
            for (KeyValuePair scenarioNetBasis : qbTfScenarioResult.getScenarioNetBasis()) {
                if (scenarioNetBasis.getKey().equals(scenarioBasis.getKey())) {
                    closePositionDto.setNetBasis(NumberUtil.roundHalfUp(scenarioNetBasis.getValue(), 4));
                    break;
                }
            }
            for (KeyValuePair scenarioBondCleanPrice : qbTfScenarioResult.getScenarioBondCleanPrice()) {
                if (scenarioBondCleanPrice.getKey().equals(scenarioBasis.getKey())) {
                    closePositionDto.setBondPrice(NumberUtil.roundHalfUp(scenarioBondCleanPrice.getValue(), 4));
                    break;
                }
            }
            for (KeyValuePair scenarioFuturesCleanPrice : qbTfScenarioResult.getScenarioFuturesCleanPrice()) {
                if (scenarioFuturesCleanPrice.getKey().equals(scenarioBasis.getKey())) {
                    closePositionDto.setFuturePrice(NumberUtil.roundHalfUp(scenarioFuturesCleanPrice.getValue(), 4));
                    break;
                }
            }
            for (KeyValuePair scenarioEachPnL : qbTfScenarioResult.getScenarioEachPnL()) {
                if (scenarioEachPnL.getKey().equals(scenarioBasis.getKey())) {
                    closePositionDto.setEachProfitLoss(NumberUtil.roundHalfUp(scenarioEachPnL.getValue(), 0));
                    break;
                }
            }
            for (KeyValuePair scenarioPnL : qbTfScenarioResult.getScenarioPnL()) {
                if (scenarioPnL.getKey().equals(scenarioBasis.getKey())) {
                    closePositionDto.setTotalProfitLoss(NumberUtil.roundHalfUp(scenarioPnL.getValue(), 0));
                    break;
                }
            }
            for (KeyValuePair scenarioYearYtm : qbTfScenarioResult.getScenarioYearYtm()) {
                if (scenarioYearYtm.getKey().equals(scenarioBasis.getKey())) {
                    closePositionDto.setAnnualizedYield(NumberUtil.roundHalfUp(NumberUtil.multiply(scenarioYearYtm.getValue(), 100), 4));
                    break;
                }
            }
            for (KeyValuePair scenarioImpactAmount : qbTfScenarioResult.getScenarioImpactAmount()) {
                if (scenarioImpactAmount.getKey().equals(scenarioBasis.getKey())) {
                    closePositionDto.setImpactAmount(NumberUtil.roundHalfUp(scenarioImpactAmount.getValue(), 2));
                }
            }

            profitLossByYield.put(NumberUtil.roundHalfUp(NumberUtil.multiply(new BigDecimal(scenarioBasis.getKey()), 100), 4).toString(), closePositionDto);
        }

        SortUtil.sortByNumberKey(profitLossByYield);
        responseDto.setProfitLossByYield(profitLossByYield);
        return responseDto;
    }
    private FutureContractDto getFutureContract(List<FutureContractDto> futureContracts, String params, String symbol) {
        if (symbol.equals("tfKey")) {
            for (FutureContractDto futureContract : futureContracts) {
                if (futureContract.getTfKey().equals(params)) {
                    return futureContract;
                }
            }
        } else if (symbol.equals("tfId")) {
            for (FutureContractDto futureContract : futureContracts) {
                if (futureContract.getTfId().equals(params)) {
                    return futureContract;
                }
            }
        }
        return null;
    }
    @CacheMe(timeout = 3600)
    public List<KeyValuePair> getYieldByDeliverableBonds(List<BondInfo> deliverableBonds) {
        List<KeyValuePair> keyValuePairs = new ArrayList<KeyValuePair>();
        List<BondInfo> bondInfoWithoutPrice = new ArrayList<BondInfo>();
        for (BondInfo bondInfo : deliverableBonds) {
            BigDecimal bondOfrLatestPrice = calculationDao.getBondOfrLatestPrice(bondInfo.getBondKey(), bondInfo.getListedMarket(), true);
            BigDecimal price = bondOfrLatestPrice!=null ? bondOfrLatestPrice : calculationDao.getBondsCdcLatestPrice(bondInfo.getBondKey(), bondInfo.getListedMarket());
            if (price == null) {
                bondInfoWithoutPrice.add(bondInfo);
            } else {
                KeyValuePair keyValuePair = new KeyValuePair();
                keyValuePair.setKey(bondInfo.getBondCode());
                keyValuePair.setValue(NumberUtil.divide(price, 100).setScale(4,ROUND_HALF_UP));

                keyValuePairs.add(keyValuePair);
            }
        }
        deliverableBonds.removeAll(bondInfoWithoutPrice);
        return keyValuePairs;
    }

    private BigDecimal getCapitalCost(List<RepoRateDto> repoRateTypes) {
        for (RepoRateDto repoRateDto : repoRateTypes) {
            if (repoRateDto.getCode().equals(FR007)) {
                return repoRateDto.getPrice();
            }
        }
        return null;
    }
    @CacheMe(timeout = 3600)
    private RepoRateDto getDefaultRepoRateDto(List<RepoRateDto> repoRateDtos) {
        if (repoRateDtos == null || repoRateDtos.size() == 0) {
            return null;
        } else {
            for (RepoRateDto repoRateDto : repoRateDtos) {
                if (repoRateDto.getCode().equals(FR007)) {
                    return repoRateDto;
                }
            }
            return repoRateDtos.get(0);
        }

    }

    private ResponseDto convertToQdp(RequestDto requestDto, List<BondInfo> deliverableBonds, List<KeyValuePair> keyValuePairs) {
        List<FixedRateBondInfo> fixedRateBondInfos = new ArrayList<FixedRateBondInfo>();
        for (BondInfo bondInfo : deliverableBonds) {
            fixedRateBondInfos.add(convertToFixedRateBondInfo(bondInfo));
        }

        TradeInfoAnalysis tradeInfo = new TradeInfoAnalysis();
        tradeInfo.setMaturityDate(requestDto.getFutureContract().getDeliveryDate());
        tradeInfo.setTradeId(requestDto.getFutureContract().getTfId());
        tradeInfo.setDeliverableBondInfos(fixedRateBondInfos);

        QdpAnalysis qdp = new QdpAnalysis();
        qdp.setTradeInfo(tradeInfo);
        qdp.setDeliverableBondYtm(keyValuePairs);
        convertToQdp(requestDto, qdp);

        ResponseDto responseDto = calculateAndGenerateResponse(qdp, requestDto);
        getCtdBondKeyAndListedMarket(responseDto, deliverableBonds);
        return responseDto;
    }

    private ResponseDto convertToQdp(RequestDto requestDto, BondInfo ctdBondInfo, BondInfo currentBondInfo) {
        TradeInfoAnalysis tradeInfo = new TradeInfoAnalysis();
        tradeInfo.setMaturityDate(requestDto.getFutureContract().getDeliveryDate());
        tradeInfo.setTradeId(requestDto.getFutureContract().getTfId());
        if (requestDto.getBondNominalPrincipal() != null) {
            tradeInfo.setNotional(requestDto.getBondNominalPrincipal().longValue());
        }

        QdpAnalysis qdp = new QdpAnalysis();
        qdp.setTradeInfo(tradeInfo);
        qdp.setCtdBondInfo(convertToFixedRateBondInfo(ctdBondInfo));
        qdp.setTargetBondInfo(convertToFixedRateBondInfo(currentBondInfo));

        convertToQdp(requestDto, qdp);

        ResponseDto responseDto = calculateAndGenerateResponse(qdp, requestDto);

        responseDto.setCdcDuration(getBondCDCDuration(currentBondInfo.getBondKey(),currentBondInfo.getListedMarket()));

        responseDto.setCtdDuration(getBondCDCDuration(ctdBondInfo.getBondKey(),ctdBondInfo.getListedMarket()));

        return responseDto;
    }

    private FixedRateBondInfo convertToFixedRateBondInfo(BondInfo bondInfo) {
        FixedRateBondInfo fixedRateBondInfo = new FixedRateBondInfo();

        fixedRateBondInfo.setFixedCoupon(NumberUtil.divide(bondInfo.getFixedCoupon(), 100));
        fixedRateBondInfo.setStartDate(bondInfo.getStartDate());
        fixedRateBondInfo.setMaturityDate(bondInfo.getMaturityDate());
        fixedRateBondInfo.setPaymentFrequency(bondInfo.getPaymentFrequency().getCalculatorEnumName());
        fixedRateBondInfo.setTradeId(bondInfo.getBondCode());

        return fixedRateBondInfo;
    }

    private QdpAnalysis convertToQdp(RequestDto requestDto, QdpAnalysis qdp) {

        qdp.setDirection(requestDto.getLongShortSymbol().getCalculatorEnumName());
        qdp.setImpactRate(requestDto.getImpactCost());
        qdp.setFuturesPrice(requestDto.getFuturePrice());
        qdp.setFundingRate(NumberUtil.divide(requestDto.getCapitalCost(), 100));
        qdp.setCtdBondFullPrice(requestDto.getCtdFullPrice());
        if (requestDto.getYield() != null) {
            qdp.setTargetBondYtm(NumberUtil.divide(requestDto.getYield(), 100));
        }
        qdp.setTargetBondDirtyPrice(requestDto.getBondFullPrice());
        qdp.setTargetBondCleanPrice(requestDto.getBondNetPrice());
        qdp.setCurveYtm(NumberUtil.divide(requestDto.getCurveYtm(), 100));
        qdp.setCurveFuturesPrice(requestDto.getCurveFuturesPrice());

        qdp.setOpenPositionDate(requestDto.getOpenPositionDate());
        qdp.setClosePositionDate(requestDto.getClosePositionDate());
        return qdp;
    }

    @CacheMe(timeout = 3600)
    public List<BondInfo> getDeliverableBonds(String tfKey) {
        List<Map<String, String>> deliverableBondKeys = basisAnalysisDao.getDeliverableBondKeys(tfKey);
        if (deliverableBondKeys != null && deliverableBondKeys.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (Map<String, String> map : deliverableBondKeys) {
                sb.append("'").append(map.get("Bond_Key")).append("',");
            }
            String inString = sb.toString().substring(1, sb.length() - 2);
            List<BondInfo> deliverableBonds = basisAnalysisDao.getDeliverableBonds(inString);
            return deliverableBonds;
        }
        return new ArrayList<>();
    }


    private BigDecimal getFuturePrice(String tfId) {
        return redisGatewayService.getFutureLastPrice(tfId);
    }

    private List<RepoRateDto> getRepoRateTypes() {
        return repoRateGatewayService.retrieveRepoRatesWithPrice();
    }

    @Override
    public List<BondPrimaryKeyDto> findBondsByNamePrifix(String namePrifix) {
        return basisAnalysisDao.findBondsByNamePrefix(namePrifix);
    }
}
