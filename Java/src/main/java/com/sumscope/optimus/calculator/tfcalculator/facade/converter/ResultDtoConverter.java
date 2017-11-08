package com.sumscope.optimus.calculator.tfcalculator.facade.converter;

import com.sumscope.optimus.calculator.planalysis.commons.util.NumberUtil;
import com.sumscope.optimus.calculator.planalysis.model.dto.RepoRateDto;
import com.sumscope.optimus.calculator.planalysis.model.qdp.BondYieldWithKeyValuePair;
import com.sumscope.optimus.calculator.planalysis.model.qdp.KeyValuePair;
import com.sumscope.optimus.calculator.shared.enums.BondPriceType;
import com.sumscope.optimus.calculator.shared.enums.YieldTypeEnum;
import com.sumscope.optimus.calculator.shared.facade.converter.BondInfoConverter;
import com.sumscope.optimus.calculator.shared.model.dbmodel.BondInfo;
import com.sumscope.optimus.calculator.shared.model.dbmodel.FutureContract;
import com.sumscope.optimus.calculator.shared.model.dbmodel.ScheduledBondInfo;
import com.sumscope.optimus.calculator.shared.model.dto.BondInfoDto;
import com.sumscope.optimus.calculator.shared.model.dto.BondPriceDto;
import com.sumscope.optimus.calculator.shared.model.dto.FutureContractDto;
import com.sumscope.optimus.calculator.shared.model.dto.FuturePriceDto;
import com.sumscope.optimus.calculator.shared.service.CalculationDataService;
import com.sumscope.optimus.calculator.tfcalculator.commons.enums.CalculationTarget;
import com.sumscope.optimus.calculator.tfcalculator.gatewayinvoke.dto.TFCalculatorQDPResponse;
import com.sumscope.optimus.calculator.tfcalculator.model.dbmodel.AbstractCalculationRequest;
import com.sumscope.optimus.calculator.tfcalculator.model.dbmodel.CalculationRequest;
import com.sumscope.optimus.calculator.tfcalculator.model.dbmodel.CalculationRequestForInit;
import com.sumscope.optimus.calculator.tfcalculator.model.dbmodel.CalculationRequestWithBondYieldType;
import com.sumscope.optimus.calculator.tfcalculator.model.dto.*;
import com.sumscope.optimus.commons.exceptions.BusinessRuntimeException;
import com.sumscope.optimus.commons.exceptions.BusinessRuntimeExceptionType;
import com.sumscope.optimus.commons.util.JsonUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.sumscope.optimus.calculator.tfcalculator.facade.converter.QdpConverterToResult.converterQdpToResult;
import static java.math.BigDecimal.ROUND_FLOOR;

/**
 * 将QDP计算结果及部分本地查询结果生成合适的结果Dto对象。
 * <p>
 * 在金融计算器服务场景中，本类是实现主要业务逻辑的功能模块之一。
 */
@Component
public class ResultDtoConverter {

    @Autowired
     private CalculationDataService calculationDataService;

    /**
     * 转换计算结果至对应ResultDto
     */
    public GetYieldResponseDto convertGetYieldResponseDto(TFCalculatorQDPResponse tfCalculatorQDPResponse,CalculationRequestWithBondYieldType calculationRequest) {
       GetYieldResponseDto bondYieldResponseDto = new GetYieldResponseDto();
       Result result = QdpConverterToResult.converterQdpToResult(tfCalculatorQDPResponse,calculationRequest.getBond().getBondCode());
       //设置债券详细
        NumberUtil.divide(calculationRequest.getBondPrice(),100);
       bondYieldResponseDto.setBondPrice(convertBondPriceDto(new BondPriceDto(),result.getBondYtm() , calculationRequest.getBondYieldType(), result));
       convertGetTFCalculatorResponseDto(bondYieldResponseDto,calculationRequest.getCalculationTarget(),result,calculationRequest);
        return bondYieldResponseDto;
    }

