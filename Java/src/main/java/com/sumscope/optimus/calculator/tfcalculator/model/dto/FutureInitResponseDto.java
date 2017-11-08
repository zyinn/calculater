package com.sumscope.optimus.calculator.tfcalculator.model.dto;

import com.sumscope.optimus.calculator.shared.model.dbmodel.ScheduledBondInfo;
import com.sumscope.optimus.calculator.shared.model.dto.BondConvertionFactorDto;
import com.sumscope.optimus.calculator.shared.model.dto.BondInfoDto;
import com.sumscope.optimus.calculator.shared.model.dto.BondPriceDto;
import com.sumscope.optimus.calculator.shared.model.dto.FuturePriceDto;

import java.util.List;

/**
 * 当期货改变是使用该响应Dto
 */
public class FutureInitResponseDto extends AbstractTFCalculatorResponseDto {

    /**
     * 当前期货价格
     */
    private FuturePriceDto futruePrice;

    /**
     * 结果由目标债卷决定，可能为当前期货的可交割卷或者与当前期货同期的未发国债，当用户在自选卷或者虚拟卷页面时则置空
     */
    private List<BondInfoDto> selectableBonds;

    /**
     * 用于QDP计算的债卷的最新价格。若在自选或者虚拟卷时为空
     */
    private BondPriceDto defaultBondPrice;

    /**
     * 用于计算的债卷的转换因子。若用于计算的债卷是可交割卷则转换因子从数据库得到，否则从QDP结果中得到
     */
    private List<BondConvertionFactorDto> bondConvertionFactors;

    /**
     * 未发国债列表
     */
    private List<ScheduledBondInfo> scheduledBondInfos;

    public List<ScheduledBondInfo> getScheduledBondInfos() {
        return scheduledBondInfos;
    }

    public void setScheduledBondInfos(List<ScheduledBondInfo> scheduledBondInfos) {
        this.scheduledBondInfos = scheduledBondInfos;
    }

    public FuturePriceDto getFutruePrice() {
        return futruePrice;
    }

    public void setFutruePrice(FuturePriceDto futruePrice) {
        this.futruePrice = futruePrice;
    }

    public List<BondInfoDto> getSelectableBonds() {
        return selectableBonds;
    }

    public void setSelectableBonds(List<BondInfoDto> selectableBonds) {
        this.selectableBonds = selectableBonds;
    }

    public BondPriceDto getDefaultBondPrice() {
        return defaultBondPrice;
    }

    public void setDefaultBondPrice(BondPriceDto defaultBondPrice) {
        this.defaultBondPrice = defaultBondPrice;
    }

    public List<BondConvertionFactorDto> getBondConvertionFactors() {
        return bondConvertionFactors;
    }

    public void setBondConvertionFactors(List<BondConvertionFactorDto> bondConvertionFactors) {
        this.bondConvertionFactors = bondConvertionFactors;
    }
}
