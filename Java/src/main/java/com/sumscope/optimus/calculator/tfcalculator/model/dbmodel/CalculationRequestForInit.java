package com.sumscope.optimus.calculator.tfcalculator.model.dbmodel;

import com.sumscope.optimus.calculator.planalysis.model.qdp.BondYieldWithKeyValuePair;
import com.sumscope.optimus.calculator.shared.enums.YieldTypeEnum;
import com.sumscope.optimus.calculator.shared.model.dbmodel.BondInfo;
import com.sumscope.optimus.calculator.shared.model.dbmodel.ScheduledBondInfo;
import com.sumscope.optimus.calculator.shared.model.dto.BondConvertionFactorDto;

import java.util.List;

/**
 * 本数据主要在父类基础上包括了可交割卷或者未发债卷列表信息。若是可交割卷则同时获得各卷的转换因子。用于计算的债卷价格类型也在本数据内给出
 */
public class CalculationRequestForInit extends AbstractCalculationRequest implements Cloneable {
    /**
     * 债券的收益率
     */
    private List<BondYieldWithKeyValuePair> bondYieldWithKeyValuePair;

    /**
     * 可交割卷列表
     */
    private List<BondInfo> selectableBonds;

    /**
     * 未发国债列表
     */
    private List<ScheduledBondInfo> scheduledBondInfos;

    /**
     * 仅在获得可交割卷窗口从数据库获取当前转换因子
     * <BondConvertionFactorDto>
     */
    private List<BondConvertionFactorDto> bondConvertionFactors;

    /**
     * 表明用于计算的债卷收益率是哪种收益率
     */
    private YieldTypeEnum bondYieldType;

    public List<ScheduledBondInfo> getScheduledBondInfos() {
        return scheduledBondInfos;
    }

    public void setScheduledBondInfos(List<ScheduledBondInfo> scheduledBondInfos) {
        this.scheduledBondInfos = scheduledBondInfos;
    }

    public List<BondInfo> getSelectableBonds() {
        return selectableBonds;
    }

    public void setSelectableBonds(List<BondInfo> selectableBonds) {
        this.selectableBonds = selectableBonds;
    }

    public List<BondConvertionFactorDto> getBondConvertionFactors() {
        return bondConvertionFactors;
    }

    public void setBondConvertionFactors(List<BondConvertionFactorDto> bondConvertionFactors) {
        this.bondConvertionFactors = bondConvertionFactors;
    }

    public YieldTypeEnum getBondYieldType() {
        return bondYieldType;
    }

    public void setBondYieldType(YieldTypeEnum bondYieldType) {
        this.bondYieldType = bondYieldType;
    }

    public List<BondYieldWithKeyValuePair> getBondYieldWithKeyValuePair() {
        return bondYieldWithKeyValuePair;
    }

    public void setBondYieldWithKeyValuePair(List<BondYieldWithKeyValuePair> bondYieldWithKeyValuePair) {
        this.bondYieldWithKeyValuePair = bondYieldWithKeyValuePair;
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