    /**
     * 转换计算结果至对应ResultDto
     */
    public BondChangedResponseDto convertBondChangedResponseDto(TFCalculatorQDPResponse tfCalculatorQDPResponse, CalculationRequestWithBondYieldType calculation ) {
        BondChangedResponseDto bondChangedResponseDto=new BondChangedResponseDto();
        Result result = converterQdpToResult(tfCalculatorQDPResponse, calculation.getBond().getBondCode());
        BondPriceDto bondPriceDto = new BondPriceDto();
        BigDecimal bondYtm =result.getBondYtm()!=null? result.getBondYtm():NumberUtil.divide(calculation.getBondPrice(),100) ;
        convertBondPriceDto(bondPriceDto,bondYtm,calculation.getBondYieldType(),result);
        bondChangedResponseDto.setBondPrice(bondPriceDto);
        if(calculation.getConvertionFactorDto()==null){
            if(result.getConversionFactors().size()>0){
                bondChangedResponseDto.setBondConvertionFactor(result.getConversionFactors().get(0).getConvertionFactor()); //如果在可交割卷窗口,这个值则不需要设置.
            }else{
                if(result.getBondYtm()==null){
                    throw new BusinessRuntimeException(BusinessRuntimeExceptionType.DATABASE_ERROR,"今日暂无收益率，请选择其他收益率或输入收益率");
                }
                throw new BusinessRuntimeException(BusinessRuntimeExceptionType.PARAMETER_INVALID,"QDP计算结果为空,请检查页面必填项稍后再试或者联系管理员");
            }
        }else{
            bondChangedResponseDto.setBondConvertionFactor(calculation.getConvertionFactorDto().getConvertionFactor());
        }
        convertGetTFCalculatorResponseDto(bondChangedResponseDto,calculation.getCalculationTarget(),result,calculation);
        return bondChangedResponseDto;
    }

    /**
     * 转换计算结果至对应ResultDto
     */

    public FutureInitResponseDto convertFutureInitResponseDto(TFCalculatorQDPResponse tfCalculatorQDPResponse, CalculationRequestForInit calculationRequestForInit) {
        FutureInitResponseDto futureInitResponseDto=new FutureInitResponseDto();
        List<BondInfoDto> bondInfo=new ArrayList<>();
        String bondCode=null;
        //获取债劵code
        bondCode = setScheduleBondInfoOrSelectableBonds(tfCalculatorQDPResponse, calculationRequestForInit, futureInitResponseDto, bondInfo, bondCode);

        if (setExceptionInfo(calculationRequestForInit, bondCode, futureInitResponseDto)) return futureInitResponseDto;
        FuturePriceDto futurePriceDto=new FuturePriceDto();
        futurePriceDto.setFuturePrice(calculationRequestForInit.getFuturePrice());
        futurePriceDto.setLastUpdateDatetime(new Date());
        futureInitResponseDto.setFutruePrice(futurePriceDto);
        if(tfCalculatorQDPResponse!=null){
        Result result = converterQdpToResult(tfCalculatorQDPResponse, bondCode);
        if(futureInitResponseDto.getBondConvertionFactors()==null&&calculationRequestForInit.getBond()!=null
                &&calculationRequestForInit.getScheduledBondInfos().size()<=0&&calculationRequestForInit.getSelectableBonds().size()<=0){
            futureInitResponseDto.setBondConvertionFactors(result.getConversionFactors()!=null?result.getConversionFactors():null);
        }
        futurePriceDto.setFuturePrice(calculationRequestForInit.getFuturePrice()!=null?calculationRequestForInit.getFuturePrice().setScale(3,BigDecimal.ROUND_HALF_UP):result.getFuturePrice().setScale(3,BigDecimal.ROUND_HALF_UP));
        BondPriceDto bondPriceDto = new BondPriceDto();
        YieldTypeEnum yieldTypeEnum = getYieldTypeEnum(calculationRequestForInit.getBondYieldWithKeyValuePair(),bondCode);
        BigDecimal bondYtm = calculationRequestForInit.getBondPrice()!=null ? calculationRequestForInit.getBondPrice() : result.getBondYtm();
        convertBondPriceDto(bondPriceDto,bondYtm,yieldTypeEnum,result);//YieldTypeEnum.ofr
        futureInitResponseDto.setDefaultBondPrice(bondPriceDto);

        convertGetTFCalculatorResponseDto(futureInitResponseDto,calculationRequestForInit.getCalculationTarget(),result,calculationRequestForInit);
        }
        else{
            futureInitResponseDto.setException("暂无收益率，请输入收益率");
        }
        return futureInitResponseDto;
    }

    private boolean setExceptionInfo(CalculationRequestForInit calculationRequestForInit, String bondCode, FutureInitResponseDto futureInitResponseDto) {
        if(calculationRequestForInit.getCalculationTarget()== CalculationTarget.IRR_BASE &&bondCode==null&&calculationRequestForInit.getBondYieldType()==null){
            List<ScheduledBondInfo> scheduledBonds = calculationDataService.getScheduledBonds(calculationRequestForInit.getFutureContract().getBondTerm());
            futureInitResponseDto.setScheduledBondInfos(scheduledBonds);
            futureInitResponseDto.setException(BusinessRuntimeExceptionType.DATABASE_ERROR.getExceptionCode()+","+BusinessRuntimeExceptionType.DATABASE_ERROR.getExceptionInfoCN() +",今日暂无收益率，请选择其他收益率或输入收益率。");
            return true;
        }else if(calculationRequestForInit.getCalculationTarget()==CalculationTarget.BOND_PRICE &&bondCode==null&&calculationRequestForInit.getBondYieldType()==null){
            List<ScheduledBondInfo> scheduledBonds = calculationDataService.getScheduledBonds(calculationRequestForInit.getFutureContract().getBondTerm());
            futureInitResponseDto.setScheduledBondInfos(scheduledBonds);
            BPMainResultDto bp = new BPMainResultDto();
            bp.setIrr(NumberUtil.multiply(calculationRequestForInit.getIrr(),100));
            bp.setBasis(calculationRequestForInit.getBasis());
            bp.setNetBasis(calculationRequestForInit.getNetBasis());
            futureInitResponseDto.setCalculationMainResult(bp);
            futureInitResponseDto.setException(BusinessRuntimeExceptionType.DATABASE_ERROR.getExceptionCode()+","+BusinessRuntimeExceptionType.DATABASE_ERROR.getExceptionInfoCN() +",该未发国债票息率为空，请手动输入债券票息率。");
            return true;
        }
        return false;
    }

    /**
     * 设置债劵列表 和 转换因子 注：债劵列表为包括未发国债
     * @param tfCalculatorQDPResponse
     * @param calculationRequestForInit
     * @param futureInitResponseDto
     * @param bondInfo
     * @param bondCode
     * @return
     */
    private String setScheduleBondInfoOrSelectableBonds(TFCalculatorQDPResponse tfCalculatorQDPResponse, CalculationRequestForInit calculationRequestForInit, FutureInitResponseDto futureInitResponseDto, List<BondInfoDto> bondInfo, String bondCode) {

//        KeyValuePair[] irr = tfCalculatorQDPResponse!=null? tfCalculatorQDPResponse.getIrr(): null;
//        getDefaultCTDBond(irr,calculationRequestForInit,futureInitResponseDto);
        setSelectableBonds(calculationRequestForInit.getSelectableBonds());
        if(calculationRequestForInit.getSelectableBonds()!=null&&calculationRequestForInit.getSelectableBonds().size()>0){
            bondCode = calculationRequestForInit.getSelectableBonds().get(0).getBondCode(); //取出默认第一个的bondkey
            List<BondInfo> selectableBonds = calculationRequestForInit.getSelectableBonds();
            for(BondInfo BondInfo:selectableBonds){
                BondInfoDto bondInfoDto = BondInfoConverter.convertBondInfoDto(BondInfo);
                bondInfo.add(bondInfoDto);
            }
            futureInitResponseDto.setSelectableBonds(bondInfo);
            futureInitResponseDto.setBondConvertionFactors(calculationRequestForInit.getBondConvertionFactors());
        }
        if(calculationRequestForInit.getScheduledBondInfos()!=null&&calculationRequestForInit.getScheduledBondInfos().size()>0){
            bondCode=calculationRequestForInit.getScheduledBondInfos().get(0).getId();
            futureInitResponseDto.setScheduledBondInfos(calculationRequestForInit.getScheduledBondInfos());
            futureInitResponseDto.setBondConvertionFactors(tfCalculatorQDPResponse!=null? converterQdpToResult(tfCalculatorQDPResponse, bondCode).getConversionFactors(): null);
        }
        List<ScheduledBondInfo> scheduledBondInfos = calculationRequestForInit.getScheduledBondInfos();
        List<BondInfo> selectableBonds = calculationRequestForInit.getSelectableBonds();
        if(((scheduledBondInfos==null||"".equals(scheduledBondInfos))||scheduledBondInfos.size()<=0)&&(selectableBonds==null||"".equals(selectableBonds)||selectableBonds.size()<=0)){
            List<BondInfo> selectableBond = new ArrayList<>();
            selectableBond.add(calculationRequestForInit.getBond());
            for(BondInfo BondInfo:selectableBond){
                BondInfoDto bondInfoDto = BondInfoConverter.convertBondInfoDto(BondInfo);
                bondInfo.add(bondInfoDto);
            }
            futureInitResponseDto.setSelectableBonds(bondInfo);
            futureInitResponseDto.setBondConvertionFactors(calculationRequestForInit.getBondConvertionFactors());
            bondCode=calculationRequestForInit.getBond()!=null?calculationRequestForInit.getBond().getBondCode():null;
        }
        return bondCode;
    }

    /**
     * 转换计算结果至对应ResultDto
     */
    public CalculationResponseDto convertCalculationResponseDto(TFCalculatorQDPResponse tfCalculatorQDPResponse, CalculationRequest calculationRequest, YieldTypeEnum yieldType) {
        CalculationResponseDto calculationResponseDto=new CalculationResponseDto();
        FuturePriceDto dto = new FuturePriceDto();
        dto.setFuturePrice(calculationRequest.getFuturePrice());
        dto.setLastUpdateDatetime(new Date());
        calculationResponseDto.setFutruePrice(dto);
        String bondCode = calculationRequest.getBond().getBondCode(); //取出默认第一个的bondkey
        Result result = converterQdpToResult(tfCalculatorQDPResponse, bondCode);
        BondPriceDto bondPriceDto = new BondPriceDto();
        BigDecimal bondYtm=result.getBondYtm();
//        if(calculationRequest.getBondPriceType()== BondPriceType.YIELD){
//            bondYtm=calculationRequest.getBondPrice()!=null ? NumberUtil.divide(calculationRequest.getBondPrice(),100) : result.getBondYtm();
//        }else{
//            bondYtm= result.getBondYtm();
//        }
//        if(bondYtm==null){
//            throw new BusinessRuntimeException(BusinessRuntimeExceptionType.PARAMETER_INVALID, "暂无收益率,请输入收益率!");
//        }
        convertBondPriceDto(bondPriceDto,bondYtm,yieldType,result);
        calculationResponseDto.setBondPrice(bondPriceDto);
        calculationRequest.setIrr(calculationRequest.getIrr()!=null ?calculationRequest.getIrr() :null);
        calculationRequest.setBasis(calculationRequest.getBasis()!=null ? calculationRequest.getBasis().setScale(4,BigDecimal.ROUND_HALF_UP) : null);
        calculationRequest.setNetBasis(calculationRequest.getNetBasis()!=null ?calculationRequest.getNetBasis().setScale(4,BigDecimal.ROUND_HALF_UP) : null);
        calculationResponseDto.setBondConvertionFactor(result.getConversionFactors()!=null?result.getConversionFactors().get(0).getConvertionFactor():null);
        convertGetTFCalculatorResponseDto(calculationResponseDto,calculationRequest.getCalculationTarget(),result,calculationRequest);
        return calculationResponseDto;
    }

    /**
     * 转换 收益率 净价 全价
     * @param bondPriceDto
     * @param bondPrice
     * @param yieldType
     * @param result
     * @return
     */
    private BondPriceDto convertBondPriceDto(BondPriceDto bondPriceDto,BigDecimal bondPrice,YieldTypeEnum yieldType,Result result){
        bondPriceDto.setYield(bondPrice!=null ? NumberUtil.multiply(bondPrice,100).setScale(4,BigDecimal.ROUND_HALF_UP):null);
        bondPriceDto.setYieldType(yieldType);
        bondPriceDto.setNetPrice(result.getBondCleanPrice().setScale(4,BigDecimal.ROUND_HALF_UP));
        bondPriceDto.setFullPrice(result.getBondDirtyPrice().setScale(4,BigDecimal.ROUND_HALF_UP));
        bondPriceDto.setLastBondUpdateDate(new Date());
        return bondPriceDto;
    }

    /**
     * 根据QDP返回结果组装返回给web端的responseDto
     * @param abstractTFCalculatorResponseDto
     * @param calculationTarget
     * @param result
     */
    private void convertGetTFCalculatorResponseDto(AbstractTFCalculatorResponseDto abstractTFCalculatorResponseDto, CalculationTarget calculationTarget, Result result, AbstractCalculationRequest request){
        if(calculationTarget.equals(CalculationTarget.IRR_BASE)) {
            IRRMainResultDto irr = new IRRMainResultDto();
            BeanUtils.copyProperties(result,irr);
            irr.setBasis(result.getBasis().setScale(4,BigDecimal.ROUND_HALF_UP));
            irr.setNetBasis(result.getNetBasis().setScale(4,BigDecimal.ROUND_HALF_UP));
            irr.setIrr(NumberUtil.multiply(result.getIrr(),100).setScale(4,BigDecimal.ROUND_HALF_UP));
            irr.setCalculationType(CalculationTarget.IRR_BASE);
            abstractTFCalculatorResponseDto.setCalculationMainResult(irr);
        } else if(calculationTarget.equals(CalculationTarget.BOND_PRICE)){
            BPMainResultDto bp = new BPMainResultDto();
            bp.setYield(result.getBondYtm()!=null ? NumberUtil.multiply(result.getBondYtm(),100).setScale(4,BigDecimal.ROUND_HALF_UP) : null);//收益率剩余100
            bp.setIrr(request.getIrr()!=null ? NumberUtil.multiply(request.getIrr(),100): NumberUtil.multiply(result.getIrr(),100).setScale(4,BigDecimal.ROUND_HALF_UP));
            bp.setBasis(request.getBasis()!=null ? request.getBasis().setScale(4,BigDecimal.ROUND_HALF_UP) : result.getBasis().setScale(4,BigDecimal.ROUND_HALF_UP));
            bp.setNetBasis(request.getNetBasis()!=null ? request.getNetBasis().setScale(4,BigDecimal.ROUND_HALF_UP):result.getNetBasis().setScale(4,BigDecimal.ROUND_HALF_UP));
            bp.setBondFullPrice(result.getBondDirtyPrice());
            bp.setBondNetPrice(result.getBondCleanPrice());
            bp.setYieldByDay(NumberUtil.multiply(result.getMaturityMoveDayYtm(),100));
            abstractTFCalculatorResponseDto.setCalculationMainResult(bp);
        }else if(calculationTarget.equals(CalculationTarget.FUTURE_ANALYSIS)){
            FSAMainResultDto fsa = new FSAMainResultDto();//场景分析
            fsa.setFuturePrice(result.getFuturePrice()!=null ? result.getFuturePrice().setScale(3,BigDecimal.ROUND_HALF_UP): null);
            fsa.setIrr(request.getIrr()!=null ?NumberUtil.multiply( request.getIrr(),100):NumberUtil.multiply(result.getIrr(),100).setScale(4,BigDecimal.ROUND_HALF_UP));
            fsa.setBasis(request.getBasis()!=null ? request.getBasis().setScale(4,BigDecimal.ROUND_HALF_UP) : result.getBasis().setScale(4,BigDecimal.ROUND_HALF_UP));
            fsa.setNetBasis(request.getNetBasis()!=null ? request.getNetBasis().setScale(4,BigDecimal.ROUND_HALF_UP):result.getNetBasis().setScale(4,BigDecimal.ROUND_HALF_UP));
            abstractTFCalculatorResponseDto.setCalculationMainResult(fsa);
        }else if(calculationTarget.equals(CalculationTarget.FUTURE_THEORETICAL_PRICE)){
            FTPMainResultDto ftp = new FTPMainResultDto();//期货理论价格
            ftp.setFuturePrice(result.getFuturePrice()!=null ? result.getFuturePrice().setScale(3,BigDecimal.ROUND_HALF_UP): null);
            ftp.setNetBasis(result.getNetBasis().setScale(4,BigDecimal.ROUND_HALF_UP));
            ftp.setIrr(NumberUtil.multiply(result.getIrr(),100).setScale(4,BigDecimal.ROUND_HALF_UP));
            ftp.setBasis(result.getBasis().setScale(4,BigDecimal.ROUND_HALF_UP));
            abstractTFCalculatorResponseDto.setCalculationMainResult(ftp);
        }else{
            abstractTFCalculatorResponseDto.setCalculationMainResult(null);
        }
        setTFCalculatorDetailsResponse(abstractTFCalculatorResponseDto, result);
    }

    /**
     * 设置计算后TFCalculator的详细信息
     * @param abstractTFCalculatorResponseDto
     * @param result
     */
    private void setTFCalculatorDetailsResponse(AbstractTFCalculatorResponseDto abstractTFCalculatorResponseDto, Result result) {
        abstractTFCalculatorResponseDto.setInterestsRateSpread(result.getSpread());
        abstractTFCalculatorResponseDto.setReceiptPrice(result.getInvoicePrice());
        abstractTFCalculatorResponseDto.setWeightedAverageInterest(result.getTimeWeightedCoupon());
        abstractTFCalculatorResponseDto.setFutureSpotSpread(result.getMargin());
        abstractTFCalculatorResponseDto.setInterestOnTradingDate(result.getAiEnd());
        abstractTFCalculatorResponseDto.setInterestOnSettlementDate(result.getAiStart());
        abstractTFCalculatorResponseDto.setInterestDuringHolding(result.getCoupon());
        abstractTFCalculatorResponseDto.setCarry(result.getPnL());

    }

    /**
     *  期货价格刷新触发接口
     */
    public GetFuturePriceResponseDto convertGetFuturePriceResponseDto(TFCalculatorQDPResponse tfCalculatorQDPResponse,CalculationRequest calculationRequest) {
        GetFuturePriceResponseDto futurePriceResponseDto = new GetFuturePriceResponseDto();
        futurePriceResponseDto.setFuturePrice(calculationRequest.getFuturePrice());
        futurePriceResponseDto.setLastFutureUpdateDate(new Date());
        Result result = converterQdpToResult(tfCalculatorQDPResponse, calculationRequest.getBond().getBondCode());
        if(calculationRequest.getIrr()!=null && !"".equals(calculationRequest.getIrr())){
            result.setIrr(NumberUtil.multiply(calculationRequest.getIrr(),100));
            calculationRequest.setIrr(calculationRequest.getIrr());
        }
        convertGetTFCalculatorResponseDto(futurePriceResponseDto,calculationRequest.getCalculationTarget(),result,calculationRequest);
        return futurePriceResponseDto;
    }


    /**
     * 根据参数转换产生InitResponseDto
     * TFCalculatorQDPResponse result, 等测试通过再传
     */
    public IniResponseDto convertInitResponseDto(CalculationRequestForInit calculationRequestForInit,TFCalculatorQDPResponse tfCalculatorQDPResponse) {
        IniResponseDto iniResponseDto=new IniResponseDto();
        FuturePriceDto futurePriceDto=new FuturePriceDto();
        BondPriceDto bondPriceDto = new BondPriceDto();
        futurePriceDto.setFuturePrice(calculationRequestForInit.getFuturePrice());
        futurePriceDto.setLastUpdateDatetime(new Date());
        iniResponseDto.setDefaultFuturePrice(futurePriceDto);
//        KeyValuePair[] irr = tfCalculatorQDPResponse.getIrr();
//        getDefaultCTDBond(irr,calculationRequestForInit,iniResponseDto);
        List<BondInfo> selectableBonds = calculationRequestForInit.getSelectableBonds();
        setSelectableBonds(selectableBonds);
        iniResponseDto.setBondConvertionFactor(calculationRequestForInit.getBondConvertionFactors());
        iniResponseDto.setDeliverableBonds(calculationRequestForInit.getSelectableBonds());
        String bondKey = calculationRequestForInit.getSelectableBonds().get(0).getBondKey();
        String bondCode = calculationRequestForInit.getSelectableBonds().get(0).getBondCode(); //取出默认第一个的
        Result result = converterQdpToResult(tfCalculatorQDPResponse, bondCode);
        BigDecimal bondOfrRate = calculationDataService.getBondOfrLatestPrice(bondKey, "CIB", true);
        YieldTypeEnum yieldTypeEnum1 = getYieldTypeEnum(calculationRequestForInit.getBondYieldWithKeyValuePair(), bondCode);
        convertBondPriceDto(bondPriceDto,NumberUtil.divide(bondOfrRate,100),yieldTypeEnum1,result);
        convertGetTFCalculatorResponseDto(iniResponseDto,CalculationTarget.IRR_BASE,result,calculationRequestForInit);
        if(bondPriceDto.getYield()==null){
            bondPriceDto.setYield(calculationDataService.getBondsCdcLatestPrice(bondKey, "CIB"));
            bondPriceDto.setYieldType(yieldTypeEnum1);
        }
        iniResponseDto.setDefaultBondPrice(bondPriceDto);
        return iniResponseDto;
    }

    private void setSelectableBonds(List<BondInfo> selectableBonds) {
        if(selectableBonds!=null && selectableBonds.size()>2){
            List<BondInfo> selectableBond = new ArrayList<>();
            selectableBond.add(selectableBonds.get(0));
            selectableBond.add(selectableBonds.get(1));
            Collections.sort(selectableBond);
            selectableBonds.removeAll(selectableBond);
            selectableBonds.addAll(0,selectableBond);
        }
    }

    private YieldTypeEnum getYieldTypeEnum(List<BondYieldWithKeyValuePair> list, String bondCode){
        if(list!=null && list.size()>0){
            for(BondYieldWithKeyValuePair  bondYield:list){
                if(bondYield.getKey().equals(bondCode)){
                    return bondYield.getYieldTypeEnum();
                }
            }
        }
        return YieldTypeEnum.ofr;
    }
    /**
     * 取出irr中值最高的那个作为默认的债券代码
     * @param irr
     * @param calculationRequestForInit
     */
    private void getDefaultCTDBond(KeyValuePair[] irr,CalculationRequestForInit calculationRequestForInit,AbstractTFCalculatorResponseDto tfCalculatorResponseDto) {
        if(irr!=null && irr.length>0){
        for(int i=0;i<irr.length;i++){
             for(int j=i+1;j<irr.length;j++) {
                 if (irr[i].getValue().compareTo(irr[j].getValue())==1) {
                     KeyValuePair temp = irr[i];
                     irr[i] = irr[j];
                     irr[j] = temp;
                 }
             }
         }
        decideSelectableBonds(irr, calculationRequestForInit,tfCalculatorResponseDto);
        }
    }

    private void decideSelectableBonds(KeyValuePair[] irr, CalculationRequestForInit calculationRequestForInit,AbstractTFCalculatorResponseDto tfCalculatorResponseDto) {
        int index = 0;
        if(calculationRequestForInit.getSelectableBonds()!=null&&calculationRequestForInit.getSelectableBonds().size()>0){
            for(BondInfo bondInfo:calculationRequestForInit.getSelectableBonds()){
                if(irr.length==0){
                    tfCalculatorResponseDto.setException("今日暂无收益率，请选择其他收益率或输入收益率");
//                    throw new BusinessRuntimeException(CalculatorExceptionType.INPUT_PARAM_INVALID, "计算返回的结果集为空");
                }
                if(irr.length!=0 && bondInfo.getBondCode().equals(irr[irr.length-1].getKey())){
                    calculationRequestForInit.getSelectableBonds().remove(index);
                    calculationRequestForInit.getSelectableBonds().add(0,bondInfo);
                    return;
                }
                ++index;
            }
        }
        if(calculationRequestForInit.getScheduledBondInfos()!=null&&calculationRequestForInit.getScheduledBondInfos().size()>0){
            for(ScheduledBondInfo bondInfo:calculationRequestForInit.getScheduledBondInfos()){
                if(irr.length==0){
                    tfCalculatorResponseDto.setException("今日暂无收益率，请选择其他收益率或输入收益率");
//                   throw new BusinessRuntimeException(CalculatorExceptionType.INPUT_PARAM_INVALID, "计算返回的结果集为空");
                }
                if(irr.length!=0 && bondInfo.getId().equals(irr[irr.length-1].getKey())){
                    calculationRequestForInit.getScheduledBondInfos().remove(index);
                    calculationRequestForInit.getScheduledBondInfos().add(0,bondInfo);
                    return;
                }
                ++index;
            }
        }

    }


    /**
     * 根据参数转换产生InitialResponsesDto，该方法应在convertInitResponseDto()方法后执行，因为其中的参数由上述方法获得。
     */
    public InitialResponsesDto convertInitialResponsesDto(IniResponseDto initResponseDto, List<FutureContract> futureContracts, List<RepoRateDto> repoRates) {
        InitialResponsesDto initialResponsesDto=new InitialResponsesDto();
        initialResponsesDto.setRepoRates(repoRates);
        List<FutureContractDto> futureContractList=new ArrayList<FutureContractDto>();
        for(FutureContract futureContract : futureContracts){
            FutureContractDto futureContractDto=new FutureContractDto();
            futureContractDto.setMaturityDate(JsonUtil.writeValueAsString(futureContract.getDeliveryDate()));
            futureContractDto.setStartDate(JsonUtil.writeValueAsString(futureContract.getTradingDate().getTime()));
            BeanUtils.copyProperties(futureContract,futureContractDto);
            futureContractList.add(futureContractDto);
        }
        initialResponsesDto.setCalculationResult(initResponseDto);
        initialResponsesDto.setFutures(futureContractList);
        initialResponsesDto.setCalculationResult(initResponseDto);
        return initialResponsesDto;
    }

}
